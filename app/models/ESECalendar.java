package models;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import javax.persistence.*;
import play.data.validation.Required;
import play.db.jpa.Model;

/** Class that manages calendars from users and its events.
 * @see ESEUser
 * @see ESEEvent
 */
@Entity
public class ESECalendar extends Model
{
	/** Name of the calendar to distinguish between several calendars.
	 */
	public String calendarName;
	/** All events entered in the calendar are stored in this list.
	 */
	@OneToMany(mappedBy = "correspondingCalendar", cascade = CascadeType.ALL)
	public List<ESEEvent> eventList;
	/** Maps entity relations from calendars to an user.
	 */
	@ManyToOne
	public ESEUser owner;

	/** Constructor for new calendars of an user. This constructor is not intended for direct use. Instead use </code>ESEFactory</code> for this.
	 * @param calendarName Name of the calendar to differentiate between, it does not have to be unique
	 * @param owner The user to which the calendar is added
	 * @see ESEEvent
	 * @see ESEUser
	 * @see ESEConversionHelper
	 * @see ESEFactory
	 */
	public ESECalendar(String calendarName, ESEUser owner)
	{
		this.calendarName = calendarName;
		this.eventList = new ArrayList<ESEEvent>();
		this.owner = owner;
	}

	/** Creates a new event and checks, if the event overlaps with existing events, a warning will be given and the event will not be added.
	 * @param eventName Description of the event shown in the calendar
	 * @param startDate Start date of the event as "{@code dd.MM.yyyy HH:mm}" formatted
	 * @param endDate End date of the event as "{@code dd.MM.yyyy HH:mm}" formatted
	 * @param isPublic Visibility to other users as {@link String}, parsed to a {@link Boolean}
	 */
	public void addEvent(@Required String eventName, @Required String startDate,
						 @Required String endDate, @Required String isPublic)
	{
		ESEEvent newEvent = ESEFactory.createEvent(eventName, startDate, endDate, this, isPublic);
		for (ESEEvent existingEvent : this.eventList)
		{
			if (checkEventOverlaps(existingEvent, newEvent))
			{
				// TODO: Complain as new event overlaps with other event
			}
		}
		this.eventList.add(newEvent);
		this.save();
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
	 * @deprecated Es soll stattdessen {@link #getCalendarName()} verwendet werden
	 */
	public String getName()
	{
		return this.getCalendarName();
	}

	/** Returns the name of the calendar.
	 * @return Name of the calendar
	 */
	public String getCalendarName()
	{
		return this.calendarName;
	}

	/** Returns the owner of the calendar.
	 * @return {@link ESEUser} that owns this calendar
	 */
	public ESEUser getOwner()
	{
		return this.owner;
	}

	/**
	 * @deprecated Es soll stattdessen {@link #editCalendarName(String)} verwendet werden
	 */
	public void renameCalendar(@Required String newName)
	{
		editCalendarName(newName);
	}

	/** Changes the name of the calendar.
	 * @param newName New description of the calendar
	 */
	public void editCalendarName(@Required String newName)
	{
		this.calendarName = newName;
		this.save();
	}

	/** Deletes an event from the calendar.
	 * @param eventId The Id the event was assigned when added to the database
	 */
	public void removeEvent(@Required Long eventId)
	{
		for (ESEEvent e : this.eventList)
		{
			if (e.getId().equals(eventId))
			{
				this.eventList.remove(e);
				e.delete(); // DB stuff
				this.save();
				break;
			}
		}
		//TODO: Complain as this event is not in the database
	}

	/**
	 * @deprecated Es soll stattdessen {@link #findCalendarById(long)} verwendet werden
	 */
	public static ESECalendar getCalendar(String id)
	{
		return findCalendarById(Long.parseLong(id));
	}

	/** Searches a calendar in the database.
	 * @param id The Id the calendar was assigned when added to the database
	 * @return Matching {@link ESECalendar} from the database
	 */
	public static ESECalendar findCalendarById(long calendarId)
	{
		return findById(calendarId);
	}

	/** Provides events that last at the given day regardless of the time they start or end.
	 * @param calendarDay The day of interest given as a {@link String} formatted as "{@code dd.MM.yyyy HH:mm}" while time is irrelevant
	 * @param onlyPublic Limitation whether only events which are marked as publicly visible should be taken into consideration
	 * @return {@link ArrayList} with the events found running at this day
	 */
	public ArrayList<ESEEvent> getListOfEventsRunningAtDay(@Required String calendarDay, boolean onlyPublic)
	{
		//TODO: Find a better way to verify input
		ESEConversionHelper.convertStringToDate(calendarDay);
		ArrayList<ESEEvent> eventsFormDate = new ArrayList<ESEEvent>();
		// Create pseudo event that
		// starts at "calendarDay" 00:00 and
		// ends at "calendarDay" 23:59
		ESEEvent pseudoEvent = new ESEEvent("CompareHelperEvent",
				calendarDay.substring(0, 10) + " 00:00",
				calendarDay.substring(0, 10) + " 23:59", this, "true");
		for (ESEEvent e : this.eventList)
		{
			if (checkEventOverlaps(e, pseudoEvent) && (!onlyPublic || e.isPublic()))
			{
				//if (onlyPublic && !e.isPublic())
				//{
					//continue;
				//}
				eventsFormDate.add(e);
			}
		}
		return eventsFormDate;
	}

	/** Provides events that last at the given day regardless of the time they start or end.
	 * @param calendarDay The day of interest given as a {@link Date} while time is irrelevant
	 * @param onlyPublic Limitation whether only events which are marked as publicly visible should be taken into consideration
	 * @return {@link ArrayList} with the events found running at this day
	 */
	public ArrayList<ESEEvent> getListOfEventsRunningAtDay(@Required Date calendarDay, boolean onlyPublic)
	{
		String calendarDayString = ESEConversionHelper.convertDateToString(calendarDay);
		return getListOfEventsRunningAtDay(calendarDayString, onlyPublic);
	}

	/** Provides events that last at the given day regardless of the time they start or end.
	 * @param calendarDay The day of interest given as a {@link String} formatted as "{@code dd.MM.yyyy HH:mm}" while time is irrelevant
	 * @param onlyPublic Limitation whether only events which are marked as publicly visible should be taken into consideration
	 * @return {@link Iterator} with the events found running at this day
	 */
	public Iterator<ESEEvent> getIteratorOfEventsRunningAtDay(@Required String calendarDay, boolean onlyPublic)
	{
		return this.getListOfEventsRunningAtDay(calendarDay, onlyPublic).iterator();
	}

	/** Provides events that last at the given day regardless of the time they start or end.
	 * @param calendarDay The day of interest given as a {@link Date} while time is irrelevant
	 * @param onlyPublic Limitation whether only events which are marked as publicly visible should be taken into consideration
	 * @return {@link Iterator} with the events found running at this day
	 */
	public Iterator<ESEEvent> getIteratorOfEventsRunningAtDay(@Required Date calendarDay, boolean onlyPublic)
	{
		return this.getListOfEventsRunningAtDay(calendarDay, onlyPublic).iterator();
	}

	/** Provides all events listed in this calendar.
	 * @return An {@link ArrayList} of the events
	 */
	public ArrayList<ESEEvent> getAllEventsAsList()
	{
		return new ArrayList<ESEEvent>(eventList);
	}

	/** Provides all events marked as publicly visible listed in this calendar.
	 * @return An {@link ArrayList} of the events
	 */
	public ArrayList<ESEEvent> getPublicEventsAsList()
	{
		ArrayList<ESEEvent> publicEventList = new ArrayList<ESEEvent>();
		for (ESEEvent e : this.eventList)
		{
			if (e.isPublic())
			{
				publicEventList.add(e);
			}
		}
		return publicEventList;
	}

	/** Provides all events listed in this calendar.
	 * @return An {@link Iterator} of the events
	 */
	public Iterator<ESEEvent> getAllEventsAsIterator()
	{
		return this.getPublicEventsAsList().iterator();
	}

	/** Provides all events marked as publicly visible listed in this calendar.
	 * @return An {@link Iterator} of the events
	 */
	public Iterator<ESEEvent> getPublicEventsAsIterator()
	{
		return this.getAllEventsAsList().iterator();
	}

	/**
	 *	XXX: need something like this..
	 */
	public ESEEvent getEvent(Long id) {
		for (ESEEvent e : this.eventList) {
			if (id == e.getId()) {
				return e;
			}
		}
		return null;
	}
}
