/*
 * Project: ESECalendar team 5
 * Authors:
 * 		Rafael Breu
 * 		Renato Corti
 * 		Lukas Keller
 */

package models;

import java.util.ArrayList;
/**
 * An ESEGroup is always owned by a certain ESEUser (referred as <b>Owner</b>).
 * The responsibility of the ESEGroup is, to organise <b>Contacts</b> consisting
 * of other ESEUsers known by the <b>Owner</b>.<br>
 * An <b>Owner</b> can have several different ESEGroups.
 * A <b>Contact</b> may be in more than one ESEGroup.
 */
public class ESEGroup
{
	/**
	 * Static counter to determine uniquely groupID.<br>
	 * Increased after initialization of an new ESEGroup.
	 */
	private static int idCounter = 0;
	/**
	 * Number to distinct different ESEGroup within one application.
	 */
	private int groupID;
	/**
	 * String name by which this ESEGroup is associated.<br>
	 * It might be that different ESEGroups have the same <code>groupname</code>.
	 * To distinguish use {@link #groupID}.
	 */
	private String groupname;
	/**
	 * The {@link ESEUser} to whom this ESEGroup belongs.
	 */
	private ESEUser owner;

	/**
	 * List containing {@link ESEUser} that are part of this group.
	 */
	private ArrayList<ESEUser> userList = new ArrayList<ESEUser>();

	/**
	 * Constructor for ESEGroup.<br>
	 * Creates an new group that belongs to one ESEUser. 
	 * The group is initially created empty. Contacts ({@link ESEUser}) have to be added later.
	 * @param groupname String name of this group. Must not be empty.
	 * @param owner ESEUser that owns this group. Must not be <code>null</code>.
	 * @throws ESEException No group name or no owner given.
	 * @see ESEUser
	 * @see #addUserToGroup(ESEUser)
	 */
	public ESEGroup(String groupname, ESEUser owner) throws ESEException
	{
		checkGroupName(groupname, owner);

		this.groupID = idCounter++;
		this.groupname = groupname;
		this.owner = owner;
	}

	// Usage only intended for testing purposes
	public static void resetIdCounter()
	{
		idCounter = 0;
	}

	/**
	 * @return int {@code #groupID} of this ESEGroup. The value is ranged
	 * between 0 and the current value of {@code #idCounter}.
	 */
	public int getGroupID()
	{
		return this.groupID;
	}

	/**
	 * @return String {@code #groupname} of this ESEGroup. 
	 */
	public String getGroupName()
	{
		return this.groupname;
	}

	/**
	 * @return ESEUser {@code #owner} of this ESEGroup. 
	 */
	public ESEUser getOwner()
	{
		return this.owner;
	}

	/**
	 * @return ArrayList<ESEUser> {@code #userList} of this ESEGroup.
	 * This lists contains all Contacts (ESEUser) that are part of this group.
	 */
	public ArrayList<ESEUser> getUserList()
	{
		return new ArrayList<ESEUser>(this.userList);
	}

	/**
	 * Adds a Contact ({@link ESEUser}) to the group.
	 * @param user to be added.
	 * @throws ESEException if the user is already part of this group.
	 */
	public void addUserToGroup(ESEUser user) throws ESEException
	{
		if(this.userList.contains(user))
		{
			throw new ESEException("This user \"" + user + "\" is already in the group!");
		}
		this.userList.add(user);
	}

	/**
	 * Removes a Contact ({@link ESEUser}) from this group.
	 * @param user to be removed.
	 * @throws ESEException if user is not in the list.
	 */
	public void removeUserFromGroup(ESEUser user) throws ESEException
	{
		if(!this.userList.contains(user))
		{
			throw new ESEException("No such user \"" + user + "\" exists!");
		}
		this.userList.remove(user);
	}

	/** Checks whether a certain {@link ESEUser} is already in this group.
	 * @param userName User to test for group membership.
	 * @return {@code Boolean} {@code true} if the user is already in this group, {@code false} otherwise.
	 */
	public boolean userAlreadyInGroup(String userName)
	{
		try
		{
			ESEUser user = ESEDatabase.getUserByName(userName);
			return this.userList.contains(user);
		}
		catch(ESEException e)
		{
			// This user is not even in the database
			return false;
		}
	}

	/** Setter for group name.
	 * @param newGroupname New name of this group. Must not be empty.
	 * @throws ESEException If group name or owner is missing or null.
	 */
	public void setGroupName(String newGroupname) throws ESEException
	{
		checkGroupName(newGroupname, this.owner);
		this.groupname = newGroupname;
	}

	private void checkGroupName(String newGroupName, ESEUser groupOwner) throws ESEException
	{
		if(newGroupName.isEmpty())
		{
			throw new ESEException("Group name must not be empty!");
		}

		if(groupOwner == null)
		{
			throw new ESEException("Group is not assigned to any user!");
		}

//		for(ESEGroup group : owner.getGroupList())
//		{
//			if(groupname.equals(group.getGroupName()))
//			{
//				throw new ESEException("This group is already in the database!");
//			}
//		}
	}
}
