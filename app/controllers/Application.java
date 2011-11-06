package controllers;

import java.util.ArrayList;

import models.ESECalendar;
import models.ESEDatabase;
import models.ESEEvent;
import models.ESEGroup;
import models.ESEUser;
import play.*;
import play.mvc.*;

public class Application extends Controller
{

    public static void showCalendars()
    {
    	ESEUser currentUser = ESEDatabase.getCurrentUser();
    	ArrayList<ESECalendar> calendarList = currentUser.getCalendarList();
    	ArrayList<ESEUser> otherUsers = ESEDatabase.getOtherUsers(currentUser.getName());
    	ArrayList<ESEGroup> groups = currentUser.getGroupList();
    	render(currentUser, groups, otherUsers, calendarList);
    }
    
    public static void showOtherCalendars(String username)
    {
    	ESEUser currentUser = ESEDatabase.getCurrentUser();
    	ESEUser otherUser = ESEDatabase.getUserByName(username);
    	ArrayList<ESECalendar> calendarList = otherUser.getCalendarList();
    	for(ESECalendar calendar : calendarList)
    		System.out.println(calendar.getID());
    	render(currentUser, otherUser, calendarList);
    }
    
    public static void addCalendar(String calendarName){
    	ESEUser currentUser = ESEDatabase.getCurrentUser();
    	currentUser.addCalendar(calendarName);
    	showCalendars();
    }
    
    public static void showEvents(int calendarID, String username){
    	ESEUser currentUser = ESEDatabase.getCurrentUser();
    	ESEUser otherUser = ESEDatabase.getUserByName(username);
    	ArrayList<ESEEvent> eventList = new ArrayList<ESEEvent>(); 
		eventList = otherUser.getCalendarByID(calendarID).getAllAllowedEvents();
		ESECalendar calendar = otherUser.getCalendarByID(calendarID);
		render(calendar, currentUser, otherUser, eventList);
	}
    
    public static void addEvent(int calendarID, String eventName, String eventStart, 
		String eventEnd, boolean isPublic){
    	ESECalendar calendar = ESEDatabase.getCurrentUser().getCalendarByID(calendarID);
		calendar.addEvent(eventName, eventStart, eventEnd, isPublic);
		showEvents(calendarID,ESEDatabase.getCurrentUser().getName());
    }
    
    public static void showUsersInGroup(int groupID){
    	ESEUser currentUser = ESEDatabase.getCurrentUser();
    	ESEGroup group = currentUser.getGroupByID(groupID);
    	ArrayList userList = group.getUserList();
    	
    	ArrayList<ESEUser> otherUsers = ESEDatabase.getOtherUsers(currentUser.getName());
    	
    	render(currentUser, userList, group, otherUsers);
    }
    
    public static void addUserToGroup(String username, int groupID){
    	ESEUser currentUser = ESEDatabase.getCurrentUser();
    	ESEUser userToAdd = ESEDatabase.getUserByName(username);
    	ESEGroup group = currentUser.getGroupByID(groupID);
    	group.addUserToGroup(userToAdd);
    	showUsersInGroup(groupID);
    }

}