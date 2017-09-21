package org.supercsv.cellprocessor;

import org.supercsv.exception.SuperCsvCellProcessorException;
import org.supercsv.util.CsvContext;

/**
 * Custom cell processor that removes commas and $signs before calling the
 * standard ParseDouble CellProcessor
 * 
 * e.g. 100,000 -> 100000 $200,000 - 200000
 */
public class ParseDate2 extends ParseDate {

	public ParseDate2(final String dateFormat) {
		super(dateFormat);
	}

	public ParseDate2(final String dateFormat, boolean next) {
		// this constructor allows other processors to be chained after
		// ParseDouble2
		super(dateFormat, next);
	}

	public Object execute(Object value, CsvContext context) {

		// validateInputNotNull(value, context); // throws an Exception if the
		// input is null

		// remove commas
		String newValue = new String(value.toString());
		if (newValue == "0") {
			newValue = null;
		}

		// call standard cell parseDouble() CellProcessor
		ParseDate parseDouble = new ParseDate(newValue);

		Object obj;
		try {
			obj = parseDouble.execute(newValue, context);
		} catch (Exception e) {
			throw new SuperCsvCellProcessorException(String.format("Could not parse '%s' as a Double2", value), context,
					this);
		}
		return obj;
	}
}
