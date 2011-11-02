package utils;

import java.util.Calendar;
import java.util.ArrayList;
import java.util.Formatter;
import models.*;

public class ESEMessage
{
	/**
	 *	The only purpose of ESEMessage is to keep the view
	 *	templates as simple/slim as possible.
	 */
	class DivDay
	{
		private String head = ""; /* table header */
		private String cssc = ""; /* css class list */

		public void set_head (
			String h
		) {
			head = h;
		}
		public String get_head (
		) {
			return head;
		}
		public void set_cssc (
			String c
		) {
			cssc = c;
		}
		public String get_cssc (
		) {
			return cssc;
		}
		public void append_cssc (
			String c
		) {
			cssc = cssc+" "+c;
		}
	}

	private int[] monthdays = {
		31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };

	private String[] monthnames = {
		"January", "February", "March", "April", "May", "June",
		"July", "August", "September", "October", "November",
		"December" };

	private String[] daysofweek = {
		"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday",
		"Friday", "Saturday" };

	/**
	 *	XXX: Getter/setter for this?  Don't know.
	 */
	public int y, m, d;		/* selected date */
	public int cy, cm, cd;		/* current date */
	public int mp, mn, yp, yn;	/* previous/next month/year */
	public int cm_days;		/* number of days, current month */
	public String cm_name;		/* name, current month */
	public String[] cm_dow;		/* 1st day of week, current month */

	public DivDay[] thead;		/* header-divs */
	public DivDay[] edays;		/* day-divs */

	/**
	 *	XXX: we should exchange Date objects..
	 */
	public String date_human;

	/**
	 *	Message needed for the visual representation of the
	 *	calendar, see ESECtlCalendar.lsEvents().
	 */
	public void lsEvents (
		String year,
		String month,
		String day,
		ESECalendar ec
	) {
		int cm_dow;
		ArrayList<ESEEvent> events;

		/**
		 *	init selected date
		 */
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

		/**
		 *	init previous/next month/year
		 */
		if (m == 1) {
			mp = 12;
			yp = y-1;
			mn = m+1;
			yn = y;
		}
		else if (m == 12) {
			mp = m-1;
			yp = y;
			mn = 1;
			yn = y+1;
		}
		else {
			mp = m-1;
			yp = y;
			mn = m+1;
			yn = y;
		}

		cy = cyear();
		cm = cmonth();
		cd = cday();
		cm_days = monthdays[m-1];
		cm_name = monthnames[m-1];
		cm_dow = dayofweek(y, m, 1);	/* yes, d=1 */

		/**
		 *	XXX: we should exchange Date objects..
		 */
		date_human = fmt_human(y, m, d);

		/**
		 *	tag header-divs
		 */
		thead = new DivDay[7];
		for (int i=0; i<7; i++) {
			thead[i] = new DivDay();
			thead[i].append_cssc("thead");
			thead[i].set_head(daysofweek[(cm_dow+i)%7]);
		}

		/**
		 *	tag day-divs
		 */
		edays = new DivDay[cm_days];
		for (int i=0; i<cm_days; i++) {
			edays[i] = new DivDay();
			edays[i].append_cssc("day");
			if (i+1 == d) {
				edays[i].append_cssc("selected");
			}
			if (y == cy && m == cm && i+1 == cd) {
				edays[i].append_cssc("today");
			}
			events = ec.getListOfEventsRunningAtDay(
				fmt_human(y, m, i+1), false);
			if (events.size() > 0) {
				edays[i].append_cssc("event");
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
		return c.get(Calendar.MONTH)+1;
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

	/**
	 *	XXX: we should exchange Date objects..
	 */
	public String fmt_human (
		int y, int m, int d
	) {
		return String.format("%02d.%02d.%d %s",
			d, m, y, "12:00");
	}
}
