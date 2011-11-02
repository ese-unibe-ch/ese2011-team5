package models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import controllers.Secure;

import play.data.validation.Required;
import play.db.jpa.Model;

/** Provides methods to create, query, edit and remove calendars, groups and user profiles.
 * @see ESECalendar
 * @see ESEEvent
 * @see ESEGroup
 */
@Entity
public class ESEUser extends Model {
	/** An username is chosen at registration or at login time. It identifies the user and has to be unique.
	 */
	@Required
	public String username;
	/** The password is linked with an {@link #username} and has to be provided at registration or is used to authenticate an user at login time. It is always a good idea to keep it secret and change it on a regular basis.
	 */
	@Required
	public String password;

	/** The list of calendars the user has available.
	 * @see ESECalendar
	 */
	@OneToMany(mappedBy = "owner", cascade = CascadeType.ALL)
	public List<ESECalendar> calendarList;
	/** List of groups of the user.
	 * @see ESEGroup
	 */
	@OneToMany(mappedBy = "owner", cascade = CascadeType.ALL)
	public List<ESEGroup> groupList;
	/** The first name of the user displayed in the profile.
	 */
	public String firstName = "";
	/** The family name of the user displayed in the profile.
	 */
	public String familyName = "";

	/** Constructor to create a new user. This constructor is not intended for direct use. Instead use </code>ESEFactory</code> for this.
	 * @param username Name to log in and visible to other users
	 * @param password Ideally a string of random characters not known to anyone except the user
	 * @param firstName First name of the user
	 * @param familyName Family name of the user
	 */
	public ESEUser(String username, String password, String firstName,
			String familyName) {
		this.username = username;
		this.password = password;

		this.firstName = firstName;
		this.familyName = familyName;

		this.initialize();
	}

	/** Constructor to create a new user. This constructor is not intended for direct use. Instead use </code>ESEFactory</code> for this.
	 * @param username Name to log in and visible to other users
	 * @param password Ideally a string of random characters not known to anyone except the user
	 */
	public ESEUser(String username, String password) {
		this(username, password, "", "");

		this.initialize();
	}

	// --------------------- //
	// GETTERS METHODS //
	// -------------------- //

	// STATIC METHODS:
	/** Returns a list of all users registered in the database unless the user mentioned.
	 * @param username Name of the current user, it will be omitted from the list
	 * @return {@link List} with the users
	 */
	public static List<ESEUser> getAllOtherUsers(String username) {
		List<ESEUser> allUsers = ESEUser.findAll();
		ESEUser user = ESEUser.find("byUsername", username).first();

		allUsers.remove(user);

		return allUsers;
	}

	/** Provides all groups the user {@link #username} has available.
	 * @param username The user from which to retrieve all groups
	 * @return {@link List} of the groups
	 * @see ESEGroup
	 */
	public static List<ESEGroup> getGroupsOfUser(String username) {
		ESEUser user = ESEUser.find("byUsername", username).first();

		return user.getMyGroups();
	}

	/** Searches an user in the database.
	 * @param user Username to find
	 * @return The user with the matching username
	 */
	public static ESEUser getUser(String user) {
		return find("byUsername", user).first();
	}

	/** Searches an user in the database.
	 * @param id The Id the user was assigned when added to the database
	 * @return Matching user
	 */
	public static ESEUser findUserById(long id)
	{
		return findById(id);
	}

	// "THIS" GETTERS:

	/** Returns the name used to login as this user.
	 * @return Login name of the user
	 */
	public String getUsername() {
		return this.username;
	}

	/** Returns list of all calendars from user.
	 * @return {@link List} of all calendars from this users
	 * @see ESECalendar
	 */
	public List<ESECalendar> getAllCalendars() {
		return this.calendarList;
	}

	/**
	 *	XXX: new routing needs something like this..
	 */
	public ESECalendar getCalendar(String name) {
		for (ESECalendar c : calendarList) {
			if (name.equals(c.getCalendarName())) {
				return c;
			}
		}
		return null;
	}

	/** Returns list of all groups from user.
	 * @return {@link List} of all groups this user has
	 * @see ESEGroup
	 */
	public List<ESEGroup> getMyGroups() {
		return this.groupList;
	}

	/** Returns group with the given group name.
	 * @param groupName The name of the group
	 * @return Group with matching name
	 * @see ESEGroup
	 */
	public ESEGroup getGroup(String groupName) {
		for (ESEGroup group : this.groupList) {
			if (group.getGroupName().equals(groupName)) {
				return group;
			}
		}
		// TODO LK: Throw an exception
		return null;
	}

	/** Verify if the given password equals the current user password.
	 * @param p Password to compare with
	 * @return Result of the comparison
	 */
	public boolean validatePassword(String p) {
		return p.equals(password);
	}

	// --------------------- //
	// CREATE/REMOVE METHODS //
	// -------------------- //

	/** Create new calendar for this user.
	 * @param calendarName Name for the calendar to be created, it does not have to be unique
	 * @see ESECalendar
	 */
	public void createCalendar(@Required String calendarName) {
		//this.validateNewCalendar(calendarName);
		ESECalendar calendar = ESEFactory.createCalendar(calendarName, this);

		this.calendarList.add(calendar);
	}

	/** Removes a calendar from the calendar list of the user.
	 * @param calendarID The Id the calendar was assigned when added to the database
	 * @see ESECalendar
	 */
	public void removeCalendar(@Required long calendarID) {
		//TODO: Check if calendar is empty
		ESECalendar calendar = ESECalendar.findById(calendarID); // LK @
																	// TEAM_TEST:
																	// test this
		this.calendarList.remove(calendar);
		calendar.delete(); // DB stuff
		this.save();
	}

	/** Creates a new group for the user.
	 * @param groupName The name the new group should have, it has to be unique
	 * @see ESEGroup
	 */
	public void createGroup(@Required String groupName) {
		this.validateNewGroup(groupName);

		ESEGroup group = ESEFactory.createGroup(groupName, this);
		this.groupList.add(group);
		this.save();
	}

	/** Removes a group from the user.
	 * @param groupName Name of the group that should be removed
	 * @see ESEGroup
	 */
	public void removeGroup(@Required String groupName) {
		//TODO: Check if group is empty
		ESEGroup group = ESEGroup.find("byGroupNameAndUsername", groupName,
				username).first(); // TODO:
		// LK
		// @
		// TEAM_TEST:
		// Test
		// this!

		this.groupList.remove(group);
		group.delete();
		this.save();
	}

	// --------------- //
	// EDIT METHODS //
	// --------------- //

	/** Changes password from user.
	 * @param password The new password for the user
	 */
	public void editPassword(@Required String password) {
		if (!password.equals("")) {
			this.password = password;
			this.save();
		} else {
			// TODO Throw an exception or do something similar
		}
	}

	/** Changes family name from user.
	 * @param familyName New family name displayed in user profile
	 */
	public void editFamilyName(@Required String familyName) {
		if (!familyName.equals("")) {
			this.familyName = familyName;
			this.save();
		} else {
			// TODO Throw an exception or do something similar
		}
	}

	/** Changes first name from user.
	 * @param firstName New first name displayed in user profile
	 */
	public void editFirstName(@Required String firstName) {
		if (!firstName.equals("")) {
			this.firstName = firstName;
			this.save();
		} else {
			// TODO Throw an exception or do something similar
		}
	}

	/** Adds an user to a group.
	 * @param userName The user that should be added to a group
	 * @param groupName The group to which the user should be added
	 * @see ESEGroup
	 */
	public void addUserToGroup(@Required String userName,
			@Required String groupName) {
		ESEGroup group = this.getGroup(groupName);
		if(group == null)
		{
			// TODO Throw an exception or do something similar
		}

		if(userName.equals(group.owner))
		{
			// TODO Throw an exception or do something similar
		}
		if(this.groupList.contains(userName))
		{
			// TODO Throw an exception or do something similar
		}
		group.addUser(userName);
	}

	// --------------- //
	// PRIVATE METHODS //
	// --------------- //

	private void initialize() {
		this.calendarList = new ArrayList<ESECalendar>();
		this.groupList = new ArrayList<ESEGroup>();

		ESEGroup groupFriends = ESEFactory.createGroup("Friends", this);
		System.out.println("----------OK BIS HIER--------");
		this.groupList.add(groupFriends);
	}

	/*
	private void validateNewCalendar(String calendarName) {
		// TODO check if a calendar with the name calendarName already
		// exists
	}
	*/

	private void validateNewGroup(String groupName) {
		// TODO check if a group with the name groupName already
		// exists
	}
}
