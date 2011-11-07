package models;

import java.util.ArrayList;

public class ESEDatabase {

	private static ESEUser currentUser;

	private static ArrayList<ESEUser> userList = new ArrayList<ESEUser>();

	// private static ArrayList<ESECalendar> calendarList;
	// private static ArrayList<ESEGroup> groupList;
	// private static ArrayList<ESEEvent> eventList;

	/*
	 * Current user methods:
	 */

	static public void setCurrentUser(String loggedIn) {
		currentUser = getUserByName(loggedIn);
	}

	static public void setCurrentUser(ESEUser loggedIn) {
		currentUser = loggedIn;
	}

	static public ESEUser getCurrentUser() {
		return currentUser;
	}

	/*
	 * Methods to create ESEUsers
	 */

	public static void createUser(String username, String password,
			String firstName, String familyName) {
		ESEUser userToAdd = new ESEUser(username, password, firstName,
				familyName);
		System.out.println(userToAdd.getName());
		userList.add(userToAdd);
	}

	/*
	 * Methods that return Users
	 */

	public static ESEUser getUserByName(String userName)
			throws IllegalArgumentException {
		for (ESEUser user : userList) {
			if (user.getName().equals(userName))
				return user;
		}
		throw new IllegalArgumentException("No such user exists");
	}

	public static ESEUser getUserByID(int userID)
			throws IllegalArgumentException {
		for (ESEUser user : userList) {
			if (user.getUserID() == userID)
				return user;
		}
		throw new IllegalArgumentException("No user with this ID");
	}

	public static ArrayList<ESEUser> getOtherUsers(String userName) {
		ArrayList<ESEUser> userListToReturn = (ArrayList<ESEUser>) userList
				.clone();

		/*
		 * int i = 0; for (ESEUser user : userList){ if
		 * (user.getName().equals(userName)) break; else i=1; }
		 * userListToReturn.remove(i);
		 */

		for (ESEUser user : userList) {
			if (user.getName().equals(userName)
					|| user.getName().equals("guest"))
				userListToReturn.remove(user);
		}

		return userListToReturn;
	}

	public static void setUserList(ArrayList<ESEUser> arrayList)
			throws IllegalAccessException {
		StackTraceElement[] stack = Thread.currentThread().getStackTrace();
		if (!stack[2].getClassName().equals("jobs.Bootstrap")
				|| !stack[2].getFileName().equals("Bootstrap.java")
				|| !stack[2].getMethodName().equals("doJob")) {
			throw new IllegalAccessException(
					"Nefarious attempt to overwrite database!");
		}
		userList = new ArrayList<ESEUser>();
	}

	/*
	 * Methods to add or remove ESEUsers, ESECalendars, ESEEvents, ESEGroups
	 */

	/*
	 * public static void addCalendarToDB(ESECalendar calendar){
	 * calendarList.add(calendar); }
	 * 
	 * public static void addEventToDB(ESEEvent event){ eventList.add(event); }
	 * 
	 * public static void addGroupToDB(ESEGroup group){ groupList.add(group); }
	 */

	public static void removeUserByName(String username) {
		for (ESEUser user : userList) {
			if (user.getName().equals(username))
				userList.remove(user);
		}
	}

	public void removeUserByID(int userID) {
		for (ESEUser user : userList) {
			if (user.getUserID() == userID)
				userList.remove(user);
		}
	}

	/*
	 * Getters for ALL ESEUSers, ALL ESECalendars etc.
	 */

	public static ArrayList<ESEUser> getAllUsers() {
		return new ArrayList<ESEUser>(userList);
	}

	/*
	 * public static ArrayList<ESECalendar> getAllCalendars(){ return
	 * calendarList; }
	 * 
	 * public static ArrayList<ESEEvent> getAllEvents(){ return eventList; }
	 * 
	 * public static ArrayList<ESEGroup> getAllGroups(){ return groupList; }
	 */

}
