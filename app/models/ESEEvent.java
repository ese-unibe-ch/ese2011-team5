/*
 * Project: ESECalendar team 5
 * Authors:
 * 		Rafael Breu
 * 		Renato Corti
 * 		Lukas Keller
 */

package models;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import models.visitor.Visitable;
import models.visitor.Visitor;

import com.google.gson.annotations.Expose;

/**
 * Class handling events in a Calendar.
 * @see ESECalendar
 * @see ESEUser
 */
public class ESEEvent implements Comparable<ESEEvent>, Visitable
{
	/**
	 * Static counter to determine uniquely eventID.<br>
	 * Increased after initialisation of an new ESEEvent.
	 */
	private static int idCounter = 0;
	/**
	 * Number to distinct different ESEEvent within one application.
	 */
	@Expose
	private int eventID;
	@Expose
	private String eventName;
	/**
	 * The {@link ESECalendar} in which the ESEEvent originally
	 * was created.
	 */
	private ESECalendar originsCalendar;
	/**
	 * List of all {@link ESECalendar} which contain this ESEEvent.
	 * The ESECalendar in which this ESEEvent was originally created is also part of this List.
	 */
	private ArrayList<ESECalendar> correspondingCalendars;
	/**
	 * List of all {@link ESEUser} that participate in this ESEEvent.
	 * The ESEUser that created this ESEEvent is also in this List.
	 */
	private ArrayList<ESEUser> participants;
	@Expose
	private Date startDate;
	@Expose
	private Date endDate;
	/**
	 * If {@link #isPublic()} is <code>true</code>, all ESEUser may see this ESEEvent.<br>
	 * If {@link #isPublic()} is <code>false</code>, only ESEUser
	 * that are <b>Contancs</b> of the {@link ESECalendar#getOwner()}
	 * of the ESEEvent can view it.
	 */
	private boolean isPublic;

	/**
	 * Constructor for ESEEvent.<br>
	 * The {@link ESECalendar} creating this ESEEvent is
	 * automatically added to the List of corresponding
	 * Calendars of this ESEEvent.<br>
	 * The {@link ESEUser} that owns the ESECalendar
	 * creating this ESEEvent is automatically added to the List of
	 * participants of this ESEEvent.
	 * @param eventName Name of Event. Must not be empty.
	 * @param correspondingCalendar Originated Calendar.
	 * @param startDate Time and Date when the Event starts.
	 * @param endDate Time and Date when the Event ends.
	 * @param isPublic Privacy of an Event.
	 * @throws ESEException if correspondingCalendar is <code>null</code>
	 * or if eventName is empty.
	 * @see ESECalendar
	 * @see ESEUser
	 */
	public ESEEvent(String eventName, ESECalendar correspondingCalendar,
			Date startDate, Date endDate, boolean isPublic) throws ESEException
	{

		checkDateValidity(startDate, endDate);
		if (correspondingCalendar == null)
		{
			throw new ESEException("Event is not assigned to any calendar!");
		}
		if (eventName.isEmpty())
		{
			throw new ESEException("Event name must not be empty!");
		}

		this.eventID = idCounter++;
		this.eventName = eventName;
		this.correspondingCalendars = new ArrayList<ESECalendar>();
		this.correspondingCalendars.add(correspondingCalendar);
		this.originsCalendar = correspondingCalendar;
		this.startDate = startDate;
		this.endDate = endDate;
		this.isPublic = isPublic;

		this.participants = new ArrayList<ESEUser>();
		this.participants.add(correspondingCalendar.getOwner());

	}

	public void accept(Visitor visitor)
	{
		visitor.visit(this);
	}

	// Usage only intended for testing purposes
	public static void resetIdCounter()
	{
		idCounter = 0;
	}

	/**
	 * Query to determine if this ESEEvent takes places on a given day.<br>
	 * @param day of month
	 * @param month 0 to 11
	 * @param year
	 * @return <code>true</code> if Event takes place on that day.<br>
	 *         <code>false</code> if Event doesn't take place on that day.
	 */
	public boolean isEventDay(int day, int month, int year)
	{
		// TODO: remove noise!
		Calendar dateAsCal = new GregorianCalendar();
		dateAsCal.set(dateAsCal.DAY_OF_MONTH, day);
		dateAsCal.set(dateAsCal.MONTH, month);
		dateAsCal.set(dateAsCal.YEAR, year);

		Calendar startCal = new GregorianCalendar();
		Calendar endCal = new GregorianCalendar();

		startCal.setTime(this.startDate);
		endCal.setTime(this.endDate);

		if (dateAsCal.get(dateAsCal.DAY_OF_YEAR) == startCal.get(startCal.DAY_OF_YEAR)
				&& dateAsCal.get(dateAsCal.YEAR) == startCal.get(startCal.YEAR))
			return true;
		else if (dateAsCal.get(dateAsCal.DAY_OF_YEAR) == endCal.get(endCal.DAY_OF_YEAR)
				&& dateAsCal.get(dateAsCal.YEAR) == endCal.get(endCal.YEAR))
			return true;
		else if (startCal.getTimeInMillis() < dateAsCal.getTimeInMillis()
				&& endCal.getTimeInMillis() > dateAsCal.getTimeInMillis())
			return true;
		else
			return false;
	}

	/**
	 * @param startDate of ESEEvent.
	 * @param endDate of ESEEvent.
	 * @throws ESEException if endDate is before startDate.
	 */
	private void checkDateValidity(Date startDate, Date endDate) throws ESEException
	{
		if (startDate.after(endDate))
		{
			throw new ESEException("Invalid start and end date!");
		}
	}

	/**
	 * @return int {@link #eventID} of this ESEEvent. The value is ranged
	 *         between 0 and the current value of {@link #idCounter}.
	 */
	public int getEventID()
	{
		return this.eventID;
	}

	/**
	 * @return String name of this ESEEvent.
	 */
	public String getEventName()
	{
		return this.eventName;
	}

	/**
	 * @return originsCalendar<br>
	 * ESECalendar that originally created this ESEEvent.
	 */
	public ESECalendar getCorrespondingCalendar()
	{
		return this.originsCalendar;
	}

	/**
	 * Adds this ESEEvent to another {@link ESECalendar}.
	 * @param calendar that this ESEEvent should be added to.
	 */
	public void addCorrespondingCalendar(ESECalendar calendar)
	{
		if (!this.correspondingCalendars.contains(calendar))
		{
			this.correspondingCalendars.add(calendar);
		}
	}

	/**
	 * Returns a ESECalendar specified by it's Id that contains
	 * this ESEEvent.
	 * @param id of searched Calendar.
	 * @return correspondingCalendar
	 * @throws ESEException if there is no ESECalendar with this id.
	 */
	public ESECalendar getOneCorresponding(int id) throws ESEException
	{
		for (ESECalendar calendar : this.correspondingCalendars)
		{
			if (calendar.getID() == id)
			{
				return calendar;
			}
		}
		throw new ESEException("No calendar with ID \"" + id + "\"!");
	}

	/**
	 * @return Date specifying the time, when the ESEEvent starts.
	 */
	public Date getStartDate()
	{
		return this.startDate;
	}

	/**
	 * @return Date specifying the time, when the ESEEvent ends.
	 */
	public Date getEndDate()
	{
		return this.endDate;
	}

	/**
	 * This method allows an existing ESEEvent to be copied to another
	 * ESECalendar than the originated ESECalendar.<br>
	 * However, it is only within the originated ESECalendar possible,
	 * to edit an ESEEvent.
	 * @param correspondingCalendar to which this ESEEvent should be copied.
	 * @see #removeCopiedEvent(ESECalendar)
	 * @see ESECalendar
	 */
	public void copyEvent(ESECalendar correspondingCalendar)
	{
		if (!this.correspondingCalendars.contains(correspondingCalendar))
		{
			this.correspondingCalendars.add(correspondingCalendar);
		
		}

	}

	/**
	 * Removes an ESEEvent, that is not originated from the corresponding Calendar.
	 * @param correspondingCalendar 
	 * @see #copyEvent(ESECalendar)
	 */
	public void removeCopiedEvent(ESECalendar correspondingCalendar)
	{
		if (this.correspondingCalendars.contains(correspondingCalendar))
		{
			this.correspondingCalendars.remove(correspondingCalendar);
			
		}
	}

	/**
	 * @return String representation of endDate in the following format: "dd.MM.yyyy HH:mm"
	 */
	public String getStringEndDate()
	{
		return ESEConversionHelper.convertDateToString(this.endDate);
	}

	/**
	 * @return String representation of startDate in the following format: "dd.MM.yyyy HH:mm"
	 */
	public String getStringStartDate()
	{
		return ESEConversionHelper.convertDateToString(this.startDate);
	}

	/**
	 * @return boolean Privacy of this ESEEvent.
	 */
	public boolean isPublic()
	{
		return this.isPublic;
	}

	/**
	 * Set the {@link #eventName} to a new value.<br>
	 * @param eventName to be set.
	 * @throws ESEException 
	 */
	public void setEventName(String eventName) throws ESEException
	{
		if (eventName.isEmpty())
		{
			throw new ESEException("Event name must not be empty!");
		}
		this.eventName = eventName;
	}

	// Setting start date and end date should be atomic
	public void setStartDateAndEndDate(Date startDate, Date endDate) throws ESEException
	{
		checkDateValidity(startDate, endDate);
		this.checkForOverlapping(this.originsCalendar.getAllEvents());
		this.startDate = startDate;
		this.endDate = endDate;
	}

	/**
	 * Sets the {@link #startDate} to a new value.<br>
	 * @param startDate time when ESEEvent starts.
	 * @throws ESEException if startDate is after endDate
	 */
	public void setStartDate(Date startDate) throws ESEException
	{
		checkDateValidity(startDate, this.endDate);
		this.checkForOverlapping(this.originsCalendar.getAllEvents());
		this.startDate = startDate;
	}

	/**
	 * Sets the {@link #endDate} to a new value.<br>
	 * @param endDate time when ESEEvent ends.
	 * @throws ESEException if startDate is after endDate
	 */
	public void setEndDate(Date endDate) throws ESEException
	{
		checkDateValidity(this.startDate, endDate);
		this.checkForOverlapping(this.originsCalendar.getAllEvents());
		this.endDate = endDate;
	}

	/**
	 * For publiclyViewable is <code>true</code> the Event is set to public.<br>
	 * For publiclyViewable is <code>false</code> the Event is set to private.
	 * @param publiclyViewable Boolean value to set the visibility of the event.
	 */
	public void setVisibility(boolean publiclyViewable)
	{
		this.isPublic = publiclyViewable;
	}

	public Boolean checkForOverlapping(ArrayList<ESEEvent> eventList)
	{
		for (ESEEvent existingEvent : eventList)
		{
			if (!this.equals(existingEvent) && checkEventOverlaps(existingEvent, this))
			{
				return true;
			}
		}
		return false;
	}

	private boolean checkEventOverlaps(ESEEvent existingEvent, ESEEvent newEvent)
	{
		boolean startDateOverlaps = startDateLiesInBetweenExistingEvent(existingEvent, newEvent);
		boolean endDateOverlaps = endDateLiesInBetweenExistingEvent(existingEvent, newEvent);
		boolean embracesEvent = eventIsSubsetOfExistingEvent(existingEvent, newEvent);
		boolean isInEvent = eventContainsExistingEvent(existingEvent, newEvent);

		return startDateOverlaps || endDateOverlaps || embracesEvent || isInEvent;
	}

	private boolean startDateLiesInBetweenExistingEvent(ESEEvent existingEvent, ESEEvent newEvent)
	{
		long existingStartTime = existingEvent.getStartDate().getTime();
		long newStartTime = newEvent.getStartDate().getTime();
		long existingEndTime = existingEvent.getEndDate().getTime();

		return existingStartTime <= newStartTime && newStartTime <= existingEndTime;
	}

	private boolean endDateLiesInBetweenExistingEvent(ESEEvent existingEvent, ESEEvent newEvent)
	{
		long existingStartTime = existingEvent.getStartDate().getTime();
		long newEndTime = newEvent.getEndDate().getTime();
		long existingEndTime = existingEvent.getEndDate().getTime();

		return existingStartTime <= newEndTime && newEndTime <= existingEndTime;
	}

	private boolean eventIsSubsetOfExistingEvent(ESEEvent existingEvent, ESEEvent newEvent)
	{
		long existingStartTime = existingEvent.getStartDate().getTime();
		long newStartTime = newEvent.getStartDate().getTime();
		long newEndTime = newEvent.getEndDate().getTime();
		long existingEndTime = existingEvent.getEndDate().getTime();

		return existingStartTime <= newStartTime && newEndTime <= existingEndTime;
	}

	private boolean eventContainsExistingEvent(ESEEvent existingEvent, ESEEvent newEvent)
	{
		long newStartTime = newEvent.getStartDate().getTime();
		long existingStartTime = existingEvent.getStartDate().getTime();
		long existingEndTime = existingEvent.getEndDate().getTime();
		long newEndTime = newEvent.getEndDate().getTime();

		return newStartTime <= existingStartTime && existingEndTime <= newEndTime;
	}

	/**
	 * @return String representation of this ESEEvent.<br>
	 *         Example. "Meeting: From 12.09.2011 13:00 to 12.09.2011 15:00"
	 */
	public String toString()
	{
		return this.eventName;
	}
	
	public String getStartDateString(){
		return ESEConversionHelper.convertDateToString(this.startDate).substring(0,6) 
				+ ESEConversionHelper.convertDateToString(this.startDate).substring(10,16);
	}
	
	public String getEndDateString(){
		return ESEConversionHelper.convertDateToString(this.endDate).substring(0,6) 
				+ ESEConversionHelper.convertDateToString(this.endDate).substring(10,16);
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

	/**
	 * Removes an ESEEvent from all ESECalendars, including its originated ESECalendar.
	 * In contrast, {@link #removeCopiedEvent(ESECalendar)} removes an event from one calendar.
	 * @throws ESEException
	 * @see #removeCopiedEvent(ESECalendar)
	 */
	public void eventRemoveInCorrespondingCalendars() throws ESEException
	{
		for (ESECalendar calendar : this.correspondingCalendars)
		{
			calendar.removeEvent(this.eventID);
		}

		this.correspondingCalendars.clear();
	}
	
	/**
	 * Get all participants of the event
	 * @return an arrayList of ESEUsers
	 */
	public ArrayList<ESEUser> getParticipants()
	{
		return new ArrayList<ESEUser>(this.participants);
	}
	
	public void addParticipant(ESEUser participant)
	{
		this.participants.add(participant);
	}
	
	public void removeParticipant(ESEUser participant)
	{
		if(this.participants.contains(participant))
		{
			this.participants.remove(participant);
		}
		
	}
}
