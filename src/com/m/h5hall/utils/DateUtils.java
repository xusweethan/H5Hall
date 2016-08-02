package com.m.h5hall.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.text.TextUtils;

/**
 * 时间类
 * @author yangsc
 */
public class DateUtils {

	private static final SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd");
	private static final SimpleDateFormat SDF2 = new SimpleDateFormat("MM月dd日");
	private static final SimpleDateFormat SDTF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private static final SimpleDateFormat SDTF2 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	private static final SimpleDateFormat SDTF3 = new SimpleDateFormat("HH:mm");
	
	public static String getTime(String dateTime) {
		if (!TextUtils.isEmpty(dateTime)) {
			String[] times = dateTime.split(" ");
			if (times.length >= 2) {
				times = times[1].split(":");
				if (times.length >= 2) {
					return times[0] + ":" + times[1];
				}
			}
		}
		return "";
	}

	public static String formatDate(Date date) {
		return SDF.format(date);
	}
	
	public static String formatDate2(Date date) {
		return SDF2.format(date);
	}

	public static String formatDateTime(Date date) {
		return SDTF.format(date);
	}

	public static String formatDateTime2(Date date) {
		return SDTF2.format(date);
	}
	
	public static String formatDateTime3(Date date) {
		return SDTF3.format(date);
	}
	
	public static boolean isCloseEnough(long date1, long date2) {
		long date = date1 - date2;
		if (date / (60 * 1000) > 5) {
			return true;
		}
		return false;
	}

//	public static int getWeekNameRes(Calendar c) {
//		switch (c.get(Calendar.DAY_OF_WEEK)) {
//		case Calendar.SUNDAY:
//			return (R.string.comm_sun);
//		case Calendar.MONDAY:
//			return (R.string.comm_mon);
//		case Calendar.TUESDAY:
//			return (R.string.comm_tues);
//		case Calendar.WEDNESDAY:
//			return (R.string.comm_wednes);
//		case Calendar.THURSDAY:
//			return (R.string.comm_thurs);
//		case Calendar.FRIDAY:
//			return (R.string.comm_fri);
//		case Calendar.SATURDAY:
//			return (R.string.comm_satur);
//		}
//		return 0;
//	}

}
