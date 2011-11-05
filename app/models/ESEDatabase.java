package models;

import java.util.ArrayList;
import java.util.Date;

public class ESEDatabase {

	private static ESEUser currentUser;
	
	private static ArrayList<ESEUser> userList;
	private static ArrayList<ESECalendar> calendarList;
	private static ArrayList<ESEGroup> groupList;
	private static ArrayList<ESEEvent> eventList;
	
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
	 * Methods to create ESEUsers
	 */
	
	public static void createUser(String username, String password, String firstName,
			String familyName){
		userList.add(new ESEUser(username, password, firstName, familyName));		
	}
	
	/*
	 * Methods that return Users
	 */
	
	public static ESEUser getUserByName(String userName) {
		for (ESEUser user : userList){
			if (user.getName().equals(userName))
				return user;
		}
		return null;
	}
	
	public static ESEUser getUserByID(int userID) {
		for (ESEUser user : userList){
			if (user.getUserID() == userID)
				return user;
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
	
	/*
	 * Methods to add or remove ESEUsers, ESECalendars, ESEEvents, ESEGroups
	 */
	
	public static void addCalendarToDB(ESECalendar calendar){
		calendarList.add(calendar);
	}
	
	public static void addEventToDB(ESEEvent event){
		eventList.add(event);
	}
	
	public static void addGroupToDB(ESEGroup group){
		groupList.add(group);
	}
	
	public static void removeUserByName(String username){
		for (ESEUser user : userList){
			if (user.getName().equals(username))
				userList.remove(user);
		}
	}
	
	public void removeUserByID(int userID){
		for (ESEUser user : userList){
			if (user.getUserID() == userID)
				userList.remove(user);
		}
	}
	
	//etc. TODO
	
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

