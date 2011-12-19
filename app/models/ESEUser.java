/*
 * Project: ESECalendar team 5
 * Authors:
 * 		Rafael Breu
 * 		Renato Corti
 * 		Lukas Keller
 */

package models;

import java.util.ArrayList;
import java.util.Date;
import models.visitor.Visitable;
import models.visitor.Visitor;

/**
 * Represents a User object in ESE Calendar application.<br>
 * A user is uniquely defined by it's <code>userID</code> and described by it's
 * <code>profile</code>, that contains Name and other personal Information.<br>
 * A User has a <code>password</code> that is used to login.<br>
 * A User can have several Calendars as well as different groups of contacts that
 * can be added to its lists.
 * @see ESECalendar
 * @see ESEGroup
 * @see ESEDatabase
 */
public class ESEUser implements Visitable
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
	 */
	private Date registrationDate;
	private String stateMessage = "Hi there!";
	private String username;
	private String firstName = "-";
	private String familyName = "-";

	private Date birthday;

	private String eMail = "-";

	private String street = "-";
	private String city = "-";
	private String postCode = "-";

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
	 * @see ESECalendar
	 */
	private ArrayList<ESECalendar> calendarList;

	/**
	 * Contains all ESEGroups of an ESEUser. Within these groups, an
	 * ESEUser may organize Contacts (other ESEUser).
	 */
	private ArrayList<ESEGroup> groupList;

	/**
	 * Default Constructor for ESEUser.<br>
	 * Question and Answer to change password are set to default:<br>
	 * Question: "Holy solution", Answer: "42"<p>
	 * Example: ESEUser user = ("UsernameOfJack", "secretPassword", "Jack", "Sparrow");
	 * {@link #ESEUser(String, String, String, String, String, String)} is an extended constructor<br>
	 * to create an ESEUser with individual Answer and Question.
	 * The {@link ESEDatabase} organizes ESEUser to serve in application.
	 * @param username String must be unique in {@link ESEDatabase}, or an Exception is thrown.
	 * @param password String must not be empty.
	 * @param firstName First name of registered Person.
	 * @param familyName Second/Family name of registered Person.
	 * @throws ESEException Given username already exists in the ESEDatabase.
	 * @see #ESEUser(String, String, String, String, String, String)
	 * @see ESEDatabase
	 */
	public ESEUser(String username, String password, String firstName, String familyName) throws ESEException
	{
		this(username, password, firstName, familyName, "Holy solution", "42");
	}

	/**
	 * Extended constructor for ESEUser that also provides the option of choosing
	 * an own Question and Answer to reset the password.
	 * @param username String must be unique in {@link ESEDatabase}, or an Exception is thrown.
	 * @param password String must not be empty.
	 * @param firstName First name of registered Person.
	 * @param familyName Second/Family name of registered Person.
	 * @param secureQuestion A question only the user can answer.
	 * @param secureAnswer Answer to <code>secureQuestion</code>.
	 * @throws ESEException Given username already exists in the ESEDatabase.
	 */
	public ESEUser(String username, String password,
			String firstName, String familyName,
			String secureQuestion, String secureAnswer) throws ESEException
	{
//		if(username.isEmpty())
//		{
//			throw new ESEException("User name must not be empty!");
//		}

		for(ESEUser user : ESEDatabase.getAllUsers())
		{
			if(username.equals(user.getName()))
			{
				throw new ESEException("This user name is already in the database!");
			}
		}

		this.userID = idCounter++;
		this.username = username;
		this.firstName = firstName;
		this.familyName = familyName;
		this.registrationDate = new Date();

		this.calendarList = new ArrayList<ESECalendar>();
		this.groupList = new ArrayList<ESEGroup>();
		ESEGroup friends = new ESEGroup("Friends", this);
		this.groupList.add(friends);

		this.secureQuestion = secureQuestion;
		this.secureAnswer = secureAnswer;
		this.password = password;
	}

	@Override
	public void accept(Visitor visitor)
	{
		for(ESECalendar calendar : this.calendarList)
		{
			calendar.accept(visitor);
		}
		visitor.visit(this);
	}

	// Usage only intended for testing purposes
	public static void resetIdCounter()
	{
		idCounter = 0;
	}

	/**
	 * @return {@link Integer} unique userID of this ESEUser.
	 */
	public int getUserID()
	{
		return this.userID;
	}

	/**
	 * @return {@link ArrayList} of type {@link ESEGroup}. This is a
	 * shallow copy, so changes made within this ArrayList will not
	 * affect the real Data.
	 */
	public ArrayList<ESEGroup> getGroupList()
	{
		return new ArrayList<ESEGroup>(this.groupList);
	}

	/**
	 * @return {@link ArrayList} of type {@link ESECalendar}. This is a
	 * shallow copy, so changes made within this ArrayList will not
	 * affect the real Data.
	 */
	public ArrayList<ESECalendar> getCalendarList()
	{
		return new ArrayList<ESECalendar>(this.calendarList);
	}

	/**
	 * Creates an new {@link ESECalendar} for this ESEUser.<br>
	 * This is different from {@link #addCalendar(ESECalendar)} which only adds
	 * but does not create a new calendar.
	 * @param calendarName Must not be empty and unique in the users calendar list.
	 * @throws ESEException Calendar name is null or already in the calendar list.
	 * @see #addCalendar(ESECalendar) to only add but not create a new calendar.
	 */
	public void addCalendar(String calendarName) throws ESEException
	{
		this.calendarList.add(new ESECalendar(calendarName, this));
	}

	/**
	 * Creates an new {@link ESEGroup} for this ESEUser.
	 * @param groupName Must not be empty and unique for this user.
	 * @throws ESEException Group name is empty or ambiguous.
	 */
	public void addGroup(String groupName) throws ESEException
	{
		ESEGroup group = new ESEGroup(groupName, this);
		this.groupList.add(group);
	}

	/**
	 * Returns the corresponding ESECalendar of this ESEUser.
	 * @param id unique
	 * @return {@link ESECalendar} with corresponding ID of this ESEUser.
	 * @throws ESEException User has no calendar with given ID.
	 * @see #getCalendarByName(String)
	 * @see #getCalendarList()
	 */
	public ESECalendar getCalendarByID(int id) throws ESEException
	{
		for(ESECalendar calendar : this.calendarList)
		{
			if(calendar.getID() == id)
			{
				return calendar;
			}
		}
		throw new ESEException("No calendar with ID \"" + id + " \"!");
	}

	/**
	 * Returns the corresponding ESECalendar of this ESEUser.
	 * @param name of searched ESECalendar.
	 * @return {@link ESECalendar} searched.
	 * @throws ESEException No calendar of the user has this name.
	 * @see #getCalendarByID(int)
	 * @see #getCalendarList()
	 */
	public ESECalendar getCalendarByName(String name) throws ESEException
	{
		for(ESECalendar calendar : this.calendarList)
		{
			if(calendar.getCalendarName().equals(name))
			{
				return calendar;
			}
		}
		throw new ESEException("No calendar with name \"" + name + "\"!");
	}

	/**
	 * Returns the corresponding {@link ESEGroup} to this <code>ID</code>.
	 * @param id of searched ESEGroup
	 * @return {@link ESECalendar} searched.
	 * @throws ESEException User has no group with this ID.
	 * @see #getGroupByName(String)
	 * @see #getGroupList()
	 */
	public ESEGroup getGroupByID(int id) throws ESEException
	{
		for(ESEGroup group : this.groupList)
		{
			if(group.getGroupID() == id)
			{
				return group;
			}
		}
		throw new ESEException("No group with ID \"" + id + "\"!");
	}

	/**
	 * Returns the corresponding {@link ESEGroup} to this <code>name</code>.
	 * @param name of searched ESEGroup
	 * @return {@link ESECalendar} searched.
	 * @throws ESEException User has no group with given name.
	 * @see #getGroupByID(int)
	 * @see #getGroupList()
	 */
	public ESEGroup getGroupByName(String name) throws ESEException
	{
		for(ESEGroup group : this.groupList)
		{
			if(group.getGroupName().equals(name))
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
	 * Use {@link #getAllPublicEvents(int)} to get only public ESEEvents of this ESECalendar.
	 * @param calendarID ID of searched calendar.
	 * @return {@link ArrayList} of type ESEEvent.
	 * @throws ESEException User has no calendar with given ID.
	 * @see #getAllPublicEvents(int)
	 * @see ESEEvent
	 */
	public ArrayList<ESEEvent> getAllEvents(int calendarID) throws ESEException
	{
		for(ESECalendar calendar : this.calendarList)
		{
			if(calendar.getID() == calendarID)
			{
				return calendar.getAllEvents();
			}
		}
		throw new ESEException("No calendar with ID \"" + calendarID + "\"!");
	}

	/**
	 * Returns all public ESEEvents of the ESECalendar with the corresponding ID.<br>
	 * Use {@link #getAllPublicEvents(int)} to get only public ESEEvents of this ESECalendar.
	 * @param calendarID ID to search for.
	 * @return {@link ArrayList} of type ESEEvent.
	 * @throws ESEException Calendar with given ID is not found in the list.
	 * @see #getAllPublicEvents(int)
	 * @see ESEEvent
	 */
	public ArrayList<ESEEvent> getAllPublicEvents(int calendarID) throws ESEException
	{
		for(ESECalendar calendar : this.calendarList)
		{
			if(calendar.getID() == calendarID)
			{
				return calendar.getAllPublicEvents();
			}
		}
		throw new ESEException("No calendar with this ID \"" + calendarID + "\"!");
	}

	/**
	 * Adds an ESECalendar to this ESEUsers Calendar List.<br>
	 * @param calendarToAdd owner must be this.
	 * @throws ESEException User is not owner of the calendar.
	 * @see #addCalendar(String)
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
	 * Creates and adds a new Group to the list of this ESEUser.<br>
	 * @param groupToAdd String must not be empty.
	 * @throws ESEException User is not owner of the group.
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
	 * @param newPassword String must not be empty.
	 * @see #setAnswer(String)
	 * @see #setQuestion(String)
	 */
	public void setPassword(String newPassword)
	{
		this.password = newPassword;
	}

	/**
	 * Replaces the old question with a new secret question.
	 * If changed, the <code>secretAnswer</code> should also be changed to relate to the new question.
	 * @param newQuestion String must not be empty.
	 * @see #setAnswer(String)
	 */
	public void setQuestion(String newQuestion)
	{
		this.secureQuestion = newQuestion;
	}

	/**
	 * Replaces the old answer with a new one.
	 * The answer should relate to the current <code>secretQuestion</code>.
	 * @param newAnswer String must not be empty.
	 * @see #setQuestion(String)
	 */
	public void setAnswer(String newAnswer)
	{
		this.secureAnswer = newAnswer;
	}

	/**
	 * @return {@link String} username of this ESEUser, specified by its profile.
	 */
	public String getName()
	{
		return this.username;
	}

	/**
	 * @return {@link String} password of this ESEUser.
	 */
	public String getPassword()
	{
		return this.password;
	}

	/**
	 * @return {@link String} firstName of this ESEUser.
	 */
	public String getFirstName()
	{
		return this.firstName;
	}

	/**
	 * @return {@link String} familyName of this ESEUser.
	 */
	public String getFamilyName()
	{
		return this.familyName;
	}

	/**
	 * @return birthday of this ESEUser.
	 */
	public Date getBirthday()
	{
		return this.birthday;
	}

	/**
	 * @return {@link String} birthday representation of this profile
	 * with the format specified in {@link ESEConversionHelper#convertBirthdayDateToString(Date)}.<br>
	 * If not initialized "-" is returned instead.
	 */
	public String getBirthdayString()
	{
		return ESEConversionHelper.convertBirthdayDateToString(this.birthday);
	}

	/**
	 * @return Date registrationDate of this profile.
	 */
	public Date getRegistrationDate()
	{
		return this.registrationDate;
	}

	/**
	 * @return {@link String} registrationDate representation of this profile.
	 */
	public String getRegistrationDateString()
	{
		return ESEConversionHelper.convertDateToString(this.registrationDate);
	}

	/**
	 * @return {@link String} eMail of this profile.
	 * If not initialized, the default value is "-".
	 */
	public String getMail()
	{
		return this.eMail;
	}

	/**
	 * @return {@link String} street of this profile.
	 * If not initialized, the default value is "-".
	 */
	public String getStreet()
	{
		return this.street;
	}

	/**
	 * @return {@link String} city of this profile.
	 * If not initialized, the default value is "-".
	 */
	public String getCity()
	{
		return this.city;
	}

	/**
	 * @return {@link String} postCode of this profile.
	 * If not initialized, the default value is "-".
	 */
	public String getPostCode()
	{
		return this.postCode;
	}

	/**
	 * @return {@link String} stateMessage of this profile.
	 * If not initialized, the default value is "hi there!".
	 */
	public String getStateMessage()
	{
		return this.stateMessage;
	}

	/**
	 * Sets the stateMessage to a new value.<br>
	 * The default value of stateMessage when a new profile is created is: "hi there!".<p>
	 * Example of new message:<br>
	 * stateMessage = "I'm at home and it is raining."
	 * @param stateMessage the message to be set.
	 */
	public void setStateMessage(String stateMessage)
	{
		this.stateMessage=stateMessage;
	}

	/**
	 * Sets the username to a new value.<br>
	 * @param username to be set. Must not be empty.
	 */
	public void setUsername(String username)
	{
		this.username = username;
	}

	/**
	 * Sets the firstName to a new value.<br>
	 * The default value by initialization is "-".
	 * @param firstName to be set.
	 */
	public void setFirstName(String firstName)
	{
		this.firstName = firstName;
	}

	/**
	 * Sets the familyName to a new value.<br>
	 * The default value by initialization is "-".
	 * @param familyName to be set.
	 */
	public void setFamilyName(String familyName)
	{
		this.familyName = familyName;
	}

	/**
	 * Sets birthday to the right Date.
	 * @param birthday {@link Date} of birth of related ESEUser.
	 * @see #setBirthday(String) to set by String.
	 */
	public void setBirthday(Date birthday)
	{
		this.birthday = birthday;
	}

	/**
	 * Sets birthday to the right Date.
	 * @param birthday The String must have the following format: "dd.MM.yyyy".
	 * @see #setBirthday(Date) to set by Date.
	 */
	public void setBirthday(String birthday)
	{
		Date birthdayDate = ESEConversionHelper.convertBirthdayStringToDate(birthday);
		this.setBirthday(birthdayDate);
	}

	/**
	 * Sets eMail to a new value.<br>
	 * The default value by initialization is "-".
	 * @param eMail to be set.
	 */
	public void setMail(String eMail)
	{
		this.eMail = eMail;
	}

	/**
	 * Sets street to a new value.<br>
	 * The default value by initialization is "-".
	 * @param street to be set.
	 */
	public void setStreet(String street)
	{
		this.street = street;
	}

	/**
	 * Sets city to a new value.<br>
	 * The default value by initialization is "-".
	 * @param city to be set.
	 */
	public void setCity(String city)
	{
		this.city = city;
	}

	/**
	 * Sets postCode to a new value.<br>
	 * The default value by initialization is "-".
	 * @param postCode to be set.
	 */
	public void setPostCode(String postCode)
	{
		this.postCode = postCode;
	}

	/**
	 * @return {@link String} that contains the Question.
	 */
	public String getQuestion()
	{
		return this.secureQuestion;
	}

	/**
	 * @return {@link String} that contains the Answer.
	 */
	public String getAnswer()
	{
		return this.secureAnswer;
	}

	@Override
	public String toString()
	{
		return this.getName();
	}
}
