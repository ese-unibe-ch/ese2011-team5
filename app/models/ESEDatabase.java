package models;

import java.util.ArrayList;

public class ESEDatabase
{
	
	private static ESEUser currentUser;

	private static ArrayList<ESEUser> userList = new ArrayList<ESEUser>();

	// private static ArrayList<ESECalendar> calendarList;
	// private static ArrayList<ESEGroup> groupList;
	// private static ArrayList<ESEEvent> eventList;

	// Usage only intended for testing purposes
	public static void clearAll()
	{
		userList.clear();
		ESEUser.resetIdCounter();
		ESECalendar.resetIdCounter();
		ESEEvent.resetIdCounter();
		ESEGroup.resetIdCounter();
		ESEProfile.resetIdCounter();
	}

	/*
	 * Current user methods:
	 */
	public static void setCurrentUser(String loggedIn) throws ESEException
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

	public static void createUser(String username, String password, String firstName, String familyName) throws ESEException
	{
		ESEUser userToAdd = new ESEUser(username, password, firstName, familyName);
		userList.add(userToAdd);
	}

	public static void createUser(String username, String password,
			String firstName, String familyName, String question, String answer) throws ESEException
	{
		ESEUser userToAdd = new ESEUser(username, password, firstName, familyName, question, answer);
		userList.add(userToAdd);
	}

	/*
	 * Methods that return Users
	 */

	public static ESEUser getUserByName(String username) throws ESEException
	{
		for (ESEUser user : userList)
		{
			if (user.getName().equals(username))
			{
				return user;
			}
		}
		throw new ESEException("User \"" + username + "\" not found!");
	}

	public static ESEUser getUserByID(int userID) throws ESEException
	{
		for (ESEUser user : userList)
		{
			if (user.getUserID() == userID)
			{
				return user;
			}
		}
		throw new ESEException("No user with this ID!");
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

	public static void removeUserByName(String username) throws ESEException
	{
  		for (ESEUser user : userList)
  		{
			if (user.getName().equals(username))
			{
				userList.remove(user);
				return;
			}
		}
		throw new ESEException("No such user exists!");
	}

	public static void removeUserByID(int userID) throws ESEException
	{
		for (ESEUser user : userList)
		{
			if (user.getUserID() == userID)
			{
				userList.remove(user);
				return;
			}
		}
		throw new ESEException("No user with this ID!");
	}

	/*
	 * Getters for ALL ESEUsers, ALL ESECalendars etc.
	 */

	public static ArrayList<ESEUser> getAllUsers()
	{
		return new ArrayList<ESEUser>(userList);
	}

	public static ArrayList<ESEUser> findUser(String username)
	{
		ArrayList<ESEUser> matchingUsers = new ArrayList<ESEUser>();
		CharSequence sequence = new String(username.toLowerCase());

		for(ESEUser user:userList)
		{
			//if(user.getName().toLowerCase().matches(".*"+username.toLowerCase()+".*") && !user.equals(currentUser))
			if(user.getName().toLowerCase().contains(sequence) && !user.equals(currentUser))
			{
				matchingUsers.add(user);
			}
		}
		return matchingUsers;
	}
}
