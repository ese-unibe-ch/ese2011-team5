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
import java.util.List;

import models.visitor.Visitable;
import models.visitor.Visitor;

/**
 * ESECalendar organises the ESEEvents of an ESEUser.
 * It is also responsible for the correct interpretation of
 * time and Date representation within the application.
 * @see ESEUser
 * @see ESEEvent
 */
public class ESECalendar implements Visitable
{
	private static int idCounter = 0;

	private int calendarID;
	private String calendarName;
	private ESEUser owner;
	private ArrayList<ESEEvent> eventList;

	/**
	 * Constructor for ESECalendar.
	 * @param calendarName Must not be empty.
	 * @param owner Must not be null.
	 * @throws ESEException Either owner or calendarName is not given or null.
	 */
	public ESECalendar(String calendarName, ESEUser owner) throws ESEException
	{
		checkCalendarName(calendarName, owner);
		this.calendarID = idCounter++;
		this.eventList = new ArrayList<ESEEvent>();
		this.calendarName = calendarName;
		this.owner = owner;
	}

	public void accept(Visitor visitor)
	{
		for (ESEEvent event : this.eventList)
		{
			event.accept(visitor);
		}
		visitor.visit(this);
	}

	// Usage only intended for testing purposes
	public static void resetIdCounter()
	{
		idCounter = 0;
	}

	/**
	 * @return {@link Integer} {@code calendarID}. The value ranges from 0 to {@code idCounter}.
	 */
	public int getID()
	{
		return this.calendarID;
	}

	/**
	 * @return String name of this ESECalendar.
	 */
	public String getCalendarName()
	{
		return this.calendarName;
	}

	/**
	 * @return {@link ESEUser} owner of this ESECalendar.
	 */
	public ESEUser getOwner()
	{
		return this.owner;
	}

	/**
	 * @param id of the searched {@link ESEEvent}.
	 * @return searched ESEEvent.
	 * @throws ESEException if there is no such ESEEvent in this Calendar.
	 */
	public ESEEvent getEventByID(int id) throws ESEException
	{
		for (ESEEvent e : this.eventList)
		{
			if (e.getEventID() == id)
			{
				return e;
			}
		}
		throw new ESEException("No event with ID \"" + id + "\" in calendar \"" + this.getCalendarName() + "\"!");
	}

	/**
	 * Adds a new ESEEvent to this ESECalendar.
	 * @param eventName of the new ESEEvent.
	 * @param startDate of the new ESEEvent.
	 * @param endDate of the new ESEEvent.
	 * @param isPublic shall all ESEUser be able to see this ESEEvent.
	 * @throws ESEException if this new ESEEvent overlaps with an existing ESEEvent
	 * in the same ESECalendar. Nevertheless the event is added to the calendar
	 * just before the ESEException is thrown.
	 * @see ESEUser
	 * @see ESECalendar
	 */
	public void addEvent(String eventName, String startDate, String endDate, boolean isPublic) throws ESEException
	{
		Boolean eventOverlaps = false;
		ESEEvent newEvent = new ESEEvent(eventName, this,
				ESEConversionHelper.convertStringToDate(startDate),
				ESEConversionHelper.convertStringToDate(endDate), isPublic);
		eventOverlaps = newEvent.checkForOverlapping(this.eventList);
		this.eventList.add(newEvent);
		if (eventOverlaps)
		{
			throw new ESEException("New event \"" + newEvent.getEventName() + "\" overlaps with existing event!");
		}
	}

	/**
	 * Creates a new {@link ESEEvent} and adds it to this {@link ESECalendar}.
	 * @param eventName Description of the event.
	 * @param startDate Start of event.
	 * @param endDate End of event.
	 * @param isPublic Visibility for other users.
	 * @throws ESEException The event that should be added overlaps with another event
	 * in this calendar, but the event is added though.
	 */
	public void addEvent(String eventName, Date startDate, Date endDate,
			boolean isPublic) throws ESEException
	{
		this.addEvent(eventName, ESEConversionHelper.convertDateToString(startDate),
				ESEConversionHelper.convertDateToString(endDate), isPublic);
	}

	/**
	 * @param event Existing {@link ESEEvent} that is added to this ESECalendar.
	 */
	public void addEvent(ESEEvent event)
	{
		if (!this.eventList.contains(event))
		{
			this.eventList.add(event);
		}
	}

	/**
	 * Removes an {@link ESEEvent} from this ESECalendar by its ID.
	 * @param eventID of ESEEvent to remove.
	 * @throws ESEException An event with the given ID is not in this calendar.
	 */
	public void removeEvent(int eventID) throws ESEException
	{
		ESEEvent eventToRemove = this.getEventByID(eventID);
		this.eventList.remove(eventToRemove);
		
		eventToRemove.removeParticipant(this.owner);

	}

	/**
	 * Returns a List with all ESEEvents, that may be view by the current user.
	 * @return {@code ArrayList<ESEEvent>} all events.
	 * @see ESEDatabase#getCurrentUser()
	 */
	public ArrayList<ESEEvent> getAllAllowedEvents()
	{
		try
		{
			if (ESEDatabase.getCurrentUser().equals(this.owner))
			{
				return this.getAllEvents();
			}
			else
			{
				return this.getAllPublicEvents();
			}
		}
		catch (ESEExceptionGuestUser e) 
		{
			return this.getAllPublicEvents();
		}
	}

	/**
	 * Returns a List of {@link ESEEvent}, which may be viewed by all ESEUser.
	 * @return {@code ArrayList<ESEEvent>} of all public ESEEvents of this ESECalendar.
	 */
	public ArrayList<ESEEvent> getAllPublicEvents()
	{
		ArrayList<ESEEvent> publicEventsList = new ArrayList<ESEEvent>();
		for (ESEEvent event : this.eventList)
		{
			if (event.isPublic())
			{
				publicEventsList.add(event);
			}
		}
		return publicEventsList;
	}

	/**
	 * Returns a List of all {@link ESEEvent} of this ESECalendar.
	 * This includes all public, as well as all private ESEEvents.
	 * @return {@code ArrayList<ESEEvent>} of all ESEEvents.
	 */
	public ArrayList<ESEEvent> getAllEvents()
	{
		return new ArrayList<ESEEvent>(this.eventList);
	}

	/**
	 * Returns a List of all {@link ESEEvent}, that may be viewed by the current
	 * ESEUser within the given month.
	 * See {@link #getAllEventsOfMonth} for the same function
	 * but without restriction of current user.
	 * @param month given
	 * @param year given
	 * @return {@code ArrayList<ESEEvent>} of ESEEvents of given month.
	 * @see #getAllEventsOfMonth(int, int)
	 * @see ESEDatabase#getCurrentUser()
	 */
	public ArrayList<ESEEvent> getAllAllowedEventsOfMonth(int month, int year)
	{
		try
		{
			if (ESEDatabase.getCurrentUser().equals(this.owner))
			{
				return this.getAllEventsOfMonth(month, year);
			}
			else
			{
				return this.getAllPublicEventsOfMonth(month, year);
			}
		}
		catch (ESEExceptionGuestUser e)
		{
			return this.getAllPublicEventsOfMonth(month, year);
		}
	}

	/**
	 * Returns a List of all {@link ESEEvent} within the given month.
	 * See {@link #getAllAllowedEventsOfMonth} for the same function
	 * but with restriction of current user.
	 * @param month given.
	 * @param year given.
	 * @return {@code ArrayList<ESEEvent>} of ESEEvents of given month.
	 * @see #getAllAllowedEventsOfMonth(int, int)
	 */

	public ArrayList<ESEEvent> getAllEventsOfMonth(int month, int year)
	{
		Calendar monthAsCal = new GregorianCalendar();
		monthAsCal.set(Calendar.MONTH, month);
		monthAsCal.set(Calendar.YEAR, year);
		Calendar startCal = new GregorianCalendar();
		Calendar endCal = new GregorianCalendar();
		ArrayList<ESEEvent> eventsOfMonth = new ArrayList<ESEEvent>();
		for (ESEEvent event : this.eventList)
		{
			startCal.setTime(event.getStartDate());
			endCal.setTime(event.getEndDate());
			if (monthAsCal.get(monthAsCal.MONTH) == startCal.get(startCal.MONTH)
					&& monthAsCal.get(monthAsCal.YEAR) == startCal.get(startCal.YEAR))
				eventsOfMonth.add(event);
			else if (monthAsCal.get(monthAsCal.MONTH) == endCal.get(endCal.MONTH)
					&& monthAsCal.get(monthAsCal.YEAR) == endCal.get(endCal.YEAR))
				eventsOfMonth.add(event);
			else if ((monthAsCal.get(monthAsCal.MONTH) > startCal.get(startCal.MONTH)
					&& monthAsCal.get(monthAsCal.YEAR) == startCal.get(startCal.YEAR))
					&& (monthAsCal.get(monthAsCal.MONTH) < endCal.get(endCal.MONTH)
					&& monthAsCal.get(monthAsCal.YEAR) == endCal.get(endCal.YEAR)))
				eventsOfMonth.add(event);
		}
		return eventsOfMonth;
	}

	/**
	 * Returns a List of all {@link ESEEvent} within the given month, that
	 * may be viewed by all ESEUsers.
	 * @param month given.
	 * @param year given.
	 * @return {@code ArrayList<ESEEvent>} of ESEEvents of current month, that are public.
	 */
	public ArrayList<ESEEvent> getAllPublicEventsOfMonth(int month, int year)
	{
		ArrayList<ESEEvent> listOfPublicEvents = new ArrayList<ESEEvent>();

		for (ESEEvent event : this.getAllEventsOfMonth(month, year))
		{
			if (event.isPublic())
				listOfPublicEvents.add(event);
		}
		return listOfPublicEvents;
	}

	/**
	 * Returns a List with Integers. The size of the List is equal to the number of
	 * days of the given month. The List starts with 1 and ends with the last day
	 * of the month.
	 * @param month given.
	 * @param year given.
	 * @return {@code List<Integer>} of {@link Integer} representing a month.
	 * @see #getDaysFromLastMonth(int, int)
	 * @see #getDaysFromNextMonth(int, int)
	 */
	public List<Integer> getDaysFromThisMonth(int month, int year)
	{
		Calendar cal = new GregorianCalendar();
		cal.set(year, month, 1);
		int max = cal.getActualMaximum(Calendar.DAY_OF_MONTH);

		List<Integer> daysFromThisMonth = new ArrayList<Integer>();
		for (int i = 0; i < max; i++)
		{
			daysFromThisMonth.add(i + 1);
		}
		return daysFromThisMonth;
	}

	/**
	 * Returns a List with Integers. The size of the List is equal to the number of
	 * days of the previous month. The List starts with 1 and ends with the last day
	 * of the previous month.
	 * @param month given.
	 * @param year given.
	 * @return {@code List<Integer>} of {@link Integer} representing a month.
	 * @see #getDaysFromThisMonth(int, int)
	 * @see #getDaysFromNextMonth(int, int)
	 */
	public List<Integer> getDaysFromLastMonth(int month, int year)
	{
		List<Integer> daysFromLastMonth = new ArrayList<Integer>();
		Calendar thisMonth = new GregorianCalendar();
		Calendar lastMonth = new GregorianCalendar();
		thisMonth.set(Calendar.MONTH, month);
		thisMonth.set(Calendar.YEAR, year);
		lastMonth.set(Calendar.MONTH, month - 1);
		lastMonth.set(Calendar.YEAR, year);

		thisMonth.setFirstDayOfWeek(Calendar.MONDAY);
		thisMonth.set(Calendar.DAY_OF_MONTH, 1);
		int max = lastMonth.getActualMaximum(Calendar.DAY_OF_MONTH);
		int start = thisMonth.get(Calendar.DAY_OF_WEEK);

		if (start == 1)
		{
			start = 8;
		}

		for (int i = max - start + 2; i < max; i++)
		{
			daysFromLastMonth.add(i + 1);
		}
		return daysFromLastMonth;
	}

	/**
	 * Returns a List with Integers. The size of the List is equal to the number of
	 * days of the next month. The List starts with 1 and ends with the last day
	 * of the next month.
	 * @param month given.
	 * @param year given.
	 * @return {@code List<Integer>} of {@link Integer} representing a month.
	 * @see #getDaysFromThisMonth(int, int)
	 * @see #getDaysFromLastMonth(int, int)
	 */
	public List<Integer> getDaysFromNextMonth(int month, int year)
	{
		List<Integer> daysFromNextMonth = new ArrayList<Integer>();
		int daysToAdd = 7 - (this.getDaysFromThisMonth(month, year).size()
				+ this.getDaysFromLastMonth(month, year).size()) % 7;
		for (int i = 1; i <= daysToAdd; i++)
		{
			daysFromNextMonth.add(i);
		}
		return daysFromNextMonth;
	}

	/**
	 * Returns a List with Integers. Each Integer within this
	 * List represents a date of the given month, where an {@link ESEEvent} takes place.
	 * @param month given.
	 * @param year given.
	 * @return {@code List<Integer>} date with ESEEvents.
	 */
	public ArrayList<Integer> getEventDaysOfMonth(int month, int year)
	{
		ArrayList<Integer> eventDaysList = new ArrayList<Integer>();

		for (ESEEvent event : this.getAllAllowedEventsOfMonth(month, year))
		{
			for (int i = 1; i <= 31; i++)
			{
				if (event.isEventDay(i, month, year))
				{
					eventDaysList.add(i);
				}
			}
		}
		return eventDaysList;
	}
	
	/**
	 * Setter for calendar name
	 * @param newCalendarName Must not be empty.
	 * @throws ESEException calendarName is empty.
	 */
	public void setCalendarName(String newCalendarName) throws ESEException
	{
		checkCalendarName(newCalendarName, this.owner);
		this.calendarName = newCalendarName;
	}

	private void checkCalendarName(String calendarName, ESEUser calendarOwner) throws ESEException
	{
		if (calendarName.isEmpty())
		{
			throw new ESEException("Calendar name must not be empty!");
		}

		if (calendarOwner == null)
		{
			throw new ESEException("Calendar is not assigned to any user!");
		}

//		for (ESECalendar calendar : calendarOwner.getCalendarList())
//		{
//			if (calendarName.equals(calendar.getCalendarName()))
//			{
//				throw new ESEException("A calendar with this name already exists!");
//			}
//		}
	}

	public String toString()
	{
		return this.calendarName;
	}
}
