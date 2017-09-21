package com.afn.realstat;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Example;

import com.afn.realstat.framework.SpringApplicationContext;

@Entity
@Table(name = "property_transaction", uniqueConstraints = @UniqueConstraint(columnNames = { "mlsNo" }), indexes = {
		@Index(name = "idx_zip5", columnList = "zip5"), @Index(name = "idx_city", columnList = "city"),
		@Index(name = "idx_buildingType", columnList = "buildingType"),
		@Index(name = "idx_sellingAgent", columnList = "sellingAgent_id"),
		@Index(name = "idx_sellingAgent2", columnList = "sellingAgent2_id"),
		@Index(name = "idx_listingAgent", columnList = "listingAgent_id"),
		@Index(name = "idx_listingAgent2", columnList = "listingAgent2_id"),
		@Index(name = "idx_listDate", columnList = "listDate"),
		@Index(name = "idx_listOffMarketDate", columnList = "offMarketDate"),
		})

public class PropertyTransaction extends AbstractEntity {

	public static final Logger log = LoggerFactory.getLogger("app");
	public static PropertyTransactionRepository repo;

	@Basic(optional = false)
	private Integer mlsNo;
	@ManyToOne(fetch = FetchType.LAZY)
	private RealProperty realProperty;
	@ManyToOne(fetch = FetchType.LAZY)
	private Agent listingAgent;
	@ManyToOne(fetch = FetchType.LAZY)
	private Agent listingAgent2;
	@ManyToOne(fetch = FetchType.LAZY)
	private Agent sellingAgent;
	@ManyToOne(fetch = FetchType.LAZY)
	private Agent sellingAgent2;

	private String status;
	private String dom;
	private String address;
	private String unit;
	private String city;
	private String area;
	private Double listPrice;
	private Double salesPrice;
	private String buildingType;
	private Double sqft;
	private Integer bedrooms;
	private Integer baths;
	private Integer bathsPartial;
	private String gar;
	private Integer garsp;
	private Date yrblt;
	private Double acres;
	private Double lotSqft;
	private Double hoaFee;
	private String hoaFeesFreq;
	private Date closeDate;
	private Integer age;
	private String apn;
	private String apnClean;
	private String censusTract;
	private String cityTransferTax;
	private String propClass;
	private String coListAgentBreNum;
	private String coSellAgentBreNum;
	private String compToSellingOffice;
	private String compType;
	private String county;
	private Integer cumulativeDomls;
	private Integer daysOnMls;
	private Date generalDate;
	private String distNeighborhoodSubdiv;
	private String dualVariable;
	private Integer fireplaces;
	private String floorUnitIsOn;
	private String foreclosureStatus;
	private String hoa;
	private String howSold;
	private Date inputDate;
	private Double listPriceSqft;
	private String listAgentBreNum;
	private Date listDate;
	private String listingBrokerBreNumber;
	private String listingType;
	private Integer numberOfOffers;
	private Date offMarketDate;
	private Double originalPrice;
	private Date pendingDate;
	private String pendingLitigation;
	private Double occPercent;
	private String pointOfSaleOrdinance;
	private String pool;
	private Double previousPrice;
	private Double price;
	private Date priceDate;
	private Double priceSqft;
	private Integer roomsTotal;
	private Double salePriceSqft;
	private String saleCoop;
	private Double saleLastListPrice;
	private Double saleOriginalPrice;
	private String saleRent;
	private String sellingBrokerBreNumber;
	private String soldAgentBreNum;
	private String taxId;
	private Double ticPercentOwnerOffered;
	private Integer unitsInComplex;
	private Date updateDate;
	private String source;
	private String specialInformation;
	private String state;
	private Date statusDate;
	private String stories;
	private String zip;
	private String zip5;
	private String zoning;
	private String coListAgentName;
	private String coListAgentLicenseId;
	private String coSellAgentName;
	private String coSellAgentLicenseId;
	private String listingAgentName;
	private String listingAgentLicenseId;
	private String sellingAgent1Name;
	private String sellingAgent1LicenseId;

	public PropertyTransaction() {
	}

	public PropertyTransaction(Integer id) {
		this.mlsNo = id;
	}

	@Override
	public String toString() {
		return String.format("PropTransaction [id=%d, Address='%s', Unit='%s', City='%s',Zip='%s']", mlsNo, address,
				unit, city, zip);
	}

	@Override
	public void clean() {
	}

	@Override
	public Example<PropertyTransaction> getRefExample() {
		Example<PropertyTransaction> e = Example.of(new PropertyTransaction(this.getMlsNo()));
		return e;
	}

	@Override
	public boolean isValid() {
		return (mlsNo != null);
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getDom() {
		return dom;
	}

	public void setDom(String dom) {
		this.dom = dom;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public Double getListPrice() {
		return listPrice;
	}

	public void setListPrice(Double listPrice) {
		this.listPrice = listPrice;
	}

	public Double getSalesPrice() {
		return salesPrice;
	}

	public void setSalesPrice(Double salesPrice) {
		this.salesPrice = salesPrice;
	}

	public String getBuildingType() {
		return buildingType;
	}

	public void setBuildingType(String buildingType) {
		this.buildingType = buildingType;
	}

	public Double getSqft() {
		return sqft;
	}

	public void setSqft(Double sqft) {
		this.sqft = sqft;
	}

	public Integer getBedrooms() {
		return bedrooms;
	}

	public void setBedrooms(Integer bedrooms) {
		this.bedrooms = bedrooms;
	}

	public Integer getBaths() {
		return baths;
	}

	public void setBaths(Integer baths) {
		this.baths = baths;
	}

	public Integer getBathsPartial() {
		return bathsPartial;
	}

	public void setBathsPartial(Integer bathsPartial) {
		this.bathsPartial = bathsPartial;
	}

	public String getGar() {
		return gar;
	}

	public void setGar(String gar) {
		this.gar = gar;
	}

	public Integer getGarsp() {
		return garsp;
	}

	public void setGarsp(Integer garsp) {
		this.garsp = garsp;
	}

	public Date getYrblt() {
		return yrblt;
	}

	public void setYrblt(Date yrblt) {
		this.yrblt = yrblt;
	}

	public Double getAcres() {
		return acres;
	}

	public void setAcres(Double acres) {
		this.acres = acres;
	}

	public Double getLotSqft() {
		return lotSqft;
	}

	public void setLotSqft(Double lotSqft) {
		this.lotSqft = lotSqft;
	}

	public Double getHoaFee() {
		return hoaFee;
	}

	public void setHoaFee(Double hoaFee) {
		this.hoaFee = hoaFee;
	}

	public String getHoaFeesFreq() {
		return hoaFeesFreq;
	}

	public void setHoaFeesFreq(String hoaFeesFreq) {
		this.hoaFeesFreq = hoaFeesFreq;
	}

	public Date getCloseDate() {
		return closeDate;
	}

	public void setCloseDate(Date closeDate) {
		this.closeDate = closeDate;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public String getApn() {
		return apn;
	}

	public void setApn(String apn) {
		this.apn = apn;
		this.setApnClean(apn);
	}

	public String getApnClean() {
		return apnClean;
	}

	public void setApnClean(String apn) {
		this.apnClean = RealStatUtil.cleanApn(apn);
	}

	public String getCensusTract() {
		return censusTract;
	}

	public void setCensusTract(String censusTract) {
		this.censusTract = censusTract;
	}

	public String getCityTransferTax() {
		return cityTransferTax;
	}

	public void setCityTransferTax(String cityTransferTax) {
		this.cityTransferTax = cityTransferTax;
	}

	public String getPropClass() {
		return propClass;
	}

	public void setPropClass(String propClass) {
		this.propClass = propClass;
	}

	public String getCoListAgentBreNum() {
		return coListAgentBreNum;
	}

	public void setCoListAgentBreNum(String coListAgentBreNum) {
		this.coListAgentBreNum = coListAgentBreNum;
	}

	public String getCoSellAgentBreNum() {
		return coSellAgentBreNum;
	}

	public void setCoSellAgentBreNum(String coSellAgentBreNum) {
		this.coSellAgentBreNum = coSellAgentBreNum;
	}

	public String getCompToSellingOffice() {
		return compToSellingOffice;
	}

	public void setCompToSellingOffice(String compToSellingOffice) {
		this.compToSellingOffice = compToSellingOffice;
	}

	public String getCompType() {
		return compType;
	}

	public void setCompType(String compType) {
		this.compType = compType;
	}

	public String getCounty() {
		return county;
	}

	public void setCounty(String county) {
		this.county = county;
	}

	public Integer getCumulativeDomls() {
		return cumulativeDomls;
	}

	public void setCumulativeDomls(Integer cumulativeDomls) {
		this.cumulativeDomls = cumulativeDomls;
	}

	public Integer getDaysOnMls() {
		return daysOnMls;
	}

	public void setDaysOnMls(Integer daysOnMls) {
		this.daysOnMls = daysOnMls;
	}

	public Date getGeneralDate() {
		return generalDate;
	}

	public void setGeneralDate(Date generalDate) {
		this.generalDate = generalDate;
	}

	public String getDistNeighborhoodSubdiv() {
		return distNeighborhoodSubdiv;
	}

	public void setDistNeighborhoodSubdiv(String distNeighborhoodSubdiv) {
		this.distNeighborhoodSubdiv = distNeighborhoodSubdiv;
	}

	public String getDualVariable() {
		return dualVariable;
	}

	public void setDualVariable(String dualVariable) {
		this.dualVariable = dualVariable;
	}

	public Integer getFireplaces() {
		return fireplaces;
	}

	public void setFireplaces(Integer fireplaces) {
		this.fireplaces = fireplaces;
	}

	public String getFloorUnitIsOn() {
		return floorUnitIsOn;
	}

	public void setFloorUnitIsOn(String floorUnitIsOn) {
		this.floorUnitIsOn = floorUnitIsOn;
	}

	public String getForeclosureStatus() {
		return foreclosureStatus;
	}

	public void setForeclosureStatus(String foreclosureStatus) {
		this.foreclosureStatus = foreclosureStatus;
	}

	public String getHoa() {
		return hoa;
	}

	public void setHoa(String hoa) {
		this.hoa = hoa;
	}

	public String getHowSold() {
		return howSold;
	}

	public void setHowSold(String howSold) {
		this.howSold = howSold;
	}

	public Date getInputDate() {
		return inputDate;
	}

	public void setInputDate(Date inputDate) {
		this.inputDate = inputDate;
	}

	public Double getListPriceSqft() {
		return listPriceSqft;
	}

	public void setListPriceSqft(Double listPriceSqft) {
		this.listPriceSqft = listPriceSqft;
	}

	public String getListAgentBreNum() {
		return listAgentBreNum;
	}

	public void setListAgentBreNum(String listAgentBreNum) {
		this.listAgentBreNum = listAgentBreNum;
	}

	public Date getListDate() {
		return listDate;
	}

	public void setListDate(Date listDate) {
		this.listDate = listDate;
	}

	public String getListingBrokerBreNumber() {
		return listingBrokerBreNumber;
	}

	public void setListingBrokerBreNumber(String listingBrokerBreNumber) {
		this.listingBrokerBreNumber = listingBrokerBreNumber;
	}

	public String getListingType() {
		return listingType;
	}

	public void setListingType(String listingType) {
		this.listingType = listingType;
	}

	public Integer getNumberOfOffers() {
		return numberOfOffers;
	}

	public void setNumberOfOffers(Integer numberOfOffers) {
		this.numberOfOffers = numberOfOffers;
	}

	public Date getOffMarketDate() {
		return offMarketDate;
	}

	public void setOffMarketDate(Date offMarketDate) {
		this.offMarketDate = offMarketDate;
	}

	public Double getOriginalPrice() {
		return originalPrice;
	}

	public void setOriginalPrice(Double originalPrice) {
		this.originalPrice = originalPrice;
	}

	public Date getPendingDate() {
		return pendingDate;
	}

	public void setPendingDate(Date pendingDate) {
		this.pendingDate = pendingDate;
	}

	public String getPendingLitigation() {
		return pendingLitigation;
	}

	public void setPendingLitigation(String pendingLitigation) {
		this.pendingLitigation = pendingLitigation;
	}

	public Double getOccPercent() {
		return occPercent;
	}

	public void setOccPercent(Double occPercent) {
		this.occPercent = occPercent;
	}

	public String getPointOfSaleOrdinance() {
		return pointOfSaleOrdinance;
	}

	public void setPointOfSaleOrdinance(String pointOfSaleOrdinance) {
		this.pointOfSaleOrdinance = pointOfSaleOrdinance;
	}

	public String getPool() {
		return pool;
	}

	public void setPool(String pool) {
		this.pool = pool;
	}

	public Double getPreviousPrice() {
		return previousPrice;
	}

	public void setPreviousPrice(Double previousPrice) {
		this.previousPrice = previousPrice;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public Date getPriceDate() {
		return priceDate;
	}

	public void setPriceDate(Date priceDate) {
		this.priceDate = priceDate;
	}

	public Double getPriceSqft() {
		return priceSqft;
	}

	public void setPriceSqft(Double priceSqft) {
		this.priceSqft = priceSqft;
	}

	public Integer getRoomsTotal() {
		return roomsTotal;
	}

	public void setRoomsTotal(Integer roomsTotal) {
		this.roomsTotal = roomsTotal;
	}

	public Double getSalePriceSqft() {
		return salePriceSqft;
	}

	public void setSalePriceSqft(Double salePriceSqft) {
		this.salePriceSqft = salePriceSqft;
	}

	public String getSaleCoop() {
		return saleCoop;
	}

	public void setSaleCoop(String saleCoop) {
		this.saleCoop = saleCoop;
	}

	public Double getSaleLastListPrice() {
		return saleLastListPrice;
	}

	public void setSaleLastListPrice(Double saleLastListPrice) {
		this.saleLastListPrice = saleLastListPrice;
	}

	public Double getSaleOriginalPrice() {
		return saleOriginalPrice;
	}

	public void setSaleOriginalPrice(Double saleOriginalPrice) {
		this.saleOriginalPrice = saleOriginalPrice;
	}

	public String getSaleRent() {
		return saleRent;
	}

	public void setSaleRent(String saleRent) {
		this.saleRent = saleRent;
	}

	public String getSellingBrokerBreNumber() {
		return sellingBrokerBreNumber;
	}

	public void setSellingBrokerBreNumber(String sellingBrokerBreNumber) {
		this.sellingBrokerBreNumber = sellingBrokerBreNumber;
	}

	public String getSoldAgentBreNum() {
		return soldAgentBreNum;
	}

	public void setSoldAgentBreNum(String soldAgentBreNum) {
		this.soldAgentBreNum = soldAgentBreNum;
	}

	public String getTaxId() {
		return taxId;
	}

	public void setTaxId(String taxId) {
		this.taxId = taxId;
	}

	public Double getTicPercentOwnerOffered() {
		return ticPercentOwnerOffered;
	}

	public void setTicPercentOwnerOffered(Double ticPercentOwnerOffered) {
		this.ticPercentOwnerOffered = ticPercentOwnerOffered;
	}

	public Integer getUnitsInComplex() {
		return unitsInComplex;
	}

	public void setUnitsInComplex(Integer unitsInComplex) {
		this.unitsInComplex = unitsInComplex;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getSpecialInformation() {
		return specialInformation;
	}

	public void setSpecialInformation(String specialInformation) {
		this.specialInformation = specialInformation;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public Date getStatusDate() {
		return statusDate;
	}

	public void setStatusDate(Date statusDate) {
		this.statusDate = statusDate;
	}

	public String getStories() {
		return stories;
	}

	public void setStories(String stories) {
		this.stories = stories;
	}

	public String getZip() {
		return zip;
	}

	public void setZip(String zip) {
		this.zip = zip;
		setZip5();
	}

	public String getZip5() {
		return zip5;
	}

	public void setZip5() {
		if (zip != null) {
			this.zip5 = zip.substring(0, 5);
		}
	}

	public String getZoning() {
		return zoning;
	}

	public void setZoning(String zoning) {
		this.zoning = zoning;
	}

	public Integer getMlsNo() {
		return mlsNo;
	}

	public void setMlsNo(Integer mlsNo) {
		this.mlsNo = mlsNo;
	}

	public RealProperty getRealProperty() {
		return realProperty;
	}

	public void setRealProperty(RealProperty realProperty) {
		this.realProperty = realProperty;
	}

	public String getCoListAgentName() {
		return coListAgentName;
	}

	public void setCoListAgentName(String coListAgentName) {
		this.coListAgentName = coListAgentName;
	}

	public String getCoListAgentLicenseId() {
		return coListAgentLicenseId;
	}

	public void setCoListAgentLicenseId(String coListAgentLicenseId) {
		this.coListAgentLicenseId = coListAgentLicenseId;
	}

	public String getCoSellAgentName() {
		return coSellAgentName;
	}

	public void setCoSellAgentName(String coSellAgentName) {
		this.coSellAgentName = coSellAgentName;
	}

	public String getCoSellAgentLicenseId() {
		return coSellAgentLicenseId;
	}

	public void setCoSellAgentLicenseId(String coSellAgentLicenseId) {
		this.coSellAgentLicenseId = coSellAgentLicenseId;
	}

	public String getListingAgentName() {
		return listingAgentName;
	}

	public void setListingAgentName(String listingAgentName) {
		this.listingAgentName = listingAgentName;
	}

	public String getListingAgentLicenseId() {
		return listingAgentLicenseId;
	}

	public void setListingAgentLicenseId(String listingAgentLicenseId) {
		this.listingAgentLicenseId = listingAgentLicenseId;
	}

	public String getSellingAgent1Name() {
		return sellingAgent1Name;
	}

	public void setSellingAgent1Name(String sellingAgent1Name) {
		this.sellingAgent1Name = sellingAgent1Name;
	}

	public String getSellingAgent1LicenseId() {
		return sellingAgent1LicenseId;
	}

	public void setSellingAgent1LicenseId(String sellingAgent1LicenseId) {
		this.sellingAgent1LicenseId = sellingAgent1LicenseId;
	}

	public Agent getListingAgent() {
		return listingAgent;
	}

	public void setListingAgent(Agent listingAgent) {
		this.listingAgent = listingAgent;
	}

	public Agent getListingAgent2() {
		return listingAgent2;
	}

	public void setListingAgent2(Agent listingAgent2) {
		this.listingAgent2 = listingAgent2;
	}

	public Agent getSellingAgent() {
		return sellingAgent;
	}

	public void setSellingAgent(Agent sellingAgent) {
		this.sellingAgent = sellingAgent;
	}

	public Agent getSellingAgent2() {
		return sellingAgent2;
	}

	public void setSellingAgent2(Agent sellingAgent2) {
		this.sellingAgent2 = sellingAgent2;
	}
	
	public PropertyTransactionRepository getRepo() {
		if (repo == null) {
			repo =  (PropertyTransactionRepository) SpringApplicationContext.getBean("artifactRepository");
		}
		return repo;
	}

	@Override
	public void save() {
		getRepo().save(this);
		
	}

	@Override
	public void saveOrUpdate() {
		getRepo().save(this);
	}

}
