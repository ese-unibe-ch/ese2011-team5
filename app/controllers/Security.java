package controllers;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import models.*;
import play.Play;
import play.data.validation.Required;
import play.utils.Java;

public class Security extends Secure.Security
{

	public static void ownAuthenticate(@Required String username, String password) 
	{
		try
		{
			ESEUser loginUser = ESEDatabase.getUserByName(username);

			if(loginUser.getPassword().equals(password))
			{
				ESEDatabase.setCurrentUser(loginUser.getName());
			}
			else
			{
				flash.error("Wrong password! Try it again! <a href=forgotPassword/" 
			+ username +"> Did you forget your password? - Don't worry, be happy: There is a solution! </a>");
				params.flash();
			}
		}
		catch(ESEException e)
		{
			  flash.error("No user with the given username exists!");
	          params.flash();
		}
		
		Application.showCalendars();
	}

	public static String connected()
	{
		return session.get("username");
	}

	/*
	 * Overwriting of the play-provided #logout method
	 */
	public static void logout() throws Throwable 
	{
		Security.invoke("onDisconnect");
		session.clear();
		response.removeCookie("rememberme");
		Security.invoke("onDisconnected");
		flash.success("secure.logout");
		//ESEDatabase.getCurrentUser().getProfile().changeState(ESEState.OFFLINE);
		ESEDatabase.setCurrentUser("guest");

		Application.showCalendars(); // go to the start screen, not to the login
	}

	private static Object invoke(String m, Object... args) throws Throwable 
	{
		Class security = null;
		List<Class> classes = Play.classloader.getAssignableClasses(Security.class);
		if (classes.size() == 0) {
			security = Security.class;
		} else {
			security = classes.get(0);
		}
		try {
			return Java.invokeStaticOrParent(security, m, args);
		} catch (InvocationTargetException e) {
			throw e.getTargetException();
		}
	}
}
