/*
 * Project: ESECalendar team 5
 * Authors:
 * 		Rafael Breu
 * 		Renato Corti
 * 		Lukas Keller
 */
package models;

import java.util.ArrayList;


import controllers.Secure;
import controllers.Security;
/**
 * The ESEDatabase is responsible for the administration of 
 * the {@link ESEUser}s.<br>
 * It is also the responsibility of this class to ensure, that
 * the currentUser is the user logged in within the application.
 * 
 * @see ESEUser
 *
 */
public class  ESEDatabase
{
	/**
	 * The currentUser is the ESEUser that is currently useing 
	 * the application. Depending on the identity, the currentUser
	 * has different views.
	 * 
	 * @see ESEUser
	 */
	private static ESEUser currentUser;
	
	/**
	 * The guestUser is the default user if no user is logged in at the moment.
	 */
	private static ESEUser guestUser;
	/**
	 * List of all registered ESEUsers.
	 */
	private static ArrayList<ESEUser> userList = new ArrayList<ESEUser>();



	/*
	 * Current user methods:
	 */
	/**
	 * Sets a ESEUser to {@link #currentUser}.
	 * 
	 * @param loggedIn String of loggedIn ESEUser.
	 */
	public static void setCurrentUser(String loggedIn) throws ESEException
	{
		setCurrentUser(getUserByName(loggedIn));
	}
	/**
	 * Sets a ESEUser to {@link #currentUser}.
	 * @param loggedIn ESEUser.
	 */
	public static void setCurrentUser(ESEUser loggedIn)
	{
		currentUser = loggedIn;
	}
	/**
	 * Returns the ESEUser that is currently logged in with the cookie. If no cookie exist it will return the default guestUser
	 * @return currentUser ESEUser
	 * @throws ESEException 
	 */
	public static ESEUser getCurrentUser() 
	{
		String username = Security.connected();

		try 
		{
			return ESEDatabase.getUserByName(username);
		} catch (ESEException e)
		{
			return guestUser;	
		}
	}

	/*
	 * Methods to create ESEUsers
	 */
	/**
	 * Creates a new ESEUser.
	 * 
	 * @param username of new ESEUser. Must not be null.
	 * @param password of new ESEUser. Must not be null.
	 * @param firstName of new ESEUser.
	 * @param familyName of new ESEUser
	 * @throw ESEException 
	 * @see {@link #createUser(String, String, String, String, String, String)}
	 * to create a ELEUser with secret question and answer.
	 * @see ESEUser
	 * @see ESEProfile
	 */
	public static void createUser(String username, String password, String firstName, String familyName) throws ESEException
	{
		ESEUser userToAdd = new ESEUser(username, password, firstName, familyName);
		userList.add(userToAdd);
	}
	/**
	 * Creates a new ESEUser.
	 * 
	 * @param username of new ESEUser. Must not be null.
	 * @param password of new ESEUser. Must not be null.
	 * @param firstName of new ESEUser.
	 * @param familyName of new ESEUser.
	 * @param question to reset password of new ESEUser.
	 * @param answer to reset password of new ESEUser.
	 * @throws ESEException
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
	 * 
	 * @param username to search by.
	 * @return ESEUser searched for.
	 * @throws ESEException if there is no ESEUser with such username.
	 * 
	 * @see #getUserByID(int) to search by ID.
	 */
	public static ESEUser getUserByName(String username) throws ESEException
	{
		for (ESEUser user : userList)
		{
			if (user.getName().equals(username))
			{
				if(!isUserGuestUser(user))
				{
					return user;
				}
				else
				{
					System.out.println("It is the guest user!");
				}
			}
		}
		throw new ESEException("User \"" + username + "\" not found!");
	}
	private static boolean isUserGuestUser(ESEUser user) 
	{
		
		System.out.println("in user: " + user.getName() + "guestUser: "+ guestUser.getName());
		return user.equals(guestUser);
	}
	/**
	 * Searches by userID and returns a {@link ESEUser}.
	 * 
	 * @param userID to search by.
	 * @return ESEUser searched for.
	 * @throws ESEException if there is no ESEUser with such ID.
	 * 
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
	 * 
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
	/**
	 * Removes a {@link ESEUser} by its username.
	 * 
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
	 * 
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
	 * 
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

		for(ESEUser user:userList)
		{
			if(user.getName().toLowerCase().contains(sequence) && !user.equals(currentUser))
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
	/**
	 * creates a new guestUser and set the database guestUser to this new user
	 * @throws ESEException
	 */
	
	public static void prepareGuestUser() throws ESEException 
	{
		guestUser = new ESEUser("guest", "guest", "guest", "guest");
	}
	
	/**
	 * 
	 * @return true if an user is logged in; else return false
	 */
	public static boolean isUserLogedIn()
	{
		String username = Security.connected();
		try 
		{
			ESEDatabase.getUserByName(username);
			return true;
		} 
		catch (ESEException e)
		{
			return false;
		}
	}
}
