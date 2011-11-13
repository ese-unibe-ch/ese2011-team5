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
	private ArrayList<ESECalendar> correspondingCalendars;
	private ESECalendar originsCalendar;
	private ArrayList<ESEUser> participants;
	private Date startDate;
	private Date endDate;
	private boolean isPublic;
	

	public ESEEvent(String eventName, ESECalendar correspondingCalendar,
			Date startDate, Date endDate, boolean isPublic) throws ESEException
		{

		checkDateValidity(startDate, endDate);
		if(correspondingCalendar == null)
		{
			throw new ESEException("Event is not assigned to any calendar!");
		}
		if(eventName.isEmpty())
		{
			throw new ESEException("Event name must not be empty!");
		}

		this.eventID = idCounter++;
		this.eventName = eventName;
		this.correspondingCalendars=new ArrayList<ESECalendar>();
		this.correspondingCalendars.add(correspondingCalendar);
		this.originsCalendar=correspondingCalendar;
		this.startDate = startDate;
		this.endDate = endDate;
		this.isPublic = isPublic;
		
		this.participants=new ArrayList<ESEUser>();
		this.participants.add(correspondingCalendar.getOwner());

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
		
		System.out.println(ESEConversionHelper.convertDateToString(startDate));
		System.out.println(ESEConversionHelper.convertDateToString(endDate));
		
		System.out.println(dateAsCal.get(dateAsCal.DAY_OF_MONTH));
		System.out.println(dateAsCal.get(dateAsCal.MONTH));
		System.out.println(dateAsCal.get(dateAsCal.YEAR));
		
		System.out.println(day);
		System.out.println(month);
		System.out.println(year);
		
		System.out.println(startCal.getTimeInMillis());
		System.out.println(dateAsCal.getTimeInMillis());
		System.out.println(endCal.getTimeInMillis());
		
		
		if (dateAsCal.get(dateAsCal.DAY_OF_YEAR) == startCal.get(startCal.DAY_OF_YEAR) && 
					dateAsCal.get(dateAsCal.YEAR) == startCal.get(startCal.YEAR))
				return true;
		else if (dateAsCal.get(dateAsCal.DAY_OF_YEAR) == endCal.get(endCal.DAY_OF_YEAR) && 
					dateAsCal.get(dateAsCal.YEAR) == endCal.get(endCal.YEAR))
				return true;
		else if (startCal.getTimeInMillis() < dateAsCal.getTimeInMillis()
					&& endCal.getTimeInMillis() > dateAsCal.getTimeInMillis())
				return true;
		else
			return false;
	}

	private void checkDateValidity(Date startDate, Date endDate) throws ESEException
	{
		if (startDate.after(endDate))
		{
			throw new ESEException("Invalid start and end date!");
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

	public ESECalendar getCorrespondingCalendar() {
		return this.originsCalendar;
	}
	
	public void addCorrespondingCalendar(ESECalendar calendar)
	{
		if(!this.correspondingCalendars.contains(calendar))
		{
			this.correspondingCalendars.add(calendar);
		}
	}
	
	public ESECalendar getOneCorresponding(int id)
	{
		for(ESECalendar calendar:this.correspondingCalendars)
		{
			if(calendar.getID()==id)
			{
				return calendar;
			}
		}
		return null;
	}

	public Date getStartDate()
	{
		return this.startDate;
	}

	public Date getEndDate()
	{
		return this.endDate;
	}
	
	public void copyEvent(ESECalendar correspondingCalendar)
	{
		if(!this.correspondingCalendars.contains(correspondingCalendar));
		{
				this.correspondingCalendars.add(correspondingCalendar);
		}
	
	}
	
	public void removeCopiedEvent(ESECalendar correspondingCalendar)
	{
		if(this.correspondingCalendars.contains(correspondingCalendar));
		{
				this.correspondingCalendars.remove(correspondingCalendar);
		}
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

	public void setStartDate(Date startDate) throws ESEException
	{
		checkDateValidity(startDate, this.endDate);
		this.startDate = startDate;
	}

	public void setEndDate(Date endDate) throws ESEException
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
	
	public void eventRemoveInCorrespondingCalendars() throws ESEException
	{
		for(ESECalendar calendar:this.correspondingCalendars)
		{
			calendar.removeEvent(this.eventID);
		}
		
		this.correspondingCalendars.clear();
	}
}
