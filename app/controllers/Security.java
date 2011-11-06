package controllers;

import models.*;

public class Security extends Secure.Security
{
	/*public static boolean authenticate(String username, String password)
	{
		
		try
		{
			ESEUser loginUser = ESEDatabase.getUserByName(username);
			return loginUser.getPassword().equals(password);
		}
		catch (IllegalArgumentException e)
		{
			return false;
		}
	}
*/
	static void onAuthenticated()
	{
		ESEUser loggedInUser = ESEDatabase.getUserByName(Security.connected());
		ESEDatabase.setCurrentUser(loggedInUser);
	}
	
	static boolean authenticate(String username, String password){
		
		for(ESEUser user : ESEDatabase.getAllUsers()){
			if (user.getName().equals(username) && user.getPassword().equals(password)){
				ESEDatabase.setCurrentUser(user.getName());
				return true;
			}
		}
		return false;
	}

	static void onDisconnect()
	{
	}

	static void onDisconnected()
	{
	}

}
