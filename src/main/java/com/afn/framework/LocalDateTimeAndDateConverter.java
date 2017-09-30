package com.afn.framework;

import java.time.LocalDateTime;
import java.util.Date;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import com.afn.realstat.AfnDateUtil;

@Converter(autoApply = true)
public class LocalDateTimeAndDateConverter implements AttributeConverter<LocalDateTime, Date> {
	
	    @Override
	    public Date convertToDatabaseColumn(LocalDateTime locDateTime) {
	    	return (locDateTime == null ? null : AfnDateUtil.asDate(locDateTime));
	    }

	    @Override
	    public LocalDateTime convertToEntityAttribute(Date date) {
	    	return (date == null ? null : AfnDateUtil.asLocalDateTime(date));
	    }

}
