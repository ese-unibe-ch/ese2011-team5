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

	/*
	 * Methods with read only access
	 */

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
	
	public void addCalendar(String calendarName) {
		this.calendarList.add(new ESECalendar(calendarName, this));
	}

	public void addGroup(String groupName) {
		this.groupList.add(new ESEGroup(groupName, this));
	}

	public void removeCalendarById(int calendarID)
	{
		//TODO
	}

	public void removeCalendarByName(String calendarname)
	{
		//TODO
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
				calendar.getAllAllowedEvents();
			}
		}
		throw new IllegalArgumentException("No calendar with this ID");
	}

	/*
	 * Methods with read-write access
	 */

	public void addCalendar(ESECalendar calendarToAdd) {
		assert this.equals(calendarToAdd.getOwner());
		this.calendarList.add(calendarToAdd);
	}

	public void addGroup(ESEGroup groupToAdd) {
		assert this.equals(groupToAdd.getOwner());
		this.groupList.add(groupToAdd);
	}

}
