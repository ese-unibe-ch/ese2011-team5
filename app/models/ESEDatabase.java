package models;

import java.util.ArrayList;
import java.util.Date;

public class ESEDatabase {

	private static ESEUser currentUser;
	private static ArrayList<ESEUser> userList;
	private static ArrayList<ESECalendar> calendarList;
	private static ArrayList<ESEGroup> groupList;
	private static ArrayList<ESEEvent> eventList;
	
	private static int nbrOfUsers = 0;
	private static int nbrOfCalendars = 0;
	private static int nbrOfGroups = 0;
	private static int nbrOfEvents = 0;
	
	/* 
	 * Current user methods:
	 */
	
	static public void setCurrentUser(ESEUser loggedInUser){
		currentUser = loggedInUser;
	}
	
	static public ESEUser getCurrentUser(){
		return currentUser;
	}
	
	/*
	 * Methods to create ESEUsers, ESECalendars, ESEGroups, ESEEvents
	 */
	
	public static void createUser(String username, String password, String firstName,
			String familyName){
		nbrOfUsers++;
		userList.add(new ESEUser(nbrOfUsers, username, password, firstName, familyName));		
	}
	
	public static void addCalendar(String calendarName, String ownerName){
		for (ESEUser user : userList){
			if (user.getName().equals(ownerName)){
				nbrOfCalendars++;
				ESECalendar calendarToAdd  = new ESECalendar(nbrOfCalendars, calendarName, 
						getOwnerByName(ownerName));
				user.addCalendar(calendarToAdd);
				calendarList.add(calendarToAdd);
			}
		}
	}

	public static void addEvent(String eventName, int correspondingCalendarID, 
			String startDate, String endDate, boolean isPublic){
		for (ESECalendar calendar : calendarList){
			if (correspondingCalendarID == calendar.getID()){
				nbrOfEvents++;
				ESEEvent eventToAdd = new ESEEvent(nbrOfEvents, eventName, 
						getCalendarById(correspondingCalendarID), 
						ESEConversionHelper.convertStringToDate(startDate), 
						ESEConversionHelper.convertStringToDate(endDate), isPublic);
				calendar.addEvent(eventToAdd);
				eventList.add(eventToAdd);
			}
		}
	}

	public static void addGroup(String groupName, String ownerName){
		for (ESEUser user : userList){
			if (user.getName().equals(ownerName)){
				nbrOfGroups++;
				ESEGroup groupToAdd = new ESEGroup(nbrOfGroups, groupName, 
						getOwnerByName(ownerName));
				user.addGroup(groupToAdd);
				groupList.add(groupToAdd);
			}
		}
	}
	
	/*
	 * Methods that return Users, Calendars etc. depending on some arguments
	 */
	
	public static ESEUser getOwnerByName(String ownerName) {
		for (ESEUser user : userList){
			if (user.getName().equals(ownerName))
				return user;
		}
		return null;
	}
	
	public static ESECalendar getCalendarById(int correspondingCalendarID) {
		for (ESECalendar calendar : calendarList){
			if (calendar.getID() == correspondingCalendarID)
				return calendar;
		}
		return null;
	}
	
	public static ArrayList<ESEUser> getOtherUsers(String userName){
		ArrayList<ESEUser> userListToReturn = (ArrayList<ESEUser>) userList.clone();

		int i = 0;
		for (ESEUser user : userList){
			if (user.getName().equals(userName))
				break;
			else
				i=1;
		}
		userListToReturn.remove(i);
		return userListToReturn;
	}
	
	public static ArrayList<ESEEvent> getAllEventsOfUserInCalendar(String userName, 
			int calendarID){
		for (ESEUser user : userList){
			if (user.getName().equals(userName))
				return user.getAllEvents(calendarID);
		}
		return null;
	}
	
	public static ArrayList<ESEEvent> getAllowedEventsOfUser(String calendarOwner, 
			String currentUser, int calendarID){
		for (ESEUser user : userList){
			if (user.getName().equals(calendarOwner))
				return user.getAllowedEvents(calendarOwner.equals(currentUser), 
						calendarID);
		}
		return null;
	}
	
	
	/*
	 * Getters for ALL ESEUSers, ALL ESECalendars etc.
	 */
	
	public static ArrayList<ESEUser> getAllUsers(){
		return userList;
	}
	
	public static ArrayList<ESECalendar> getAllCalendars(){
		return calendarList;
	}
	
	public static ArrayList<ESEEvent> getAllEvents(){
		return eventList;
	}
	
	public static ArrayList<ESEGroup> getAllGroups(){
		return groupList;
	}
	
}

