/*
 * Project: ESECalendar team 5
 * Authors:
 * 		Rafael Breu
 * 		Renato Corti
 * 		Lukas Keller
 * Last Change:
 * 	by:		xxx
 * 	date:	xxx
 */

package models;

import java.util.ArrayList;

/**
 * Represents a User object in ESE Calendar application.<br>
 * A user is uniquely defined by it's <code>userID</code> and described by it's
 * <code>profile</code>, that contains Name and other personal Information.<br>
 * A User has a <code>password</code> that is used to login.<br>
 * A User can have several Calendars as well as different groups of contacts that
 * can be added to its lists.
 *
 *@see ESECalendar
 *@see ESEGroup
 *@see ESEDatabase
 */
public class ESEUser
{
	/**
	 * static counter to determine uniquely userID.<br>
	 * Increased after initialization of an new ESEUser.
	 */
	private static int idCounter = 0;
	/**
	 * Number to identify each ESEUser.
	 */
	private int userID;
	/**
	 * Describes personal informations about an ESEUser.
	 * 
	 * @see ESEProfil
	 */
	private ESEProfile profile;

	/**
	 * String needed by ESEUser to login an application.<br>
	 * This String must not be empty.
	 */
	private String password;
	/**
	 * Together with <code>secureAnswer</code> an ESEUser is able
	 * to reset it's password.
	 */
	private String secureQuestion = "Holy solution";
	/**
	 * Together with <code>secureQuestion</code> an ESEUser is able
	 * to reset it's password.
	 */
	private String secureAnswer = "42";

	/**
	 * Contains all ESECalendars that are owned by this ESEUser.
	 * 
	 * @see ESECalendar
	 */
	private ArrayList<ESECalendar> calendarList;
	/**
	 * Contains all ESEGroups of an ESEUser. Within these groups, an
	 * ESEUser may organize Contacts (other ESEUser).
	 */
	private ArrayList<ESEGroup> groupList;

	/**
	 * Default Constructor for ESEUser. <br>
	 * Question and Answer to change password are set to default:<br>
	 * Question: "Holy solution", Answer:"42"<p>
	 * Example:
	 * 		ESEUser user = ("UsernameOfJack", "secretPassword", "Jack", "Sparrow");
	 * 
	 * @param username String must not be empty, or an Exception is thrown.
	 * @param password String must not be empty, or an Exception is thrown.
	 * @param firstName First name of registered Person.
	 * @param familyName Second/Family name of registered Person.
	 * @throws ESEException
	 * 
	 * @see {@link #ESEUser(String, String, String, String, String, String)} extended constructor<br>
	 * to create ESEUser with individual Answer and Question.
	 * @see {@link ESEDatabase} organizes ESEUser to serve in application.
	 */
	public ESEUser(String username, String password, String firstName, String familyName) throws ESEException
	{
		this(username, password, firstName, familyName, "Holy solution", "42");
	}
	/**
	 * Extended constructor for ESEUser that also provides the option of choosing
	 * an own Question and Answer to reset the password.
	 * 
	 * @param username String must not be empty and unique in {@link ESEDatabase}, or an Exception is thrown.
	 * @param password String must not be empty, or an Exception is thrown.
	 * @param firstName First name of registered Person.
	 * @param familyName Second/Family name of registered Person.
	 * @param secureQuestion A question only the user can answer.
	 * @param secureAnswer Answer to <code>secureQuestion</code>.
	 * @throws ESEException
	 */
	public ESEUser(String username, String password,
			String firstName, String familyName,
			String secureQuestion, String secureAnswer) throws ESEException
	{
		if(username.isEmpty())
		{
			throw new ESEException("User name must not be empty!");
		}

		for(ESEUser user : ESEDatabase.getAllUsers())
		{
			if(username.equals(user.getName()))
			{
				throw new ESEException("This user name is already in the database!");
			}
		}

		this.userID = idCounter++;
		this.calendarList = new ArrayList<ESECalendar>();
		this.groupList = new ArrayList<ESEGroup>();
		ESEGroup friends = new ESEGroup("Friends", this);
		this.groupList.add(friends);
		this.profile = new ESEProfile(this, username, firstName, familyName);

		this.secureQuestion = secureQuestion;
		this.secureAnswer = secureAnswer;
		this.password = password;
	}

	/*
	 * Methods with read only access
	 */
	/**
	 * @return {@link int} unique userID of this ESEUser.
	 */
	public int getUserID(){
		return this.userID;
	}
	/**
	 * 
	 * @return {@link String} username of this ESEUser, specified by its ESEProfile.
	 */
	public String getName()
	{
		return this.profile.getUsername();
	}
	/**
	 * 
	 * @return {@link String} password of this ESEUser.
	 */
	public String getPassword()
	{
		return this.password;
	}
	/**
	 * 
	 * @return {@link String} firstName of this ESEUser.
	 */
	public String getFirstName()
	{
		return this.profile.getFirstName();
	}
	/**
	 * 
	 * @return {@link String} familyName of this ESEUser.
	 */
	public String getFamilyName()
	{
		return this.profile.getFamilyName();
	}
	/**
	 * 
	 * @return {@link ESEProfile} containing this ESEUsers personal informations.
	 * 
	 * @see ESEProfile
	 */
	public ESEProfile getProfile()
	{
		return this.profile;
	}
	/**
	 * 
	 * @return {@link String} that contains the Question.
	 */
	public String getQuestion()
	{
		return this.secureQuestion;
	}
	/**
	 * 
	 * @return {@link String} that contains the Answer.
	 */
	public String getAnswer()
	{
		return this.secureAnswer;
	}
	/**
	 * 
	 * @return {@link ArrayList} of type {@link ESEGroup}. This is a
	 * shallow copy, so changes made within this ArrayList will not 
	 * affect the real Data.
	 */
	public ArrayList<ESEGroup> getGroupList()
	{
		return new ArrayList<ESEGroup>(this.groupList);
	}
	/**
	 * 
	 * @return {@link ArrayList} of type {@link ESECalendar}. This is a
	 * shallow copy, so changes made within this ArrayList will not 
	 * affect the real Data.
	 */
	public ArrayList<ESECalendar> getCalendarList()
	{
		return new ArrayList<ESECalendar>(this.calendarList);
	}
	
	/**
	 *Creates an new {@link ESECalendar} for this ESEUser. <br>
	 *
	 * @param calendarName Must not be empty.
	 * 
	 * @throws ESEException
	 * 
	 * @see {@link #addCalendar(ESECalendar)} to only add but not create a new calendar.
	 */
	public void addCalendar(String calendarName) throws ESEException
	{
		this.calendarList.add(new ESECalendar(calendarName, this));
	}
	/**
	 * Creates an new {@link ESEGroup} for this ESEUser. <br>
	 * 
	 * @param groupName Must not be empty.
	 * @throws ESEException
	 */
	public void addGroup(String groupName) throws ESEException
	{
		ESEGroup group = new ESEGroup(groupName, this);
		this.groupList.add(group);
	}
	/**
	 * Returns the corresponding ESECalendar of this ESEUser.
	 * 
	 * @param id unique 
	 * @return {@link ESECalendar} with corresponding ID of this ESEUser.
	 * @throws ESEException
	 * 
	 * @see {@link #getCalendarByName(String)} to search by Calendar name.
	 * @see {@link #getCalendarList()} to get all Calendars.
	 */
	public ESECalendar getCalendarByID(int id) throws ESEException
	{
		for (ESECalendar calendar : calendarList)
		{
			if (calendar.getID() == id)
			{
				return calendar;
			}
		}
		throw new ESEException("No calendar with ID \"" + id + " \"!");
	}
	/**
	 * Returns the corresponding ESECalendar of this ESEUser.
	 * 
	 * @param name of searched ESECalendar.
	 * @return {@link ESECalendar} searched.
	 * @throws ESEException
	 * @see {@link #getCalendarByID(int)} to search by Calendar ID.
	 * @see {@link #getCalendarList()} to get all Calendars.
	 */
	public ESECalendar getCalendarByName(String name) throws ESEException
	{
		for (ESECalendar calendar : calendarList)
		{
			if (calendar.getCalendarName().equals(name))
			{
				return calendar;
			}
		}
		throw new ESEException("No calendar with name \"" + name + "\"!");
	} 
	/**
	 * Returns the corresponding ESEGroup to this <code>id</code>.
	 * 
	 * @param id of searched ESEGroup
	 * @return {@link ESECalendar} searched.
	 * @throws ESEException
	 * @see {@link #getGroupByName(String)}
	 * @see {@link #getGroupList()}
	 */
	public ESEGroup getGroupByID(int id) throws ESEException
	{
		for (ESEGroup group : groupList)
		{
			if (group.getGroupID() == id)
			{
				return group;
			}
		}
		throw new ESEException("No group with ID \"" + id + "\"!");
	}
	/**
	 * Returns the corresponding ESEGroup to this <code>name</code>.
	 * 
	 * @param name of searched ESEGroup
	 * @return {@link ESECalendar} searched.
	 * @throws ESEException
	 * @see {@link #getGroupByID(String)}
	 * @see {@link #getGroupList()}
	 */
	public ESEGroup getGroupByName(String name) throws ESEException
	{
		for (ESEGroup group : groupList)
		{
			if (group.getGroupName().equals(name))
				return group;
		}
		throw new ESEException("No group with name \"" + name + "\"!");
	}

	public void removeCalendar(int calendarID) throws ESEException
	{
		ESECalendar calendarToRemove = this.getCalendarByID(calendarID);
		this.calendarList.remove(calendarToRemove);
	}
	/**
	 * Returns all ESEEvents of the ESECalendar with the corresponding ID.<br>
	 * 
	 * @param calendarID 
	 * @return {@link ArrayList} of type ESEEvent.
	 * @throws ESEException
	 * 
	 * @see {@link #getAllPublicEvents(int)} to get only public ESEEvents of this ESECalendar.
	 * @see {@link ESEEvent}
	 */
	public ArrayList<ESEEvent> getAllEvents(int calendarID) throws ESEException
	{
		for (ESECalendar calendar : this.calendarList)
		{
			if (calendar.getID() == calendarID)
			{
				return calendar.getAllEvents();
			}
		}
		throw new ESEException("No calendar with this ID!");
	}
	/**
	 * Returns all public ESEEvents of the ESECalendar with the corresponding ID. <br>
	 * 
	 * @param calendarID
	 * @return {@link ArrayList} of type ESEEvent.
	 * @throws ESEException
	 * 
	 * @see {@link #getAllPublicEvents(int)} to get only public ESEEvents of this ESECalendar.
	 * @see {@link ESEEvent}
	 * 

	 */
	public ArrayList<ESEEvent> getAllPublicEvents(int calendarID) throws ESEException
	{
		for (ESECalendar calendar : this.calendarList)
		{
			if (calendar.getID() == calendarID)
			{
				return calendar.getAllPublicEvents();
			}
		}
		throw new ESEException("No calendar with this ID!");
	}

	/*
	 * Methods with read-write access
	 */
	/**
	 *Adds an ESECalendar to this ESEUsers Calendar List. <br>
	 *
	 * @param calendarToAdd owner must be this.
	 * 
	 * @throws ESEException
	 * 
	 * @see {@link #addCalendar(String)} to create and add a new calendar.
	 */
	public void addCalendar(ESECalendar calendarToAdd) throws ESEException
	{
		if(!this.equals(calendarToAdd.getOwner()))
		{
			throw new ESEException("Taking a foreign calendar is not allowed!");
		}
		this.calendarList.add(calendarToAdd);
	}
	/**
	 * Creates and adds a new Group to the list of this ESEUser. <br>
	 * 
	 * @param groupToAdd String must not be empty.
	 * @throws ESEException
	 */
	public void addGroup(ESEGroup groupToAdd) throws ESEException
	{
		if(!this.equals(groupToAdd.getOwner()))
		{
			throw new ESEException("Taking a foreign group is not allowed!");
		}
		this.groupList.add(groupToAdd);
	}
	/**
	 * Sets the password to a new value.
	 * 
	 * @param newPassword String must not be empty.
	 * 
	 * @see #setAnswer(String)
	 * @see #setQuestion(String)
	 */
	public void setPassword(String newPassword)
	{
		this.password = newPassword;
	}
	
	/**
	 * Replaces the old questions with a new secret question.
	 * 
	 * @param newQuestion String must not be empty.
	 * @see {@link #setAnswer(String)} to change also the answer to the new question.
	 */
	public void setQuestion(String newQuestion)
	{
		this.secureQuestion = newQuestion;
	}
	/**
	 * Replaces the old answer with a new one.
	 * The answer should relate to the current <code>secretQuestion</code>.
	 * 
	 * @param newAnswer String must not be empty.
	 * @see {@link #setQuestion(String)}
	 */
	public void setAnswer(String newAnswer)
	{
		this.secureAnswer = newAnswer;
	}

}
