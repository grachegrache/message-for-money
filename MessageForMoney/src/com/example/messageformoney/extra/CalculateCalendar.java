package com.example.messageformoney.extra;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import android.annotation.SuppressLint;
import com.ibm.icu.util.ChineseCalendar;

public class CalculateCalendar {
	private ChineseCalendar chinaCal;
	private String[] solarArr;
	private String[] lunarArr;

	public CalculateCalendar() {
		chinaCal = new ChineseCalendar();
		solarArr = new String[] { "0101", "0301", "0505", "0606", "0815","1003","1009","1225" };
		lunarArr = new String[] { "0101", "0408", "0814", "0815","0816" };

	}

	/**
	 * 
	 * 해당일자가 음력 법정공휴일에 해당하는 지 확인
	 * 
	 * @param date yyyyMMdd
	 * 
	 * @return
	 */

	public boolean isHolidayLunar(Calendar cal) {
		chinaCal.setTimeInMillis(cal.getTimeInMillis());

		/** 음력으로 변환된 월과 일자 **/
		int mm = chinaCal.get(ChineseCalendar.MONTH) + 1;
		int dd = chinaCal.get(ChineseCalendar.DAY_OF_MONTH);

		StringBuilder sb = new StringBuilder();
		
		if (mm < 10)
			sb.append("0");
		sb.append(mm);
		if (dd < 10)
			sb.append("0");
		sb.append(dd);

		/** 음력 12월의 마지막날 (설날 첫번째 휴일)인지 확인 **/
		if (mm == 12) {
			int lastDate = chinaCal.getActualMaximum(ChineseCalendar.DAY_OF_MONTH);

			if(dd == lastDate)
				return true;
		}

		for (String d : lunarArr)
			if (sb.toString().equals(d))
				return true;

		return false;
	}

	/**
	 * 
	 * 해당일자가 양력 법정공휴일에 해당하는 지 확인
	 * 
	 * @param date yyyyMMdd
	 * 
	 * @return
	 */
	@SuppressLint("SimpleDateFormat")
	public boolean isHolidaySolar(Calendar cal) {
		String date = new SimpleDateFormat("MMdd").format(cal.getTime());

		for (String d : solarArr)
			if (date.equals(d))
				return true;

		return false;
	}

	/**
	 * 
	 * 해당일자가 해당 요일인지 확인
	 * 
	 * @param date
	 * @param weekday Calendar.xxxday
	 * @return
	 */

	public boolean isWeekday(Calendar cal, int weekday) {
		if (cal.get(Calendar.DAY_OF_WEEK) == weekday)
			return true;

		return false;
	}

}
