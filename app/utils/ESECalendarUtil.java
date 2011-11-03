package utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * ESECalendarUtil builds a wrapper around the <code> java.calendar</code>, so 
 * that the features provided by <code>java.calendar</code> may be used most 
 * efficiently.
 * 
 * @author lyriael
 *
 */
public class ESECalendarUtil {
	
	private static java.util.Calendar cal;
	
	/**
	 * Private constructor. Use {@link #getInstanceToday()} to receive a valid
	 * instance.
	 */
	private ESECalendarUtil(){
	}
	
	/**
	 * Get an instance of <code>ESECalendarUtil</code>. By default
	 * it points at today.<p>
	 * {@link #getInstanceOfDate(Date date)} to set it with a <code>
	 * Date</code> object.<br>
	 * {@link #getInstanceOfStringDate(String date)} to set it with
	 * a <code>String</code> that is then converted to an <code>Date</code>
	 * object.
	 * 
	 * @return ESECalendarUtil
	 */
	public static ESECalendarUtil getInstanceToday(){
		cal = java.util.Calendar.getInstance();
		return new ESECalendarUtil();	
	}
	
	/**
	 * Sets the calendar at this
	 * specific date.
	 * 
	 * @param day
	 */
	public void setAtDate(Date date){
		cal.setTime(date);
	}
	
	/**
	 * Sets the calendar at this month within the
	 * same year. <br>
	 * Months are numbered starting from 1 (jan) to
	 * 12 (dec). <p>
	 * 
	 * Wrong input is not handled.
	 * 
	 * @param int month
	 */
	public void setMonth(int month){
		assert(month >0);
		assert(month <=12);
		cal.set(cal.MONTH, month-1);
	}

	/**
	 * Returns the current month indicated by numbers
	 * from 1(jan) to 12 (dec).
	 * 
	 * @return int month
	 */
	public int getMonth(){
		int month = cal.get(cal.MONTH);
		assert(0<=month);
		assert(month<12);
		return month+1;
	}
	
	/**
	 * Sets the calendar at this month within the
	 * same year. <br>
	 * Months are numbered starting from 1 (jan) to
	 * 12 (dec). <p>
	 * 
	 * Wrong input is not handled.
	 * 
	 * @param int month
	 */
	public void setYear(int year){
		cal.set(cal.YEAR, year-1);
	}

	/**
	 * Returns the current year.
	 * 
	 * @return int year
	 */
	public int getYear(){
		return cal.get(cal.YEAR);
	}
	
	/**
	 * Sets the calendar at this date within the
	 * same month. <br>
	 * 
	 * Wrong input is not handled.
	 * 
	 * @param int month
	 */
	public void setDayOfMonth(int dayOfMonth){
		assert(dayOfMonth >0);
		assert(dayOfMonth <=31);
		cal.set(cal.DAY_OF_MONTH, dayOfMonth);
	}

	/**
	 * Returns the current day of the month (date).
	 * 
	 * @return int date
	 */
	public int getDayOfMonth(){
		int dayOfMonth = cal.get(cal.DAY_OF_MONTH);
		assert(0<dayOfMonth);
		assert(dayOfMonth<=31);
		return dayOfMonth;
	}
	
	/**
	 * Returns amount of days in the current month.
	 * 
	 * @see setAtDate(Date day)
	 * @see setMonth(int month)
	 * 
	 * @return int days
	 */
	public int numberOfDaysOfCurrentMonth(){
		return cal.getActualMaximum(cal.DATE);
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
	 * @return ArrayList<Integer> Days
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
		
		int lastDay = cal.getActualMaximum(cal.DATE);
		System.out.println("Last day in this month: "+lastDay);
		cal.set(cal.DAY_OF_MONTH, lastDay);
		int lastDayOfWeek = (((cal.get(cal.DAY_OF_WEEK))+5)%7)+1;
		cal.set(cal.DAY_OF_MONTH, 1);	//setting default
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
		cal.set(cal.DAY_OF_MONTH, 1);
		cal.add(cal.DAY_OF_MONTH, -1);

		int lastDayOflastMonth = cal.get(cal.DAY_OF_MONTH);	
		cal.add(cal.DAY_OF_WEEK, 1);
		
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

		Date temp = cal.getTime(); //save original date		
		cal.set(cal.DATE, 1);
		int weekday = cal.get(cal.DAY_OF_WEEK);
		int monday = ((8-weekday)%7)+2;//calculate date from weekday		
		cal.setTime(temp); //setting pointer back
		
		return monday;
	}
	
	/**
	 * Returns the first weekday of the current month. <br>
	 * Weekdays are numbered from 1(monday) to 7(sunday).
	 * 
	 * @return int weekday
	 */
	public int firstWeekdayOfCurrentMonth(){
		
		Date temp = cal.getTime(); //save original date
		cal.set(cal.DATE, 1);
		int weekday = cal.get(cal.DAY_OF_WEEK);	
		cal.setTime(temp); //setting pointer back
		weekday = ((weekday + 5)%7)+1;
		return weekday;
	}
	
	/**
	 * Sets the whole calendar to the next month.<br>
	 * All methods relating to "current" one month forth.
	 */
	public void setToNextMonth(){
		cal.add(cal.MONTH, 1);
	}
	
	/**
	 * Sets the whole calendar to the previous month.<br>
	 * All methods relating to "current" one month back.
	 */	
	public void setToPreviousMonth(){
		cal.add(cal.MONTH,-1);
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
		cal.add(cal.DAY_OF_MONTH, weekdayCurrent-1); //find out Monday
		int lastMonday = cal.get(cal.DAY_OF_MONTH);
		cal.add(cal.DAY_OF_MONTH, weekdayCurrent-1); //reset cal to default
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
		Date temp = cal.getTime();
		cal.add(cal.MONTH, 1);
		int weekday = firstWeekdayOfCurrentMonth();
		int firstSunday = (8-(weekday)%7);
		cal.setTime(temp);//set back 
		return firstSunday;
		//TODO tests
	}
	
	/**
	 * Time stamp of util is put back to the current time.
	 * 
	 */
	public void reset(){
		cal.setTime(new Date());
	}
	
	/**
	 *TODO 
	 * @return
	 */
	public String monthToString(){
		//TODO
		return "";
	}
	/**
	 *TODO
	 * @return
	 */
	public String weekDayToString(){
		//TODO
		return "";
	}
}
