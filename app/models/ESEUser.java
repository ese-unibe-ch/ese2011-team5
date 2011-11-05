package models;

import java.util.ArrayList;

public class ESEUser {
	
	private static int idCounter = 0;
	
	private int userID;
	private String username;
	private String password;
	private String firstName;
	private String familyName;
	private ArrayList<ESECalendar> calendarList;
	private ArrayList<ESEGroup> groupList;

	public ESEUser(String username, String password,
			String firstName, String familyName) {
		
		assert(username != "");

		this.userID = idCounter++;
		this.calendarList = new ArrayList<ESECalendar>();
		this.groupList = new ArrayList<ESEGroup>();
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
		return new ArrayList<ESEGroup>(this.groupList);
	}
	
	public ArrayList<ESECalendar> getCalendarList(){
		return new ArrayList<ESECalendar>(this.calendarList);
	}
	
	public void addCalendar(ESECalendar calendarToAdd) {
		assert this.equals(calendarToAdd.getOwner());
		//TODO: Inform DB
		this.calendarList.add(calendarToAdd);
	}

	public void addGroup(ESEGroup groupToAdd) {
		assert this.equals(groupToAdd.getOwner());
		//TODO: Inform DB
		this.groupList.add(groupToAdd);
	}

	public ArrayList<ESEEvent> getAllEvents(int calendarID) throws IllegalArgumentException {
		for (ESECalendar calendar : this.calendarList){
			if (calendar.getID() == calendarID)
				return calendar.getAllEvents();
		}
		throw new IllegalArgumentException("No calendar with this ID");
	}
	
	public ArrayList<ESEEvent> getAllPublicEvents(int calendarID) throws IllegalArgumentException {
		for (ESECalendar calendar : this.calendarList){
			if (calendar.getID() == calendarID)
				return calendar.getAllPublicEvents();
		}
		throw new IllegalArgumentException("No calendar with this ID");
	}
	
	public ArrayList<ESEEvent> getAllowedEvents(boolean currentUserIsOwner, int calendarID) throws IllegalArgumentException {
		for (ESECalendar calendar : this.calendarList){
			if (calendar.getID() == calendarID){
				if (currentUserIsOwner)
					return calendar.getAllEvents();
				else
					return calendar.getAllPublicEvents();
			}
		}
		throw new IllegalArgumentException("No calendar with this ID");
	}
	
	
	
}
