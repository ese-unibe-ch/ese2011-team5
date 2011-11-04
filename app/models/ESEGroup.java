package models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

import play.data.validation.Required;
import play.db.jpa.Model;

/** Class to manage groups of users.
 * @see ESEUser
 */
@Entity
public class ESEGroup extends Model {
	/** Maps entity relations from a group to an user.
	 */
	@ManyToOne
	public ESEUser owner;
	/** Name of the group displayed in the group list.
	 */
	@Required
	public String groupName;
	/** List of all groups from an user.
	 */
	public ArrayList<ESEUser> userList;

	/** Constructor for new groups to add to an user. This constructor is not intended for direct use. Instead use {@link ESEFactory} for this.
	 * @param groupName Name of the group list
	 * @param owner User that will have this group
	 * @see ESEUser
	 */
	public ESEGroup(String groupName, ESEUser owner) {
		this.userList = new ArrayList<ESEUser>();
		this.groupName = groupName;
		this.owner = owner;

	}

	/** Changes the group name.
	 * @param groupName New name for the group
	 */
	public void editGroupName(@Required String groupName) {
		// TODO: check string is not empty
		this.groupName = groupName;
	}

	/** Adds an existing user to the group.
	 * @param userID The Id the user was assigned when added to the database
	 * @see ESEUser
	 */
	public void addUser(@Required long userID) {
		ESEUser user = ESEUser.findById(userList);

		boolean valid = !this.userList.contains(user) && user != null;

		if (valid) {
			this.userList.add(user);
			user.save();
			this.save();
		} else {
			// TODO Throw an exception or do something similar, because user is
			// already in this group or does not exist
		}
	}

	/** Adds an existing user to the group.
	 * @param userName Name of the user that should be added to this group
	 * @see ESEUser
	 */
	public void addUser(@Required String userName) {
		ESEUser user = ESEUser.find("byUsername", userName).first(); // LK @
																		// TEAM_TEST
																		// test
																		// this!

		boolean valid = !this.userList.contains(user) && user != null;
		if (valid) {
			this.userList.add(user);
			this.owner.save();
			//this.save();
		} else {
			// TODO Throw an exception or do something similar, because user is
			// already in this group or does not exist
		}
	}

	/** Removes an user from the group list.
	 * @param userName Name of the user that should be removed from the list
	 * @see ESEUser
	 */
	public void removeUser(@Required String userName) {
		ESEUser user = ESEUser.find("byUsername", userName).first();

		this.userList.remove(user); // TODO (Maybe) Throw an exception or do
									// something similar, if the user does not
									// exist in the list
	}

	/** Removes an user from the group list.
	 * @param userID The Id the user was assigned when added to the database
	 * @see ESEUser
	 */
	public void removeUser(@Required long userID) {
		ESEUser user = ESEUser.findById(userID);

		this.userList.remove(user); // TODO (Maybe) Throw an exception or do
									// something similar, if the user does not
									// exist in the list
	}

	/** Returns a {@link List} of all users in the list.
	 * @return {@link List} which contains all users in this list.
	 */
	public List<ESEUser> getAllUser() {
		return this.userList;
	}

	/** Searches a group in the database.
	 * @param id The Id the group was assigned when added to the database
	 * @return {@link ESEGroup} with the corresponding Id
	 */
	public static ESEGroup findGroupById(long id)
	{
		return findById(id);
	}

	/** Determines if a group already contains an user.
	 * @param userID The Id the user was assigned when added to the database
	 * @return {@link Boolean} value if the user is already in the group
	 */
	public boolean isUserInGroup(@Required long userID) {
		ESEUser user = ESEUser.findById(userID);
		return this.userList.contains(user);
	}

	/** Determines if an user is already in a group.
	 * @param userName Name of the user to test
	 * @return {@link Boolean} value if the user is already in the group
	 */
	public boolean isUserInGroup(@Required String userName) {
		ESEUser user = ESEUser.find("byUsername", userName).first();
		return this.userList.contains(user);
	}

	/** Returns the name of the group.
	 * @return Name of the group
	 */
	public String getGroupName() {
		return this.groupName;
	}
}
