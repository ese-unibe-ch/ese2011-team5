package controllers;

import java.util.ArrayList;

import models.ESECalendar;
import models.ESEConversionHelper;
import models.ESEDatabase;
import models.ESEEvent;
import models.ESEGroup;
import models.ESEProfile;
import models.ESEUser;
import play.mvc.Controller;

public class Application extends Controller {

	private static boolean validLogin = true;

	public static void showCalendars() {
		ESEUser currentUser = ESEDatabase.getCurrentUser();
		ArrayList<ESECalendar> calendarList = currentUser.getCalendarList();
		ArrayList<ESEUser> otherUsers = ESEDatabase.getOtherUsers(currentUser
				.getName());
		ArrayList<ESEGroup> groups = currentUser.getGroupList();

		if (!validLogin) 
		{
			flash.error("You have to provide an username and a password!");
	        params.flash();
		}

		render(currentUser, groups, otherUsers, calendarList);//, valid);
	}

	public static void showCalendars(boolean valid) {
		validLogin=valid;
		showCalendars();
	}
	

	public static void showOtherCalendars(String username) {
		ESEUser currentUser = ESEDatabase.getCurrentUser();
		ESEUser otherUser = ESEDatabase.getUserByName(username);
		ArrayList<ESECalendar> calendarList = otherUser.getCalendarList();
		ArrayList<ESEUser> otherUsers = ESEDatabase.getOtherUsers(currentUser
				.getName());
		ArrayList<ESEGroup> groups = currentUser.getGroupList();
		for (ESECalendar calendar : calendarList)
			System.out.println(calendar.getID());
		render(currentUser, otherUser, otherUsers, calendarList, groups);
	}

	public static void addCalendar(String calendarName) {
		ESEUser currentUser = ESEDatabase.getCurrentUser();
		currentUser.addCalendar(calendarName);
		showCalendars();
	}

	public static void showEvents(int calendarID, String username) {

		ESEUser currentUser = ESEDatabase.getCurrentUser();
		ESEUser otherUser = ESEDatabase.getUserByName(username);
		ArrayList<ESEEvent> eventList = new ArrayList<ESEEvent>();
		eventList = otherUser.getCalendarByID(calendarID).getAllAllowedEvents();
		ArrayList<ESEUser> otherUsers = ESEDatabase.getOtherUsers(currentUser
				.getName());
		ArrayList<ESEGroup> groups = currentUser.getGroupList();
		ESECalendar calendar = otherUser.getCalendarByID(calendarID);
		
		for(ESEEvent e:eventList)
		{
			System.out.println(e);
		}
		
		render(calendar, currentUser, otherUser, otherUsers, eventList, groups);
	}

	public static void addEvent(int calendarID, String eventName,
			String eventStart, String eventEnd, boolean isPublic) {
		ESECalendar calendar = ESEDatabase.getCurrentUser().getCalendarByID(
				calendarID);
		calendar.addEvent(eventName, eventStart, eventEnd, isPublic);
		showEvents(calendarID, ESEDatabase.getCurrentUser().getName());
	}
	
	public static void removeEvent(int calendarID, int eventID)
	{
		ESECalendar calendar = ESEDatabase.getCurrentUser().getCalendarByID(calendarID);
		calendar.removeEvent(eventID);
		showEvents(calendarID, ESEDatabase.getCurrentUser().getName());
	}
	
	public static void doEditEvent(int calendarID, int eventID, String eventName,
			String eventStart, String eventEnd, boolean isPublic)
	{
		ESECalendar calendar = ESEDatabase.getCurrentUser().getCalendarByID(calendarID);
		ESEEvent event=calendar.getEventById(eventID);
		
		event.setStartDate(ESEConversionHelper.convertStringToDate(eventStart));
		event.setEndDate(ESEConversionHelper.convertStringToDate(eventEnd));
		event.setEventName(eventName);
		event.setVisibility(isPublic);
		
		
		showEvents(calendarID, ESEDatabase.getCurrentUser().getName());
	}
	
	public static void editEvent(int calendarID, int eventID)
	{
		ESEUser currentUser = ESEDatabase.getCurrentUser();
		ESEUser otherUser = ESEDatabase.getUserByName(currentUser.getName());
		ArrayList<ESEUser> otherUsers = ESEDatabase.getOtherUsers(currentUser.getName());
		ArrayList<ESEGroup> groups = currentUser.getGroupList();
		ESECalendar calendar = currentUser.getCalendarByID(calendarID);
		System.out.println("USER ID: " + currentUser.getUserID() + " calendarID: " + calendar.getID());
		ESEEvent event = calendar.getEventById(eventID);
		render(event,calendar,currentUser,otherUser,otherUsers,groups);//, calendar);
	}

	public static void showUsersInGroup(int groupID) {
		ESEUser currentUser = ESEDatabase.getCurrentUser();
		ESEGroup group = currentUser.getGroupByID(groupID);
		ArrayList userList = group.getUserList();
		ArrayList<ESEGroup> groups = currentUser.getGroupList();

		ArrayList<ESEUser> otherUsers = ESEDatabase.getOtherUsers(currentUser
				.getName());

		render(currentUser, userList, group, otherUsers, groups);
	}

	public static void addUserToGroup(String username, int groupID) {
		ESEUser currentUser = ESEDatabase.getCurrentUser();
		ESEUser userToAdd = ESEDatabase.getUserByName(username);
		ESEGroup group = currentUser.getGroupByID(groupID);
		group.addUserToGroup(userToAdd);
		showUsersInGroup(groupID);
	}

	public static void removeUserFromGroup(String username, int groupID) {
		ESEUser currentUser = ESEDatabase.getCurrentUser();
		ESEUser userToRemove = ESEDatabase.getUserByName(username);
		ESEGroup group = currentUser.getGroupByID(groupID);
		group.removeUserFromGroup(userToRemove);
		showUsersInGroup(groupID);
	}
	
	public static void profile(int userID)
	{
		ESEUser currentUser = ESEDatabase.getCurrentUser();
		ESEUser watchedUser=ESEDatabase.getUserByID(userID);
		ESEProfile profile=watchedUser.getProfile();
		ArrayList<ESECalendar> calendarList = currentUser.getCalendarList();
		ArrayList<ESEUser> otherUsers = ESEDatabase.getOtherUsers(currentUser.getName());
		ArrayList<ESEGroup> groups = currentUser.getGroupList();
		
		render(currentUser, groups, otherUsers,watchedUser, profile);
		
		
	}

}