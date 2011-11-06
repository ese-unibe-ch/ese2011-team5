package models;

import java.util.ArrayList;

public class ESEGroup {

	private static int idCounter = 0;
	
	private int groupID;
	private String groupName;
	private ESEUser owner;
	private ArrayList<ESEUser> userList;

	public ESEGroup(String groupName, ESEUser owner) {
		
		this.userList = new ArrayList<ESEUser>();
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

	public void addUserToGroup(ESEUser user) {
		this.userList.add(user);
	}
}
