package models;

import java.util.ArrayList;
import java.util.regex.*;

public class ESEDatabase
{

	private static ESEUser currentUser;

	private static ArrayList<ESEUser> userList = new ArrayList<ESEUser>();

	// private static ArrayList<ESECalendar> calendarList;
	// private static ArrayList<ESEGroup> groupList;
	// private static ArrayList<ESEEvent> eventList;

	/*
	 * Current user methods:
	 */

	public static void setCurrentUser(String loggedIn)
	{
		setCurrentUser(getUserByName(loggedIn));
	}

	public static void setCurrentUser(ESEUser loggedIn)
	{
		if(currentUser != null)
		{
			currentUser.getProfile().changeState(ESEState.OFFLINE);
		}
		currentUser = loggedIn;
		currentUser.getProfile().changeState(ESEState.ONLINE);
	}

	public static ESEUser getCurrentUser()
	{
		return currentUser;
	}

	/*
	 * Methods to create ESEUsers
	 */

	public static void createUser(String username, String password, String firstName, String familyName)
	{
		ESEUser userToAdd = new ESEUser(username, password, firstName, familyName);
		userList.add(userToAdd);
	}

	public static void createUser(String username, String password,
			String firstName, String familyName, String question, String answer)
	{
		ESEUser userToAdd = new ESEUser(username, password, firstName, familyName, question, answer);
		userList.add(userToAdd);
	}

	/*
	 * Methods that return Users
	 */

	public static ESEUser getUserByName(String username) throws IllegalArgumentException
	{
		for (ESEUser user : userList)
		{
			if (user.getName().equals(username))
			{
				return user;
			}
		}
		throw new IllegalArgumentException("user " + username + " not found!");
	}

	public static ESEUser getUserByID(int userID) throws IllegalArgumentException
	{
		for (ESEUser user : userList)
		{
			if (user.getUserID() == userID)
			{
				return user;
			}
		}
		throw new IllegalArgumentException("No user with this ID");
	}

	public static ArrayList<ESEUser> getOtherUsers(String username)
	{
		ArrayList<ESEUser> userListToReturn = new ArrayList<ESEUser>(userList);

		/*
		 * int i = 0; for (ESEUser user : userList){ if
		 * (user.getName().equals(username)) break; else i=1; }
		 * userListToReturn.remove(i);
		 */

		for (ESEUser user : userList)
		{
			if (user.getName().equals(username) || user.getName().equals("guest"))
			{
				userListToReturn.remove(user);
			}
		}
		return userListToReturn;
	}

	public static void setUserList(ArrayList<ESEUser> arrayList) throws IllegalAccessException
	{
		StackTraceElement[] stack = Thread.currentThread().getStackTrace();
		if (!stack[2].getClassName().equals("jobs.Bootstrap")
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

	/*
	 * public static void addCalendarToDB(ESECalendar calendar){
	 * calendarList.add(calendar); }
	 *
	 * public static void addEventToDB(ESEEvent event){ eventList.add(event); }
	 *
	 * public static void addGroupToDB(ESEGroup group){ groupList.add(group); }
	 */

	public static void removeUserByName(String username) throws IllegalArgumentException
	{
  		for (ESEUser user : userList)
  		{
			if (user.getName().equals(username))
			{
				userList.remove(user);
				return;
			}
		}
		throw new IllegalArgumentException("No such user exists");
	}

	public static void removeUserByID(int userID) throws IllegalArgumentException
	{
		for (ESEUser user : userList)
		{
			if (user.getUserID() == userID)
			{
				userList.remove(user);
				return;
			}
		}
		throw new IllegalArgumentException("No user with this ID");
	}

	public static ArrayList<ESEUser> findUser(String username) throws PatternSyntaxException
	{
		Pattern searchPattern = Pattern.compile(username.toLowerCase());
		//Pattern searchPattern = Pattern.compile(username.toLowerCase().replaceAll("\\?", "\\.").replaceAll("\\*", "\\.\\*"));

		ArrayList<ESEUser> matchingUsers = new ArrayList<ESEUser>();
		for(ESEUser user : userList)
		{
			Matcher m = searchPattern.matcher(user.getName().toLowerCase());
			if(m.matches())
			//if(user.getName().toLowerCase().matches(searchPattern))
			//if(user.getName().toLowerCase().matches(username.toLowerCase()))
			{
				matchingUsers.add(user);
			}
		}
		return matchingUsers;
	}

	/*
	 * Getters for ALL ESEUsers, ALL ESECalendars etc.
	 */

	public static ArrayList<ESEUser> getAllUsers()
	{
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

	public static ArrayList<ESEUser> searchOtherUserByName(String name)
	{
		ArrayList<ESEUser> users = new ArrayList<ESEUser>();

		CharSequence sequence = new String(name);

		for(ESEUser user:userList)
		{
			if(user.getName().contains(sequence) && !user.equals(currentUser))
			{
				users.add(user);
			}
		}
		return users;
	}
}
