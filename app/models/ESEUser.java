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

	public void addCalendar(ESECalendar calendarToAdd) {
		calendarList.add(calendarToAdd);
	}

	public void addGroup(ESEGroup groupToAdd) {
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
