package controllers;

import java.util.Calendar;

public class ESEMonth
{
	public int[] monthdays = {
		31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };

	public String[] monthnames = {
		"January", "February", "March", "April", "May", "June",
		"July", "August", "September", "October", "November",
		"December" };

	public String[] daysofweek = {
		"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday",
		"Friday", "Saturday" };

	public int y, m, d, cy, cm, cd, days;
	public String mname;
	public String[] dow;

	public ESEDay[] thead;
	public ESEDay[] edays;

	public ESEMonth (
		String year,
		String month,
		String day
	) {
		int dow;

		if (year == null || month == null || day == null) {
			y = cyear();
			m = cmonth();
			d = cday();
		}
		else {
			y = Integer.parseInt(year);
			m = Integer.parseInt(month);
			d = Integer.parseInt(day);
		}
		cy = cyear();
		cm = cmonth();
		cd = cday();
		days = monthdays[m-1];
		mname = monthnames[m-1];
		dow = dayofweek(y, m, 1);

		thead = new ESEDay[7];
		for (int i=0; i<7; i++) {
			thead[i] = new ESEDay();
			thead[i].append_cssc("thead");
			thead[i].set_head(daysofweek[(dow+i)%7]);
		}

		edays = new ESEDay[days];
		for (int i=0; i<days; i++) {
			edays[i] = new ESEDay();
			edays[i].append_cssc("day");
			if (i+1 == d) {
				edays[i].append_cssc("selected");
			}
			if (y == cy && m == cm && i+1 == cd) {
				edays[i].append_cssc("today");
			}
		}
	}

	public int cyear (
	) {
		Calendar c = Calendar.getInstance();
		return c.get(Calendar.YEAR);
	}

	public int cmonth (
	) {
		Calendar c = Calendar.getInstance();
		return c.get(Calendar.MONTH);
	}

	public int cday (
	) {
		Calendar c = Calendar.getInstance();
		return c.get(Calendar.DAY_OF_MONTH);
	}

	/**
	 *	derived from http://users.aol.com/s6sj7gt/mikecal.htm
	 *	output: 0 (sun) -> 6 (sat)
	 */
	public int dayofweek (
		int y, int m, int d
	) {
		int z = 0;
		int adj = 0;

		if (m < 3) {
			z = y-1;
		}
		else {
			z = y;
			adj = 2;
		}
		return ((int)(23*m/9)+d+4+y+(int)(z/4)-(int)(z/100)+(int)(z/400)-adj)%7;
	}
}
