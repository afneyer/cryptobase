package com.afn.realstat;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.assertj.core.util.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AppParamManager extends AbstractEntityManager<AppParam> {

	public static final Logger log = LoggerFactory.getLogger("import");
	
	@Autowired
	AppParamRepository apRepo;

	public AppParamManager(AppParamRepository apRepo) {
		repo = apRepo;
	}

	public void initializeParameters () {
		
		List<AppParam> list = new ArrayList<AppParam>();
		
		// Google Map Api Parameters (used in MapLocation)
		list.add(new AppParam("maxCallsPerDay", "MAP", "2000", "Maximum number of calls to the Google Maps API per day"));
		list.add(new AppParam("maxCallsPerSecond", "MAP", "50", "Maximum number of calls to the Google Maps API per second"));
		
		if (get("callsToday","MAP") == null) {
			list.add(new AppParam("callsToday", "MAP", "0", "Number of calls made so far today"));
		}
		if (get("callsToday","MAP") == null) {
			list.add(new AppParam("lastCall", "MAP", "2017-03-01T10:00:00.000", "Date/time of the last call made"));
		}
		
		list.forEach(ap -> apRepo.saveOrUpdate(ap));
	}

	
	/**
	 * Function is private because it may return null
	 * 
	 * @param key
	 * @param type
	 * @return may be null
	 */
	private AppParam get(String key, String type) {
		AppParam p = apRepo.findOneByParamKeyAndParamType(key, type);
		return p;
	}

	public String getVal(String key, String type) {
		return get(key, type).getParamValue();
	}
	
	public Date getDateVal(String key, String type) {
		String str = get(key,type).getParamValue();
		return DateUtil.parseDatetimeWithMs(str);	
	}
	
	// TODO manage value as object or generic type
	public void setVal(String key, String type, Date val) {
		String valStr = DateUtil.formatAsDatetimeWithMs(val);
		AppParam p = get(key, type);
		p.setParamValue(valStr);
		apRepo.save(p);
	}

	public void setVal(String key, String type, String valStr) {
		AppParam p = get(key, type);
		p.setParamValue(valStr);
		apRepo.save(p);
	}
	
}
