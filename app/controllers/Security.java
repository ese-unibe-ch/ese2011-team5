package controllers;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import models.ESEDatabase;
import models.ESEUser;
import play.Play;
import play.data.validation.Required;
import play.utils.Java;

public class Security extends Secure.Security {
	/*
	 * public static boolean authenticate(String username, String password) {
	 * 
	 * try { ESEUser loginUser = ESEDatabase.getUserByName(username); return
	 * loginUser.getPassword().equals(password); } catch
	 * (IllegalArgumentException e) { return false; } }
	 */
	static void onAuthenticated() {
		ESEUser loggedInUser = ESEDatabase.getUserByName(Security.connected());
		ESEDatabase.setCurrentUser(loggedInUser);
	}

	public static void ownAuthenticate(@Required String username,
			String password) {
		boolean valid = false;
		for (ESEUser user : ESEDatabase.getAllUsers()) {
			if (user.getName().equals(username)
					&& user.getPassword().equals(password)) {
				ESEDatabase.setCurrentUser(user.getName());
				valid = true;
				Application.showCalendars();
			}
		}
		Application.setValid(valid);
	}

	static void onDisconnect() {
	}

	static void onDisconnected() {

	}

	public static String connected() {
		return session.get("username");
	}

	// ////////////////////////
	// OVERWRITTING OF LOGOUT//
	// ////////////////////////

	public static void logout() throws Throwable {
		System.out.println("OWN LOGOUT...");
		Security.invoke("onDisconnect");
		session.clear();
		response.removeCookie("rememberme");
		Security.invoke("onDisconnected");
		flash.success("secure.logout");

		ESEDatabase.setCurrentUser("guest");

		Application.showCalendars(); // go to the start screen, not to the login

	}

	private static Object invoke(String m, Object... args) throws Throwable {
		Class security = null;
		List<Class> classes = Play.classloader
				.getAssignableClasses(Security.class);
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
