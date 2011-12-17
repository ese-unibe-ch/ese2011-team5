/*
 * Project: ESECalendar team 5
 * Authors:
 * 		Rafael Breu
 * 		Renato Corti
 * 		Lukas Keller
 */
package models;

import java.util.ArrayList;
import java.util.List;


import controllers.Security;
/**
 * The ESEDatabase is responsible for the administration of the {@link ESEUser}s.<br>
 * It is also the responsibility of this class to ensure, that
 * the currentUser is the user logged in within the application.
 * @see ESEUser
 */
public class  ESEDatabase
{
	
	/**
	 * The guestUser is the default user if no user is logged in at the moment.
	 */
	//private static ESEUser guestUser;
	/**
	 * List of all registered ESEUsers.
	 */
	private static ArrayList<ESEUser> userList = new ArrayList<ESEUser>();
	private static ArrayList<ESEUser> onlineUser = new ArrayList<ESEUser>();


	/*
	 * Current user method:
	 */

	/**
	 * Returns the ESEUser that is currently logged in with the cookie. If no cookie exist it will return a ESEExceptionGuestUser
	 * @return currentUser ESEUser
	 * @throws ESEExceptionGuestUser 
	 */
	public static ESEUser getCurrentUser() throws ESEExceptionGuestUser 
	{
		try
		{
			String username = Security.connected();
			return ESEDatabase.getUserByName(username);
		}
		catch (ESEException e)
		{
			System.out.println("THE CURRENT USER IS GUEST!");
			throw new ESEExceptionGuestUser();
		}
		catch(NullPointerException e)
		{
			System.out.println("No session, that means we are running a test");
			throw new ESEExceptionGuestUser();
		}
	}

	/*
	 * Methods to create ESEUsers
	 */
	/**
	 * Creates a new ESEUser without secret question and answer.
	 * @param username of new ESEUser. Must not be null.
	 * @param password of new ESEUser. Must not be null.
	 * @param firstName of new ESEUser.
	 * @param familyName of new ESEUser
	 * @throws ESEException 
	 * @see #createUser(String, String, String, String, String, String)
	 * @see ESEUser
	 */
	public static void createUser(String username, String password, String firstName, String familyName) throws ESEException
	{
		ESEUser userToAdd = new ESEUser(username, password, firstName, familyName);
		userList.add(userToAdd);
	}
	/**
	 * Creates a new ESEUser with secret question and answer.
	 * @param username of new ESEUser. Must not be null.
	 * @param password of new ESEUser. Must not be null.
	 * @param firstName of new ESEUser.
	 * @param familyName of new ESEUser.
	 * @param question to reset password of new ESEUser.
	 * @param answer to reset password of new ESEUser.
	 * @throws ESEException
	 * @see #createUser(String, String, String, String)
	 * @see ESEUser
	 */
	public static void createUser(String username, String password,
			String firstName, String familyName, String question, String answer) throws ESEException
	{
		ESEUser userToAdd = new ESEUser(username, password, firstName, familyName, question, answer);
		userList.add(userToAdd);
	}

	/*
	 * Methods that return Users
	 */
	/**
	 * Searches by username and returns a {@link ESEUser}.
	 * @param username to search by.
	 * @return ESEUser searched for.
	 * @throws ESEException if there is no ESEUser with such username.
	 * @see #getUserByID(int) to search by ID.
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
	
	/**
	 * Searches by userID and returns a {@link ESEUser}.
	 * @param userID to search by.
	 * @return ESEUser searched for.
	 * @throws ESEException if there is no ESEUser with such ID.
	 * @see #getUserByName(String) to search by username.
	 */
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
	/**
	 * Returns a List with all registered {@link ESEUser}s but the one
	 * ESEUser that is passed as argument.
	 * @param username only ESEUser not to be returned.
	 * @return ArrayList<ESEUser> of ESEUsers.
	 */
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
			if (user.getName().equals(username)/* || user.getName().equals("guest")*/)
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
	/**
	 * Removes a {@link ESEUser} by its username.
	 * @param username String of ESEUser to be removed.
	 * @throws ESEException if there is no ESEUser with this username.
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
	/**
	 * Remove a {@link ESEUser} by its ID.
	 * @param userID int of ESEUser to be removed.
	 * @throws ESEException if there is no ESEUser with this ID.
	 */
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
	/**
	 * Returns a List with all {@link ESEUser}s registered.
	 * @return ArrayList<ESEUser> of all ESEUsers.
	 */
	public static ArrayList<ESEUser> getAllUsers()
	{
		return new ArrayList<ESEUser>(userList);
	}
	/**
	 * Returns all {@link ESEUser}s with the corresponding <code>username</code>.
	 * @param username to be searched for.
	 * @return ArrayList<ESEUser> of ESEUsers with corresponding username.
	 */
	public static ArrayList<ESEUser> findUser(String username)
	{
		ArrayList<ESEUser> matchingUsers = new ArrayList<ESEUser>();
		CharSequence sequence = new String(username.toLowerCase());

		for(ESEUser user : userList)
		{
			ESEUser localCurrentUser = null;
			try
			{
				localCurrentUser = getCurrentUser();
			} 
			catch (ESEExceptionGuestUser e) 
			{
//				// TODO Auto-generated catch block
//				e.printStackTrace();				//DO NOTHING
			}
			if(user.getName().toLowerCase().contains(sequence) && !user.equals(localCurrentUser))
			{
				matchingUsers.add(user);
			}
		}
		return matchingUsers;
	}
	
	/*
	 * for testing purposes
	 */
	public static void clearAll()
	{
		userList.clear();
		ESEUser.resetIdCounter();
		ESECalendar.resetIdCounter();
		ESEEvent.resetIdCounter();
		ESEGroup.resetIdCounter();
	}
	
//	public static ESEUser getCurrentUserForView()
//	{
//		try 
//		{
//			return getCurrentUser();
//		} 
//		catch (ESEExceptionGuestUser e) 
//		{
//			return null;
//		}
//	}
	
	public static void addUserToOnline(ESEUser user)
	{
		System.out.println("ADD USER TO ONLINE: " + user);
		if(!onlineUser.contains(user))
		{
			onlineUser.add(user);
		}
	}
	
	public static void removeUserOnline(ESEUser user)
	{
		if(onlineUser.contains(user))
		{
			onlineUser.remove(user);
		}
	}
	
	public static List<ESEUser> getOnlineUsers()
	{
		return onlineUser;
	}
}
