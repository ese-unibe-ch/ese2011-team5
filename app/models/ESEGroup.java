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
 * The responsibility of the ESEGroup is, to organize <b>Contancts</b> (other {@link ESEUser}s) 
 * known by the <b>Owner</b>.<br>
 * An <b> Owner</b> can have several different ESEGroups.
 * A <b>Contact</b> may be in more than one ESEGroup.
 */
public class ESEGroup
{
	/**
	 * static counter to determine uniquely groupId.<br>
	 * Increased after initialization of an new ESEGroup.
	 */
	private static int idCounter = 0;
	/**
	 * Number to distinct different ESEGroup within one application.
	 */
	private int groupID;
	/**
	 * String name by which this ESEGroup is associated.<br> It might be
	 * that different ESEGroups have the same <code>groupename</code>. To
	 * distinguish use {@link #groupID}.
	 */
	private String groupname;
	/**
	 * The {@link ESEUser} to whom this ESEGroup belongs.
	 */
	private ESEUser owner;
	private ArrayList<ESEUser> userList = new ArrayList<ESEUser>();

	/**
	 * Constructor for ESEGroup.<br>
	 * Creates an new Group that belongs to one ESEUser. 
	 * The Group is initially created empty. Contacts ({@link ESEUser}) have to be added later.
	 * 
	 * @param groupname String name of this group. Must not be empty.
	 * @param owner ESEUser that owns this group. Must not be <code>null</code>
	 * 
	 * @throws ESEException
	 * 
	 * @see {@link ESEUser}
	 * @see {@link #addUserToGroup(ESEUser)}
	 */
	public ESEGroup(String groupname, ESEUser owner) throws ESEException
	{
		if(groupname.isEmpty())
		{
			throw new ESEException("Group name must not be empty!");
		}
		if(owner == null)
		{
			throw new ESEException("Group is not assigned to any user!");
		}

		for(ESEGroup group : owner.getGroupList())
		{
			if(groupname.equals(group.getGroupName()))
			{
				throw new ESEException("This group is already in the database!");
			}
		}

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
	 * @return int {@link #groupID} of this ESEGroup. The value is ranged
	 * between 0 and the current value of {@link #idCounter}.
	 */
	public int getGroupID()
	{
		return this.groupID;
	}
	/**
	 * @return String {@link #groupname} of this ESEGroup. 
	 */
	public String getGroupName()
	{
		return this.groupname;
	}
	/**
	 * @return ESEUser {@link #owner} of this ESEGroup. 
	 */
	public ESEUser getOwner()
	{
		return this.owner;
	}
	/**
	 * @return ArrayList<ESEUser> {@link #userList} of this ESEGroup.
	 * This lists contains all Contacts (ESEUser) that are part of this group.
	 */
	public ArrayList<ESEUser> getUserList()
	{
		return new ArrayList<ESEUser>(this.userList);
	}
	/**
	 * Adds a Contact ({@link ESEUser}) to the group.
	 * 
	 * @param user to be added.
	 * @throws ESEException if the user is already part of this group.
	 */
	public void addUserToGroup(ESEUser user) throws ESEException
	{
		if (!this.userList.contains(user))
		{
			this.userList.add(user);
			return;
		}
		throw new ESEException("This user is already in the group!");
	}
	/**
	 * Removes a Contact ({@link ESEUser}) from this group.
	 * 
	 * @param user to be removed.
	 * @throws ESEException if user is not in the list.
	 */
	public void removeUserFromGroup(ESEUser user) throws ESEException
	{
		if (this.userList.contains(user))
		{
			this.userList.remove(user);
			return;
		}
		throw new ESEException("No such user exists!");
	}

	public boolean userAlreadyInGroup(String userName)
	{
		try 
		{
			ESEUser user=ESEDatabase.getUserByName(userName);
			return this.userList.contains(user);
			
		} 
		catch (ESEException e) 
		{
			return false;
		}
		
		
		
		
	}
}
