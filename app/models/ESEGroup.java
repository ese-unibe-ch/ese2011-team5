package models;

import java.util.ArrayList;

public class ESEGroup
{

	private static int idCounter = 0;

	private int groupID;
	private String groupname;
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

	public static void resetIdCounter()
	{
		idCounter = 0;
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
