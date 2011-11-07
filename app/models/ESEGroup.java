package models;

import java.util.ArrayList;

public class ESEGroup {

	private static int idCounter = 0;

	private int groupID;
	private String groupName;
	private ESEUser owner;
	private ArrayList<ESEUser> userList = new ArrayList<ESEUser>();

	public ESEGroup(String groupName, ESEUser owner) {

		this.groupID = idCounter++;
		this.groupName = groupName;
		this.owner = owner;
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

	public ArrayList<ESEUser> getUserList() {
		return this.userList;
	}

	public void addUserToGroup(ESEUser user) {
		if (!this.userList.contains(user))
			this.userList.add(user);
	}

	public void removeUserFromGroup(ESEUser user) {
		if (this.userList.contains(user)) {
			this.userList.remove(user);
		}

	}
}
