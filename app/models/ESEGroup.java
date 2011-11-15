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
 * TODO
 *
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

	public int getGroupID()
	{
		return this.groupID;
	}

	public String getGroupName()
	{
		return this.groupname;
	}

	public ESEUser getOwner()
	{
		return this.owner;
	}

	public ArrayList<ESEUser> getUserList()
	{
		return new ArrayList<ESEUser>(this.userList);
	}

	public void addUserToGroup(ESEUser user) throws ESEException
	{
		if (!this.userList.contains(user))
		{
			this.userList.add(user);
			return;
		}
		throw new ESEException("This user is already in the group!");
	}

	public void removeUserFromGroup(ESEUser user) throws ESEException
	{
		if (this.userList.contains(user))
		{
			this.userList.remove(user);
			return;
		}
		throw new ESEException("No such user exists!");
	}
}
