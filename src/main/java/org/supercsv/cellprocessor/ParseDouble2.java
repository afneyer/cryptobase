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
public class ParseDouble2 extends CellProcessorAdaptor {

	public ParseDouble2() {
		super();
	}

	public ParseDouble2(CellProcessor next) {
		// this constructor allows other processors to be chained after
		// ParseDouble2
		super(next);
	}

	@SuppressWarnings("unchecked")
	public Object execute(Object value, CsvContext context) {

		validateInputNotNull(value, context); // throws an Exception if the
												// input is null

		// clean the string
		String newValue = cleanStrDouble2(value.toString());

		// This parser processes optional values since the cleaning process may
		// result in null
		if (newValue == null) {
			return null;
		}

		// call standard cell parseDouble() CellProcessor
		ParseDouble parseDouble = new ParseDouble();

		Object obj;
		try {
			obj = parseDouble.execute(newValue, context);
		} catch (Exception e) {
			throw new SuperCsvCellProcessorException(String.format("Could not parse '%s' as a Double2", newValue),
					context, this);
		}
		return obj;
	}

	public String cleanStrDouble2(String inStr) {
		// remove commas
		String outStr = new String(inStr);
		outStr = outStr.replaceAll(",", "");
		outStr = outStr.replaceAll("\\$", "");
		outStr = outStr.replaceAll(" ", "");
		outStr = outStr.replaceAll("\\%", "");
		outStr = outStr.replaceAll("[A-Z]", "");
		outStr = outStr.replaceAll("\\*", "");
		if (outStr.isEmpty() || outStr.equals(".")) {
			outStr = null;
		}
		return outStr;
	}
}
