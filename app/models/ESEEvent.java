package models;

import java.util.Date;
import javax.persistence.*;
import play.data.validation.Required;
import play.db.jpa.Model;
import utils.ESEConversionHelper;

/** Class handling events in calendars
 * @see ESECalendar
 */
@Entity
public class ESEEvent extends Model
{

	/** Maps entity relations from events to a calendar.
	 */
	@ManyToOne
	public ESECalendar correspondingCalendar;
	/** The name of the event displayed in the calendar.
	 */
	public String eventName;
	/** Date when the event is set to begin.
	 */
	public Date startDate;
	/** Date of the end of the event.
	 */
	public Date endDate;
	/** Declares, whether the event is visible to other users or only to the owner of the calendar.
	 */
	public boolean isPublic;

	/**
	 * @deprecated Es soll stattdessen {@link #ESEEvent(String, String, String, ESECalendar, String)} verwendet werden
	 */
	public ESEEvent(String eventName, String strStart, String strEnd, String strIsPublic, ESECalendar correspondingCalendar)
	{
		new ESEEvent(eventName, strStart, strEnd, correspondingCalendar, strIsPublic);
	}

	/** Constructor for new events to add to a calendar. This constructor is not intended for direct use. Instead use {@link ESEFactory} for this.
	 * @param eventName Description of the event shown in the calendar, it does not have to be unique
	 * @param strStart Start date of the event. For easy use the date can be entered as a {@link String} in the form <code>dd.MM.yyyy HH:mm</code>, it is then parsed internally to a {@link Date} value
	 * @param strEnd End date of the event, same rules as for <code>strStart</code> apply
	 * @param correspondingCalendar The calendar to which this event should belong
	 * @param strIsPublic Declares if the event should be publicly visible by other users
	 * @see ESECalendar
	 * @see ESEUser
	 * @see ESEConversionHelper
	 * @see ESEFactory
	 */
	public ESEEvent(@Required String eventName,
			@Required String strStart, @Required String strEnd,
			@Required ESECalendar correspondingCalendar, @Required String strIsPublic)
	{
		this.eventName = eventName;
		this.startDate = ESEConversionHelper.convertStringToDate(strStart);
		this.endDate = ESEConversionHelper.convertStringToDate(strEnd);
		// TODO: Is strStart <= strEnd?
		if (startDate.getTime() > endDate.getTime())
		{
			// TODO: Complain as these dates are !possible
		}
		this.correspondingCalendar = correspondingCalendar;
		this.correspondingCalendar.save();
		this.isPublic = Boolean.parseBoolean(strIsPublic);
	}

	/**
	 * @deprecated Es soll stattdessen {@link #editEventName(String)} verwendet werden
	 */
	public void renameEvent(@Required String newName)
	{
		editEventName(newName);
	}

	/**
	 * @deprecated Es soll stattdessen {@link #getEventName()} verwendet werden
	 */
	public String getName()
	{
		return this.getEventName();
	}

	/** Returns the name of the event.
	 * @return Description of the event displayed in the calendar
	 */
	public String getEventName()
	{
		return this.eventName;
	}

	/** Returns the date of the start of the event.
	 * @return A {@link Date} that represents the start of the event
	 */
	public Date getStartDate()
	{
		return this.startDate;
	}

	/** Returns the date when the calendar ends.
	 * @return A {@link Date} when the event ends
	 */
	public Date getEndDate()
	{
		return this.endDate;
	}

	/** Returns visibility of the event.
	 * @return {@link Boolean} value if the event can be seen by all other users or not
	 */
	public boolean isPublic()
	{
		return this.isPublic;
	}

	/** Searches an event in the database
	 * @param id The Id the event was assigned when added to the database
	 * @return Matching {@link ESEEvent} from the database
	 */
	public static ESEEvent findEventById(long eventId)
	{
		return findById(eventId);
	}

	// TODO: Setters = editEvents? How does this look like?
	/** Changes the name of an existing event and notifies the database about the change.
	 * @param newName New description of the event
	 */
	public void editEventName(@Required String newName)
	{
		this.eventName = newName;
		this.save();
	}

	/** Changes the start date of an event and notifies the database about the change.
	 * @param newStartDate New start date of the event, it is parsed to a {@link Date} value
	 */
	public void editStartDate(@Required String newStartDate)
	{
		this.startDate = ESEConversionHelper.convertStringToDate(newStartDate);
		this.save();
	}

	/** Changes the end date of an event and notifies the database about the change.
	 * @param newEndDate New end date of the event, it is parsed to a {@link Date} value
	 */
	public void editEndDate(@Required String newEndDate)
	{
		this.endDate = ESEConversionHelper.convertStringToDate(newEndDate);
		this.save();
	}

	/** Changes the visibility of the event and notifies the database about the change.
	 * @param publiclyViewable {@link String} that describes the new visibility, it is parsed to a {@link Boolean} value
	 */
	public void editVisibility(@Required String publiclyViewable)
	{
		this.isPublic = Boolean.parseBoolean(publiclyViewable);
		this.save();
	}
}
