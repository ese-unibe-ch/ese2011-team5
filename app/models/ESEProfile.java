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
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
/**
 * A ESEProfile always relates to exactly one ESEUser. A profile describes different
 * attributes of an ESEUser, such as names, birth date and current state.<br>
 * ESEProfile contains almost no logic since all its responsibilities lies by 
 * setting and getting values.
 * 
 * @see ESEUser
 * @see ESEState
 * 
 */
public class ESEProfile
{
	/**
	 * static counter to determine uniquely profilId.<br>
	 * Increased after initialization of an new ESEProfile.
	 */
	private static int idCounter = 0;

	/**
	 * Number to distinct different ESEProfile within one application.
	 */
	private int profileID;
	/**
	 * The {@link ESEUser} to whom this ESEProfile relates.
	 */
	private ESEUser owner;
	/**
	 * Date on which the {@link #owner} registered.
	 */
	private Date registrationDate;
	/**
	 * Message that is being displayed to other ESEUsers.
	 */
	private String stateMessage = "Hi there!";
	/**
	 * Describes whether the {@link #owner} is online or offline.
	 * @see ESEState
	 */
	private ESEState state = ESEState.OFFLINE;

	/*Detailed information about the related ESEUser*/
	private String username;
	private String firstName = "-";
	private String familyName = "-";

	private Date birthday;

	private String eMail = "-";

	private String street = "-";
	private String city = "-";
	private String postCode = "-";
	
	/**
	 * Default constructor to a ESEProfile. <br>
	 * All String values, that are not passed as parameters, are set to default: "-".
	 * The registrationDate is set to the current time.
	 * 
	 * @param owner {@link ESEUser} that relates to this ESEProfile.
	 * @param username {@link String} to login. Must not be empty.
	 * @param firstName {@link String}
	 * @param familyName {@link String}
	 * 
	 * @throws ESEException thrown if username is empty, or owner <code>null</code>.
	 * 
	 * @see {@link ESEUser}
	 */
	public ESEProfile(ESEUser owner, String username, String firstName, String familyName) throws ESEException
	{
		if(username.isEmpty())
		{
			throw new ESEException("Profile user name must not be empty!");
		}
		if(owner == null)
		{
			throw new ESEException("Profile is not assigned to any user!");
		}

		this.profileID = idCounter++;
		this.owner = owner;
		this.username = username;
		this.firstName = firstName;
		this.familyName = familyName;

		this.registrationDate = new Date();
	}
	/**
	 * @return int {@link #profileID} of this ESEProfil. The number ranges 
	 * from 0 to the current {@link #idCounter}.
	 */
	public int getID()
	{
		return this.profileID;
	}
	/**
	 * @return ESEUser {@link #owner} of this ESEprofile.
	 */
	public ESEUser getOwner()
	{
		return this.owner;
	}
	/**
	 * @return String {@link #username} of this ESEProfile. The returned String is never empty.
	 */
	public String getUsername()
	{
		return username;
	}
	/**
	 * @return String {@link #firstName} of this ESEProfile. If not initialized,
	 *  the default value is "-".
	 */
	public String getFirstName()
	{
		return firstName;
	}
	/**
	 * @return String {@link #familyName} of this ESEProfile. If not initialized,
	 *  the default value is "-".
	 */
	public String getFamilyName()
	{
		return familyName;
	}
	/**
	 * @return Date {@link #birthday} of this ESEProfile. If not initialized, 
	 * a {@link RuntimeException} is thrown.
	 */
	public Date getBirthday()
	{
		return birthday;
	}
	/**
	 * @return String {@link #birthday} representation of this ESEProfile, 
	 * with the following format: "dd.MM.yyyy HH:mm".<br>
	 * If not initialized, a {@link RuntimeException} is thrown.
	 */
	public String getBirthdayString()
	{
		return ESEConversionHelper.convertBirthdayDateToString(this.birthday);
	}
	/**
	 * @return Date {@link #registrationDate} of this ESEProfile.
	 */
	public Date getRegistrationDate()
	{
		return this.registrationDate;
	}
	/**
	 * @return String {@link #registrationDate} representation of this ESEProfile.
	 */
	public String getRegistrationDateString()
	{
		return ESEConversionHelper.convertDateToString(this.registrationDate);
	}
	/**
	 * @return String {@link #eMail} of this ESEProfile.
	 * If not initialized, the default value is "-".
	 */
	public String getMail()
	{
		return eMail;
	}
	/**
	 * @return String {@link #street} of this ESEProfile. 
	 * If not initialized, the default value is "-".
	 */
	public String getStreet()
	{
		return street;
	}
	/**
	 * @return String {@link #city} of this ESEProfile. 
	 * If not initialized, the default value is "-".
	 */
	public String getCity()
	{
		return city;
	}
	/**
	 * @return String {@link #postCode} of this ESEProfile. 
	 * If not initialized, the default value is "-".
	 */
	public String getPostCode()
	{
		return postCode;
	}
	/**
	 * @return String {@link #state} of this ESEProfile. 
	 * If not initialized, the default value is "-".
	 */
	public String getState()
	{
		return this.state.toString();
	}
	/**
	 * @return String {@link #stateMessage} of this ESEProfile. 
	 * If not initialized, the default value is "hi there!".
	 */
	public String getStateMessage()
	{
		return this.stateMessage;
	}
	/**
	 * Set the {@link #stateMessage} to a new value.<br>
	 * The default value of stateMessage when a new ESEProfile is 
	 * created is: "hi there!". <p>
	 * Example of new message: <br>
	 * stateMessage = "I'm at home and it is raining."
	 * 
	 * @param stateMessage the message to be set.
	 */
	public void setStateMessage(String stateMessage)
	{
		this.stateMessage=stateMessage;
	}
	/**
	 * Set the {@link #username} to a new value.<br>
	 * 
	 * @param username to be set. Must not be empty.
	 */
	public void setUsername(String username)
	{
		this.username = username;
	}
	/**
	 * Set the {@link #firstName} to a new value.<br> 
	 * The default value by initialization is "-".
	 * 
	 * @param firstName to be set.
	 */
	public void setFirstName(String firstName)
	{
		this.firstName = firstName;
	}
	/**
	 * Set the {@link #familyName} to a new value. <br>
	 * The default value by initialization is "-".
	 * 
	 * @param familyName to be set.
	 */
	public void setFamilyName(String familyName)
	{
		this.familyName = familyName;
	}
	/**
	 * Set {@link #birthday} to the right Date.
	 * @param birthday {@link Date} of birth of related ESEUser.
	 * @see {@link #setBirthday(String)} to set by String.
	 */
	public void setBirthday(Date birthday)
	{
		this.birthday = birthday;
	}
	/**
	 * Set {@link #birthday} to the right Date.
	 * 
	 * @param birthday <br>The String must have the following format: "dd.MM.yyyy HH:mm".
	 * @see {@link #setBirthday(Date)} to set by Date.
	 */	
	public void setBirthday(String birthday)
	{
		Date birthdayDate = ESEConversionHelper.convertBirthdayStringToDate(birthday);
		this.setBirthday(birthdayDate);
	}
	/**
	 * Set {@link #eMail} to a new value.<br>
	 * The default value by initialization is "-".
	 * @param eMail to be set.
	 */
	public void setMail(String eMail)
	{
		this.eMail = eMail;
	}
	/**
	 * Set {@link #street} to a new value.<br>
	 * The default value by initialization is "-".
	 * @param street to be set.
	 */
	public void setStreet(String street)
	{
		this.street = street;
	}
	/**
	 * Set {@link #city} to a new value.<br>
	 * The default value by initialization is "-".
	 * @param city to be set.
	 */
	public void setCity(String city)
	{
		this.city = city;
	}
	/**
	 * Set {@link #postCode} to a new value.<br>
	 * The default value by initialization is "-".
	 * @param postCode to be set.
	 */
	public void setPostCode(String postCode)
	{
		this.postCode = postCode;
	}
	/**
	 * Changes the state of this ESEProfile.<br>
	 * A state can either be <code>online</code> or <code>offline</code>.
	 * 
	 * @param state to change into.
	 * @see ESEState
	 */
	public void changeState(ESEState state)
	{
		this.state=state;
	}
}
