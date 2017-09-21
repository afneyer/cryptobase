package org.supercsv.cellprocessor;

import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.exception.SuperCsvCellProcessorException;
import org.supercsv.util.CsvContext;

/**
 * Custom cell processor that removes commas and $signs before calling the
 * standard ParseDouble CellProcessor
 * 
 * e.g. 100,000 -> 100000 $200,000 - 200000
 */
public class ParseInt2 extends CellProcessorAdaptor {

	public ParseInt2() {
		super();
	}

	public ParseInt2(CellProcessor next) {
		// this constructor allows other processors to be chained after
		// ParseDouble2
		super(next);
	}

	@SuppressWarnings("unchecked")
	public Object execute(Object value, CsvContext context) {

		validateInputNotNull(value, context); // throws an Exception if the
												// input is null

		// remove commas
		String newValue = new String(value.toString());
		newValue = newValue.replaceAll(",", "");
		newValue = newValue.replaceAll("\\$", "");
		newValue = newValue.replaceAll(" ", "");
		newValue = newValue.replaceAll("\\%", "");

		// call standard cell parseDouble() CellProcessor
		ParseInt parseInt = new ParseInt();

		Object obj;
		try {
			obj = parseInt.execute(newValue, context);
		} catch (Exception e) {
			throw new SuperCsvCellProcessorException(String.format("Could not parse '%s' as a Int2", value), context,
					this);
		}
		return obj;
	}
}
