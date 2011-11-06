package controllers;

import models.*;

public class Security extends Secure.Security
{
	private static ESEDatabase database = ESEDatabase.getInstance();
	public static boolean authenticate(String username, String password)
	{
		try
		{
			ESEUser loginUser = database.getUserByName(username);
			return loginUser.getPassword().equals(password);
		}
		catch (IllegalArgumentException e)
		{
			return false;
		}
	}

	static void onAuthenticated()
	{
		ESEUser loggedInUser = database.getUserByName(Security.connected());
		database.setCurrentUser(loggedInUser);
	}

	static void onDisconnect()
	{
	}

	static void onDisconnected()
	{
	}

}
