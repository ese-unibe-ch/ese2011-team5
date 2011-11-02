package models;

import java.util.Date;

/** Class whose sole purpose is to create and save new objects to the database.<br>
 * Requests are delegated to the constructor of the corresponding object.<br>
 * Afterwards it lets the database know about the object.
 */
public abstract class ESEFactory
{
	/** Creates a new calendar and tells the database about it.
	 * @param calendarName Name displayed as the description of the calendar
	 * @param owner User that will have this calendar added to the calendar list, normally <code>this</code> is given as an user object
	 * @return The newly created {@link ESECalendar} object
	 */
	public static ESECalendar createCalendar(String calendarName, ESEUser owner)
	{
		ESECalendar calendar = new ESECalendar(calendarName, owner);
		calendar.save();

		return calendar;
	}

	/**
	 * @deprecated Es soll stattdessen {@link #createEvent(String, String, String, ESECalendar, String)} verwendet werden
	 */
	public static ESEEvent createEvent(String eventName, String strStart,
			String strEnd, String strIsPublic, ESECalendar correspondingCalendar)
	{
		return ESEFactory.createEvent(eventName, strStart, strEnd,
				correspondingCalendar, strIsPublic);
	}

	/** Creates a new event and tells the database about it.
	 * @param eventName Name displayed in the calendar
	 * @param strStart Date when the events starts, entered as a {@link String} in the form <code>dd.MM.yyyy HH:mm</code>, it is parsed in the constructor to a {@link Date} value
	 * @param strEnd Date when the events ends, same rules as for <code>strStart</code> apply
	 * @param correspondingCalendar Calendar that will have this event in the event list, normally <code>this</code> is given as a calendar object
	 * @param strIsPublic Defines, if the event should be visible to other users
	 * @return The newly created {@link ESEEvent} object
	 */
	public static ESEEvent createEvent(String eventName, String strStart, String strEnd,
			ESECalendar correspondingCalendar, String strIsPublic)
	{
		ESEEvent event = new ESEEvent(eventName, strStart, strEnd,
				correspondingCalendar, strIsPublic);
		correspondingCalendar.save();
		event.save();

		return event;
	}

	/** Creates a new group and tells the database about it.
	 * @param groupName Description of the group
	 * @param owner User that would create a new group
	 * @return The newly created {@link ESEGroup} object
	 */
	public static ESEGroup createGroup(String groupName, ESEUser owner)
	{
		ESEGroup group = new ESEGroup(groupName, owner);
		owner.save();
		group.save();

		return group;
	}

	/** Creates a new user and tells the database about it.
	 * @param username Username used to log in, it has to be unique
	 * @param password Password used to authenticate as this user
	 * @return The newly created {@link ESEUser} object
	 */
	public static ESEUser createUser(String username, String password)
	{
		ESEUser user = new ESEUser(username, password);
		user.save();

		return user;
	}

	/** Creates a new user and tells the database about it.
	 * @param username Username used to log in, it has to be unique
	 * @param password Password used to authenticate as this user
	 * @param firstName First name displayed in the profile of the user
	 * @param familyName Family name displayed in the profile of the user
	 * @return The newly created {@link ESEUser} object
	 */
	public static ESEUser createUser(String username, String password,
			String firstName, String familyName)
	{
		ESEUser user = new ESEUser(username, password, firstName, familyName);
		user.save();

		return user;
	}
}
