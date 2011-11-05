package models;

import java.util.ArrayList;

public class ESEUser {
	
	private int userID;
	private String username;
	private String password;
	private String firstName;
	private String familyName;
	private ArrayList<ESECalendar> calendarList;
	private ArrayList<ESEGroup> groupList;

	public ESEUser(int userID, String username, String password,
			String firstName, String familyName) {
		
		assert(username != "");
		calendarList = new ArrayList<ESECalendar>();
		groupList = new ArrayList<ESEGroup>();
		
		this.userID = userID;
		this.username = username;
		this.password = password;
		this.firstName = firstName;
		this.familyName = familyName;
	}

	public int getUserID(){
		return this.userID;
	}
	
	public String getName() {
		return this.username;
	}
	
	public String getPassword(){
		return this.password;
	}
	
	public String getFirstName(){
		return this.firstName;
	}
	
	public String getFamilyName(){
		return this.familyName;
	}

	public ArrayList<ESEGroup> getGroupList(){
		return groupList;
	}
	
	public ArrayList<ESECalendar> getCalendarList(){
		return calendarList;
	}
	
	public void addCalendar(ESECalendar calendarToAdd) {
		assert(calendarToAdd.getOwner() == this);
		calendarList.add(calendarToAdd);
	}

	public void addGroup(ESEGroup groupToAdd) {
		assert(groupToAdd.getOwner() == this);
		groupList.add(groupToAdd);
	}

	public ArrayList<ESEEvent> getAllEvents(int calendarID) {
		for (ESECalendar calendar : calendarList){
			if (calendar.getID() == calendarID)
				return calendar.getAllEvents();
		}
		return null;
	}
	
	public ArrayList<ESEEvent> getAllPublicEvents(int calendarID) {
		for (ESECalendar calendar : calendarList){
			if (calendar.getID() == calendarID)
				return calendar.getAllPublicEvents();
		}
		return null;
	}
	

	public ArrayList<ESEEvent> getAllowedEvents(boolean currentUserIsOwner, int calendarID) {
		for (ESECalendar calendar : calendarList){
			if (calendar.getID() == calendarID){
				if (currentUserIsOwner)
					return calendar.getAllEvents();
				else
					return calendar.getAllPublicEvents();
			}
		}
		return null;
	}

}
