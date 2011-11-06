package controllers;

import models.*;

public class Security extends Secure.Security
{
	public static boolean authenticate(String username, String password)
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

	static void onAuthenticated()
	{
		ESEUser loggedInUser = ESEDatabase.getUserByName(Security.connected());
		ESEDatabase.setCurrentUser(loggedInUser);
	}

	static void onDisconnect()
	{
	}

	static void onDisconnected()
	{
	}

}
