package controllers;

import java.util.ArrayList;

import play.data.validation.Required;
import play.mvc.Controller;
import models.*;

public class Register extends Controller
{

	public static void index()
    {
		ESEUser currentUser = ESEDatabase.getCurrentUser();
		ArrayList<ESECalendar> calendarList = currentUser.getCalendarList();
		ArrayList<ESEUser> otherUsers = ESEDatabase.getOtherUsers(currentUser.getName());
		ArrayList<ESEGroup> groups = currentUser.getGroupList();
		
    	render(currentUser, calendarList, otherUsers, groups);
    }

    public static void createNewUser(@Required String username, @Required String password, @Required String confirmpassword, @Required String question, @Required String answer, @Required String confirmanswer) throws Throwable
    {
        if(!password.equals(confirmpassword))
        {
            flash.error("Passwords do not match!");
            params.flash();
            index();
        }
        
        if(!answer.equals(confirmanswer))
        {
        	flash.error("Secret answer does not match!");
        	params.flash();
        	index();
        }
        
        if(validation.hasErrors())
        {
            flash.error("You have to provide an username, a password, a secret question and the answer!");
            params.flash();
            index();
        }

        ESEDatabase.createUser(username, password, "", "", question, answer);
        Security.ownAuthenticate(username, password);
        Application.showCalendars();
    }
}
