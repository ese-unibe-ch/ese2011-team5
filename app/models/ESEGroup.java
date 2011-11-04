package models;

public class ESEGroup {
	
	private int groupID;
	private String groupName;
	private ESEUser owner;

	public ESEGroup(int groupID, String groupName, ESEUser owner) {
		this.groupID = groupID;
		this.groupName = groupName;
		this.owner = owner;
	}
	
	public int getGroupID(){
		return this.groupID;
	}

	public String getGroupName(){
		return this.groupName;
	}
	
	public ESEUser getOwner(){
		return this.owner;
	}
}
