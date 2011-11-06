package models;

import java.util.ArrayList;
import java.util.Date;

public class ESEDatabase {

	
	private static ESEDatabase database;
	private static ESEUser currentUser;
	private static ArrayList<ESEUser> userList;
	//private static ArrayList<ESECalendar> calendarList;
	//private static ArrayList<ESEGroup> groupList;
	//private static ArrayList<ESEEvent> eventList;

	private ESEDatabase(){
		userList = new ArrayList<ESEUser>();
	}
	
	public static ESEDatabase getInstance(){
		if(database == null){
			database = new ESEDatabase();
		}
		return database;
	}
	
	/* 
	 * Current user methods:
	 */
	
	public void setCurrentUser(ESEUser loggedInUser){
		currentUser = loggedInUser;
	}
	
	public ESEUser getCurrentUser(){
		return currentUser;
	}
	
	/*
	 * Methods to create ESEUsers
	 */
	
	public void createUser(String username, String password, String firstName,
			String familyName){
		userList.add(new ESEUser(username, password, firstName, familyName));		
	}
	
	/*
	 * Methods that return Users
	 */
	
	public ESEUser getUserByName(String userName) {
		for (ESEUser user : userList){
			if (user.getName().equals(userName))
				return user;
		}
		throw new IllegalArgumentException("No such user exists");
	}
	
	public ESEUser getUserByID(int userID) {
		for (ESEUser user : userList){
			if (user.getUserID() == userID)
				return user;
		}
		throw new IllegalArgumentException("No user with this ID");
	}
	
	public ArrayList<ESEUser> getOtherUsers(String userName){
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
	
	public void setUserList(ArrayList<ESEUser> arrayList) throws IllegalAccessException
	{
		StackTraceElement[] stack = Thread.currentThread().getStackTrace();
		if(!stack[2].getClassName().equals("jobs.Bootstrap")
				|| !stack[2].getFileName().equals("Bootstrap.java")
				|| !stack[2].getMethodName().equals("doJob"))
		{
			throw new IllegalAccessException("Nefarious attempt to overwrite database!");
		}
		userList = new ArrayList<ESEUser>();
	}
	
	/*
	 * Methods to add or remove ESEUsers, ESECalendars, ESEEvents, ESEGroups
	 */
	
	/*public static void addCalendarToDB(ESECalendar calendar){
		calendarList.add(calendar);
	}
	
	public static void addEventToDB(ESEEvent event){
		eventList.add(event);
	}
	
	public static void addGroupToDB(ESEGroup group){
		groupList.add(group);
	}*/
	
	public void removeUserByName(String username){
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
	
	/*
	 * Getters for ALL ESEUSers, ALL ESECalendars etc.
	 */
	
	public ArrayList<ESEUser> getAllUsers(){
		return userList;
	}

	public boolean isEmpty() {
		return this.userList.isEmpty();
	}
	
	/*public static ArrayList<ESECalendar> getAllCalendars(){
		return calendarList;
	}
	
	public static ArrayList<ESEEvent> getAllEvents(){
		return eventList;
	}
	
	public static ArrayList<ESEGroup> getAllGroups(){
		return groupList;
	}*/
	
}

