package models;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class ESEEvent implements Comparable<ESEEvent>
{

	private static int idCounter = 0;

	private int eventID;
	private String eventName;
	private ESECalendar correspondingCalendar;
	private Date startDate;
	private Date endDate;
	private boolean isPublic;
	

	public ESEEvent(String eventName, ESECalendar correspondingCalendar,
			Date startDate, Date endDate, boolean isPublic) throws AssertionError, IllegalArgumentException {

		// This would prevent creation of events with zero duration
		//assert(startDate.before(endDate));
		assert(!startDate.after(endDate));
		checkDateValidity(startDate, endDate);
		assert (correspondingCalendar != null);
		assert (eventName != "");

		this.eventID = idCounter++;
		this.eventName = eventName;
		this.correspondingCalendar = correspondingCalendar;
		this.startDate = startDate;
		this.endDate = endDate;
		this.isPublic = isPublic;

	}
	
	public boolean isEventDay(int day, int month, int year)
	{
		Calendar dateAsCal = new GregorianCalendar();
		dateAsCal.set(dateAsCal.DAY_OF_MONTH, day);
		dateAsCal.set(dateAsCal.MONTH, month);
		dateAsCal.set(dateAsCal.YEAR, year);
		
		Calendar startCal = new GregorianCalendar();
		Calendar endCal = new GregorianCalendar();
		
		startCal.setTime(startDate);
		endCal.setTime(endDate);
		
		if (dateAsCal.get(dateAsCal.DAY_OF_YEAR) == startCal.get(startCal.DAY_OF_YEAR) && 
					dateAsCal.get(dateAsCal.YEAR) == startCal.get(startCal.YEAR))
				return true;
		else if (dateAsCal.get(dateAsCal.DAY_OF_YEAR) == endCal.get(endCal.DAY_OF_YEAR) && 
					dateAsCal.get(dateAsCal.YEAR) == endCal.get(endCal.YEAR))
				return true;
		else if (startCal.get(startCal.MILLISECOND) < dateAsCal.get(dateAsCal.MILLISECOND)
					&& endCal.get(endCal.MILLISECOND) > dateAsCal.get(dateAsCal.MILLISECOND))
				return true;
		else
			return false;
	}

	private void checkDateValidity(Date startDate, Date endDate) throws IllegalArgumentException
	{
		if (startDate.after(endDate))
		{
			throw new IllegalArgumentException("Invalid start and end date");
		}
	}

	/*
	 * Methods with read only access
	 */

	public int getEventID()
	{
		return this.eventID;
	}

	public String getEventName()
	{
		return this.eventName;
	}

	public ESECalendar getCorrespondingCalendar()
	{
		return this.correspondingCalendar;
	}

	public Date getStartDate()
	{
		return this.startDate;
	}

	public Date getEndDate()
	{
		return this.endDate;
	}

	public String getStringEndDate()
	{
		return ESEConversionHelper.convertDateToString(this.endDate);
	}


	public String getStringStartDate()
	{
		return ESEConversionHelper.convertDateToString(this.startDate);
	}

	public boolean isPublic()
	{
		return this.isPublic;
	}

	/*
	 * Methods with read-write access
	 */

	public void setEventName(String eventName)
	{
		this.eventName = eventName;
	}

	public void setStartDate(Date startDate) throws IllegalArgumentException
	{
		checkDateValidity(startDate, this.endDate);
		this.startDate = startDate;
	}

	public void setEndDate(Date endDate) throws IllegalArgumentException
	{
		checkDateValidity(this.startDate, endDate);
		this.endDate = endDate;
	}

	public void setVisibility(boolean publiclyViewable)
	{
		this.isPublic = publiclyViewable;
	}
	
	public String toString(){
		return eventName+": From "+ESEConversionHelper.convertDateToString(startDate)
				+" to "+ESEConversionHelper.convertDateToString(endDate);
	}

	@Override
	public int compareTo(ESEEvent compareEvent)
	{
		if (this.getStartDate().getTime() > compareEvent.getStartDate().getTime())
			return 1;
		if (this.getStartDate().getTime() == compareEvent.getStartDate().getTime())
			return 0;
		else
			return -1;
	}
}
