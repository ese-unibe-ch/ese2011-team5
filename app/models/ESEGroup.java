package models;

import java.util.ArrayList;

public class ESEGroup {

	private int groupID;
	private String groupName;
	private ESEUser owner;
	private ArrayList<ESEUser> userList;

	public ESEGroup(int groupID, String groupName, ESEUser owner) {
		this.groupID = groupID;
		this.groupName = groupName;
		this.owner = owner;
		this.owner.addGroup(this);
	}

	public int getGroupID() {
		return this.groupID;
	}

	public String getGroupName() {
		return this.groupName;
	}

	public ESEUser getOwner() {
		return this.owner;
	}

	public void addUserToGroup(ESEUser user) {
		this.userList.add(user);
	}
}
