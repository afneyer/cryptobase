package com.afn.realstat;

import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.afn.util.QueryResultTable;

@Component
public class SalesFrequencyStatistics {

	public static final Logger importLog = LoggerFactory.getLogger("app");

	@Autowired
	PropertyTransactionRepository ptRepo;

	@Autowired
	RealPropertyRepository rpRepo;

	@Autowired
	DataSource afnDataSource;

	private static String filePath = AppFiles.getReportsDir();

	public SalesFrequencyStatistics() {
	}

	@Transactional
	public void timeBetweenSales2016() {

		String[] header = { "Apn", "Street", "City", "Zip", "Recent MLS", "Recent Date", "Earlier MLS", "Earlier Date",
				"Earlier Years" };
		CsvFileWriter cfw = new CsvFileWriter(getFile(), header);

		// find all propertyTransactions for 2016
		Date d1 = AfnDateUtil.of(2016, 1, 1);
		Date d2 = AfnDateUtil.of(2017, 1, 1);
		List<PropertyTransaction> pt2016 = ptRepo.findByCloseDateBetween(d1, d2);

		for (PropertyTransaction pt1 : pt2016) {
			RealProperty rp = pt1.getRealProperty();

			if (rp != null) {
				// find all transactions for the property
				Sort sort = new Sort(Direction.DESC, "closeDate");
				List<PropertyTransaction> transForProperty = ptRepo.findByRealPropertyAndCloseDateBefore(rp,
						pt1.getCloseDate(), sort);

				// assertTrue(transForProperty.size() > 0);

				// verify the first transaction is the property itself
				// assertTrue(transForProperty.get(0) == pt1);

				if (transForProperty.size() >= 1) {
					PropertyTransaction pt2 = transForProperty.get(0);
					d1 = pt2.getCloseDate();
					d2 = pt1.getCloseDate();
					// find years between closing dates
					long years = AfnDateUtil.yearsBetween(d1, d2);

					String line = new String();
					line += rp.getApn() + ",";
					line += rp.getPropertyAddress() + ",";
					line += rp.getPropertyCity() + ",";
					line += rp.getPropertyZip5() + ",";
					line += pt1.getMlsNo() + ",";
					line += pt1.getCloseDate() + ",";
					line += pt2.getMlsNo() + ",";
					line += pt2.getCloseDate() + ",";
					line += years;

					cfw.appendLine(line);
				}
			}
		}
		cfw.close();

	}

	public void salesByYearCityAndZip() {

		String query = "select year(closeDate) as closeYear, city, substring(zip,1,5) as zipCode, count(id) \n"
				+ "from property_transaction \n" + "where closeDate is not null \n"
				+ "group by closeYear, city, zipCode \n";

		CsvFileWriter.writeQueryResult(afnDataSource, query, getFile());

	}

	public void doubleEndedTransactionsByAgent() {

		String query = "select sum(case when(pt.listingAgent_id = pt.sellingAgent_id) then 1 else 0 end) as doubleEnded, \n"
				+ "(100*sum(case when(pt.listingAgent_id = pt.sellingAgent_id) then 1 else 0 end)/count(pt.id)) as percentDoubleEnded, count(pt.id) as allTrans, a.agentName \n"
				+ "from property_transaction pt, agent a \n"
				+ "where pt.sellingAgent_id = a.id or pt.listingAgent_id = a.id \n" + "group by a.agentName \n"
				+ "having count(pt.id) > 20 \n" + "order by percentDoubleEnded desc \n";

		CsvFileWriter.writeQueryResult(afnDataSource, query, getFile());
	}

	public void checkPropertyLinking() {

		String query = "select count(id) as totalCnt, count(realProperty_id) as linkedCnt, \n"
				+ "100*count(realProperty_id)/count(id) as linkedPercent,  substring(zip,1,5) as zipCode, city \n"
				+ "from property_transaction where substring(zip,1,5) in ('94610', '94611', '94618') group by zipCode, city;";

		CsvFileWriter.writeQueryResult(afnDataSource, query, getFile());
	}

	public void checkAgentLinking() {

		String query =

				"select substr(zip,1,5) as zipCode, " + "		count(ListingAgentName) as list1NameCnt, "
						+ "		100 * count(listingAgent_id) / count(ListingAgentName) as list1Percent, "
						+ "		count(SellingAgent1Name) as sell1NameCnt, "
						+ "		100 * count(sellingAgent_id) / count(SellingAgent1Name) as sell1Percent, "
						+ "		count(CoListAgentName) as list2NameCnt, "
						+ "		100 * count(listingAgent2_id) / count(CoListAgentName) as list2Percent, "
						+ "		count(CoSellAgentName) as sell2NameCnt, "
						+ "		100 * count(sellingAgent2_id) / count(CoSellAgentName) as sell2Percent "
						+ "		from property_transaction  "
						+ "		where substr(status,1,3) = 'SLD' and substr(zip,1,5) in ('94610','94611','94618') "
						+ "		group by substr(zip,1,5) ";

		System.out.print(query);

		CsvFileWriter.writeQueryResult(afnDataSource, query, getFile());
	}

	/**
	 * Computes Sales Activity
	 */
	public void salesActivtyByYearCityZipBuildingType() {

		String ptQuery = "select count(pt.id), propertyZip5,  city, buildingType, year(closeDate) \n"
				+ "from property_transaction pt, real_property rp \n"
				+ "where pt.realProperty_id = rp.id and substr(status,1,3) = 'SLD' and propertyZip5 in ('94610','94611','94618') \n"
				+ "and buildingType in ('DE','CO') \n" + "and city in ('PIEDMONT','OAKLAND') \n"
				+ "and closeDate is not null \n" + "group by propertyZip5, city, buildingType, year(closeDate) \n";

		QueryResultTable qrt = new QueryResultTable(afnDataSource, ptQuery);

		int numRows = qrt.getRowCount();

		// add a column for the total property count
		String[] column = new String[numRows + 1];
		column[0] = "totalCount";

		for (int i = 0; i < numRows; i++) {

			String zip = qrt.get(i, 1);
			String city = qrt.get(i, 2).toUpperCase();
			String landUse = RealStatUtil.convertBuildingTypeToLandUse(qrt.get(i, 3));

			long countOfProperties = rpRepo.countByZipCityLandUseCloseYear(zip, city, landUse);
			column[i + 1] = Long.toString(countOfProperties);

		}

		qrt.addColumn(column);

		int numCols = qrt.getColumnCount()-1;
		// Normalize table
		for (int i = 0; i < numRows; i++) {
				Double d0i = new Double(qrt.get(i, 0));
				Double t = new Double(qrt.get(i, numCols - 1));
				qrt.set(i, 0, Double.toString(d0i/t));
		}

		CsvFileWriter.writeQueryTable(qrt, getFile());

	}

	/**
	 * Computes Properties on the Market
	 */
	public void propertiesOnMarketByCityZipBuildingType() {

		LocalDate startDate = LocalDate.of(1998, 1, 4);
		LocalDate endDate = LocalDate.now();

		// iterate through all dates in intervals of a week
		LocalDate date = startDate;

		String ptQuery = onMarketQueryByDate(date);
		QueryResultTable baseTable = new QueryResultTable(afnDataSource, ptQuery);
		String[] dateCol = createDateCol(date, baseTable.getRowCount());
		baseTable.addColumn(dateCol);

		date = date.plus(1, ChronoUnit.WEEKS);

		while (date.isBefore(endDate)) {

			ptQuery = onMarketQueryByDate(date);
			QueryResultTable qrt = new QueryResultTable(afnDataSource, ptQuery);
			dateCol = createDateCol(date, qrt.getRowCount());
			qrt.addColumn(dateCol);

			baseTable.addQueryResultTable(qrt);

			date = date.plus(1, ChronoUnit.WEEKS);
		}

		CsvFileWriter.writeQueryTable(baseTable, getFile());
	}

	private String[] createDateCol(LocalDate date, int rowCount) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		String dateString = formatter.format(date);
		String[] col = new String[rowCount + 1];
		col[0] = "Date";
		for (int i = 1; i <= rowCount; i++) {
			col[i] = dateString;
		}
		return col;
	}

	private String onMarketQueryByDate(LocalDate date) {

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		String dateString = formatter.format(date);
		String ptQuery = "select count(*) as cnt, zip5, city, buildingType from property_transaction \n"
				+ "where (offMarketDate is not null and " + "'" + dateString + "'"
				+ " between listDate and date_add(offMarketDate,interval 1 day)) \n"
				+ "or ((offMarketDate is null) and substr(status,1,3) = 'ACT') \n"
				+ "group by zip5, city, buildingType; \n";
		System.out.println(ptQuery);
		return ptQuery;
	}

	private File getFile() {

		String functionName = Thread.currentThread().getStackTrace()[2].getMethodName();
		String fileName = functionName + ".csv";
		return new File(filePath, fileName);

	}
}
