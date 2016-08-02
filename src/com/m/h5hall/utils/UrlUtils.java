package com.m.h5hall.utils;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class UrlUtils {

	public static String build(String baseUrl, Map<String, ?> params) {
//		String query = "";
//		Iterator<?> it = params.entrySet().iterator();
//		while (it.hasNext()) {
//			Entry<String, ?> pairs = (Entry<String, ?>) it.next();
//			String key = pairs.getKey();
//			String val = pairs.getValue() != null ? pairs.getValue().toString() : "";
//			query += URLEncoder.encode(key) + "=" + URLEncoder.encode(val);
//			if (it.hasNext()) {
//				query += "&";
//			}
//		}
//		return baseUrl + "?" + query;
		
		Iterator<?> it = params.entrySet().iterator();
		while (it.hasNext()) {
			Entry<String, ?> pairs = (Entry<String, ?>) it.next();
			String key = pairs.getKey();
			String val = pairs.getValue() != null ? pairs.getValue().toString() : "";
			
//			if (baseUrl.matches("\\{" + key + "\\}"))
				baseUrl = baseUrl.replaceFirst("\\{" + key + "\\}", val);
		}
		
		return baseUrl;
	}
}
