package controllers;

import play.data.validation.Required;
import play.mvc.Controller;
import models.*;

public class Register extends Controller
{

	public static void index()
	{
//		ESEUser currentUser = null;
//		ArrayList<ESECalendar> calendarList = new ArrayList<ESECalendar>();
//		ArrayList<ESEGroup> groups = new ArrayList<ESEGroup>();
//		ArrayList<ESEUser> otherUsers = new ArrayList<ESEUser>();
//		try 
//		{
//			currentUser = ESEDatabase.getCurrentUser();
//			calendarList = currentUser.getCalendarList();
//			otherUsers = ESEDatabase.getOtherUsers(currentUser.getName());
//			groups = currentUser.getGroupList();
//		} 
//		catch (ESEExceptionGuestUser e) 
//		{
//			// TODO Auto-generated catch block
//			// e.printStackTrace();				//DON'T DO ANYTHING
//		}
//		
//
//		render(currentUser, calendarList, otherUsers, groups);
		render();
	}

	public static void createNewUser(@Required String username, @Required String password, @Required String confirmpassword, @Required String question, @Required String answer)
	{
		if (!password.equals(confirmpassword))
		{
			flash.error("Passwords do not match!");
			params.flash();
			index();
		}

		if (validation.hasErrors())
		{
			flash.error("You have to provide an username, a password, a secret question and the answer!");
			params.flash();
			index();
		}

		try
		{
			ESEDatabase.createUser(username, password, "", "", question, answer);
		}
		catch(ESEException e)
		{
			flash.error(e.getMessage());
			params.flash();
			index();
		}
		Security.ownAuthenticate(username, password);
		Application.showCalendars();
	}
}
