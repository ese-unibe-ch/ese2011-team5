package utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * ESECalendarUtil builds a wrapper around the {@link java.util.Calendar}, so 
 * that the features provided by this class may be used most efficiently.
 * 
 * @author Judith Fuog
 *
 */
public class ESECalendarUtil {
	
	private static java.util.Calendar calendar;
	
	/**
	 * Private constructor. Use {@link #getInstanceToday()} to receive a valid
	 * instance.
	 */
	private ESECalendarUtil(){
	}
	
	/**
	 * Get an instance of {@link ESECalendar}. By default
	 * it points at today.<p>
	 * 
	 * @return ESECalendarUtil
	 */
	public static ESECalendarUtil getInstanceToday(){
		calendar = java.util.Calendar.getInstance();
		return new ESECalendarUtil();	
	}
	
	/**
	 * Sets the calendar at this
	 * specific date.
	 * 
	 * @param date at which the calendar should point.
	 */
	public void setAtDate(Date date){
		assert(date!=null);
		calendar.setTime(date);
	}
	
	/**
	 * Sets the calendar at this month within the
	 * same year. <br>
	 * Months are numbered starting from 1 (jan) to
	 * 12 (dec). <p>
	 * 
	 * Wrong input is not handled.
	 * 
	 * @param month at which the calendar should be set.
	 */
	public void setMonth(int month){
		assert(month >0);
		assert(month <=12);
		calendar.set(calendar.MONTH, month-1);
	}

	/**
	 * Returns the current month indicated by numbers
	 * from 1(jan) to 12 (dec).
	 * 
	 * @return month at which the calendar currently points.
	 */
	public int getMonth(){
		int month = calendar.get(calendar.MONTH);
		assert(0<=month);
		assert(month<12);
		return month+1;
	}
	
	/**
	 * Sets the calendar at this year. <br>
	 * 
	 * Wrong input is not handled.
	 * 
	 * @param year at which the calendar should point.
	 */
	public void setYear(int year){
		calendar.set(calendar.YEAR, year-1);
	}

	/**
	 * Returns the current year.
	 * 
	 * @return int year at which the calendar currently points.
	 */
	public int getYear(){
		return calendar.get(calendar.YEAR);
	}
	
	/**
	 * Sets the calendar to this date within the
	 * same month. <br>
	 * 
	 * Wrong input is not handled.
	 * 
	 * @param dayOfMonth at which the calendar should be set.
	 */
	public void setDayOfMonth(int dayOfMonth){
		assert(dayOfMonth >0);
		assert(dayOfMonth <=31);
		calendar.set(calendar.DAY_OF_MONTH, dayOfMonth);
	}

	/**
	 * Returns the current day of the month (date).
	 * 
	 * @return int day of month at which the calendar currently points.
	 */
	public int getDayOfMonth(){
		int dayOfMonth = calendar.get(calendar.DAY_OF_MONTH);
		assert(0<dayOfMonth);
		assert(dayOfMonth<=31);
		return dayOfMonth;
	}
	
	/**
	 * Returns amount of days in the current month.
	 * 
	 * @see #setAtDate(Date day)
	 * @see #setMonth(int month)
	 * 
	 * @return int days
	 */
	public int numberOfDaysOfCurrentMonth(){
		return calendar.getActualMaximum(calendar.DATE);
		//TODO tests
	}
	
	/**
	 * Returns a list starting from 1 to the last day
	 * of the current month.<br>
	 * Example: If current month is Juni, then the lists
	 * size is 30 and its contence is: <br>[1][2]...[29][30]<p>
	 * 
	 * Combinded with the methos #daysOfLastMonth() and
	 * #daysOfNextMonth a calendar view can be put 
	 * together that looks like this: <br>
	 * 
	 * Full Example: December<br>
	 * <code>
	 * [28][29][30][31][01][02][03] <br>
	 * [04][05][06][07][08][09][10] <br>
	 * ...<br>
	 * [24][25][26][27][28][01][02] <br>
	 * </code>
	 * comment: the leading '0' are only for better
	 * view and are not in the lists.
	 * 
	 * @see #daysOfLastMonth()
	 * @see #daysOfNextMonth()
	 * 
	 * @return ArrayList<Integer> with all the dates of the current month.
	 */
	public List<Integer> daysOfCurrentMonth(){
		List<Integer> currentMonth = new ArrayList<Integer>();
		int noOfDays = numberOfDaysOfCurrentMonth();
		for(int i = 1; i<= noOfDays; ++i){
			currentMonth.add(i);
		}
		return currentMonth;
		//TODO tests
	}
	/**
	 * Returns a List that contains the first few dates of 
	 * the next month that will fill up the currents months
	 * last week until Sunday. <p>
	 * Example: This month ends with a Wednesday(3) on the 31., so 
	 * the list will provide the following numbers: [1][2][3][4] <br>
	 * 
	 * @see #daysOfCurrentMonth()
	 * @see #daysOfLastMonth()
	 * 
	 * @return ArrayList<Integer> of first few days from next month
	 */
	public List<Integer> daysOfNextMonth() {
		
		int lastDay = calendar.getActualMaximum(calendar.DATE);
		System.out.println("Last day in this month: "+lastDay);
		calendar.set(calendar.DAY_OF_MONTH, lastDay);
		int lastDayOfWeek = (((calendar.get(calendar.DAY_OF_WEEK))+5)%7)+1;
		calendar.set(calendar.DAY_OF_MONTH, 1);	//setting default
		int remainingDays = 7-lastDayOfWeek;
		
		List<Integer> nextMonth = new ArrayList<Integer>();
		for(int i = 1; i<= remainingDays; i++){
			nextMonth.add((Integer)i);
		}
		assert(nextMonth.size()<7);
		return nextMonth;
		//TODO tests
	}
	
	/**
	 * Returns a List that contains the last few dates of 
	 * the previous month that will fill up the currents months
	 * first week from Monday until week day the month starts. <p>
	 * Example: This month starts with a Friday(5) and assuming the
	 * last month had 31 days, so the list will provide the 
	 * following numbers: [28][29][30][31] <br>
	 * 
	 * @see #daysOfCurrentMonth()
	 * @see #daysOfNextMonth()
	 * 
	 * @return ArrayList<Integer> of first few days from next month
	 */
	public List<Integer> daysOfLastMonth() {
		
		int firstDay = firstWeekdayOfCurrentMonth();
		calendar.set(calendar.DAY_OF_MONTH, 1);
		calendar.add(calendar.DAY_OF_MONTH, -1);

		int lastDayOflastMonth = calendar.get(calendar.DAY_OF_MONTH);	
		calendar.add(calendar.DAY_OF_WEEK, 1);
		
		List<Integer> lastMonth = new ArrayList<Integer>();
		for(int i = 1; i < firstDay; i++){
			lastMonth.add((Integer)lastDayOflastMonth);
			lastDayOflastMonth--;
		}
		Collections.reverse(lastMonth);
		assert(lastMonth.size()<7);
		return lastMonth;
		//TODO tests
	}
	
	
	/**
	 * Returns the weekday which this month starts with. <br>
	 * Weekdays are numbered from 1(monday) to 7(sunday).
	 * 
	 * @return int weekday
	 */
	public int firstMondayOfCurrentMonth(){

		Date temp = calendar.getTime();	/*save original date*/		
		calendar.set(calendar.DATE, 1);
		int weekday = calendar.get(calendar.DAY_OF_WEEK);
		int monday = ((8-weekday)%7)+2;	/*calculate date from weekday*/		
		calendar.setTime(temp); 		/*setting pointer back*/
		
		return monday;
	}
	
	/**
	 * Returns the first weekday of the current month. <br>
	 * Weekdays are numbered from 1(monday) to 7(sunday).
	 * 
	 * @return int weekday
	 */
	public int firstWeekdayOfCurrentMonth(){
		
		Date temp = calendar.getTime();	/*save original date*/
		calendar.set(calendar.DATE, 1);
		int weekday = calendar.get(calendar.DAY_OF_WEEK);	
		calendar.setTime(temp); 		/*setting pointer back*/
		weekday = ((weekday + 5)%7)+1;
		return weekday;
	}
	
	/**
	 * Sets the whole calendar to the next month.<br>
	 * All methods relating to "current" one month forth.
	 */
	public void setToNextMonth(){
		calendar.add(calendar.MONTH, 1);
	}
	
	/**
	 * Sets the whole calendar to the previous month.<br>
	 * All methods relating to "current" one month back.
	 */	
	public void setToPreviousMonth(){
		calendar.add(calendar.MONTH,-1);
	}
	/**
	 * Returns the day of month of the last Monday of the previous
	 * Month. <br>
	 * If the first weekday of the current month is a Monday, then <code>0</code>
	 * is returned. <br>
	 * 
	 * @see #daysOfLastMonth()
	 * @see #firstSundayOfNextMonth()
	 * 
	 * @return int day of month
	 */
	public int lastMondayOfPreviousMonth(){
		int weekdayCurrent = firstWeekdayOfCurrentMonth();
		calendar.add(calendar.DAY_OF_MONTH, weekdayCurrent-1); /*find out Monday*/
		int lastMonday = calendar.get(calendar.DAY_OF_MONTH);
		calendar.add(calendar.DAY_OF_MONTH, weekdayCurrent-1); /*reset to default*/
		return lastMonday;
		//TODO tests
	}
	/**
	 * Returns the day of month of the first Sunday of the next
	 * Month. <br>
	 * If the last weekday of the current month is a Sunday, then <code>0</code>
	 * is returned. <br>
	 * 
	 * @see #daysOfNextMonth()
	 * @see #lastMondayOfPreviousMonth()
	 * 
	 * @return int day of month
	 */
	public int firstSundayOfNextMonth(){
		Date temp = calendar.getTime();	/*save original date*/
		calendar.add(calendar.MONTH, 1);
		int weekday = firstWeekdayOfCurrentMonth();
		int firstSunday = (8-(weekday)%7);
		calendar.setTime(temp);			/*setting pointer back*/
		return firstSunday;
		//TODO tests
	}
	
	/**
	 * Time stamp is set to now.
	 * 
	 */
	public void reset(){
		calendar.setTime(new Date());
	}
	
	/**
	 *Returns the full name of the current
	 * month as a {@link String}.
	 *
	 *@see #monthToStringShort()
	 *
	 * @return month name
	 */
	public String monthToString(){
        Date today = calendar.getTime();
		String monthName = new SimpleDateFormat("MMMM").format(today);
		return monthName;
		//TODO tests
	}
	
	/**
	 *Returns the abbreviation name of the current
	 * month as a {@link String}.
	 *
	 *@see #monthToString()
	 *
	 * @return month name
	 */
	public String monthToStringShort(){
        Date today = calendar.getTime();
		String monthName = new SimpleDateFormat("MMM").format(today);
		return monthName;
		//TODO tests
	}
	
	/**
	 *Returns the full name of the current
	 *weekday as a {@link String}.
	 * @return
	 */
	public String weekDayToString(){
        Date today = calendar.getTime();
		String weekDayName = new SimpleDateFormat("EEEE").format(today);
		return weekDayName;
		//TODO tests
	}
	
	/**
	 *Returns the abbreviation name of the current
	 *weekday as a {@link String}.
	 * @return
	 */
	public String weekDayToStringShort(){
        Date today = calendar.getTime();
		String weekDayName = new SimpleDateFormat("EEE").format(today);
		return weekDayName;
		//TODO tests
	}
}
