package models;

import java.util.ArrayList;

public class ESEGroup
{

	private static int idCounter = 0;

	private int groupID;
	private String groupname;
	private ESEUser owner;
	private ArrayList<ESEUser> userList = new ArrayList<ESEUser>();

	public ESEGroup(String groupname, ESEUser owner)
	{
		assert(groupname != "");
		assert(owner != null);

		for(ESEGroup group : owner.getGroupList())
		{
			if(groupname.equals(group.getGroupname()))
			{
				throw new IllegalArgumentException("This group is already in the database");
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

	public String getGroupname()
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

	public void addUserToGroup(ESEUser user)
	{
		if (!this.userList.contains(user))
		{
			this.userList.add(user);
			return;
		}
		throw new IllegalArgumentException("This user is already in the group");
	}

	public void removeUserFromGroup(ESEUser user)
	{
		if (this.userList.contains(user))
		{
			this.userList.remove(user);
			return;
		}
		throw new IllegalArgumentException("No such user exists");
	}
}
