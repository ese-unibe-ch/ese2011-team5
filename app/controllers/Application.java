package controllers;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import models.ESECalendar;
import models.ESEConversionHelper;
import models.ESEDatabase;
import models.ESEEvent;
import models.ESEGroup;
import models.ESEProfile;
import models.ESEUser;
import play.data.validation.Required;
import play.mvc.Controller;

public class Application extends Controller {

	private static boolean validLogin = true;
	
	public static void showCalendars() {
		ESEUser currentUser = ESEDatabase.getCurrentUser();
		ArrayList<ESECalendar> calendarList = currentUser.getCalendarList();
		ArrayList<ESEUser> otherUsers = ESEDatabase.getOtherUsers(currentUser.getName());
		ArrayList<ESEGroup> groups = currentUser.getGroupList();

		if (!validLogin) 
		{
			flash.error("You have to provide an username and a password!");
	        params.flash();
		}
		
		render(currentUser, groups, otherUsers, calendarList);
	}
	
	public static void betweenShowCalendarsAndShowCalendarView(int calendarID, String currentUser)
	{
		int currentMonth = Calendar.getInstance().get(Calendar.MONTH);
		int currentYear = Calendar.getInstance().get(Calendar.YEAR);
		showCalendarView(calendarID, currentUser,100,currentMonth,currentYear);		
	}

	
	public static void showCalendars(boolean valid) {
		validLogin=valid;
		showCalendars();
	}
	

	public static void showOtherCalendars(String username) {
		ESEUser currentUser = ESEDatabase.getCurrentUser();
		ESEUser otherUser = ESEDatabase.getUserByName(username);
		ArrayList<ESECalendar> calendarList = otherUser.getCalendarList();
		ArrayList<ESEUser> otherUsers = ESEDatabase.getOtherUsers(currentUser.getName());
		ArrayList<ESEGroup> groups = currentUser.getGroupList();
		for (ESECalendar calendar : calendarList)
			System.out.println(calendar.getID());
		render(currentUser, otherUsers, calendarList, groups);
	}

	public static void addCalendar(String calendarName) {
		ESEUser currentUser = ESEDatabase.getCurrentUser();
		currentUser.addCalendar(calendarName);
		showCalendars();
	}
	
	public static void showCalendarView(int calendarID, String username, int selectedDay, 
			int month, int year)
	{
		
		if (month == 12)
		{
			month = 0;
			year++;
		}
		if (month == -1)
		{
			month = 11;
			year--;
		}
		
		ESEUser currentUser = ESEDatabase.getCurrentUser();
		ESECalendar calendar = currentUser.getCalendarByID(calendarID);
		
		int currentDay  = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
		
		SimpleDateFormat dfs = new SimpleDateFormat();
    	String monthString = dfs.getDateFormatSymbols().getMonths()[month];
    	
		
		List<Integer> daysFromLastMonth = new ArrayList<Integer>();
		List<Integer> daysFromNextMonth = new ArrayList<Integer>();
		daysFromLastMonth =	calendar.getDaysFromLastMonth(month, year);
		daysFromNextMonth = calendar.getDaysFromNextMonth(month, year);
		List<Integer> daysFromThisMonth =  calendar.getDaysFromThisMonth(month, year);
        
		int startOfLastMonth = 0; //NOCH ANPASSEN
		if(!daysFromLastMonth.isEmpty())
			startOfLastMonth = daysFromLastMonth.get(0);
        
		ArrayList<ESEEvent> events = calendar.getAllAllowedEventsOfMonth(month);
        ArrayList<Integer> eventDaysOfMonth = calendar.getEventDaysOfMonth(month);
        ArrayList<String> weekdays = getWeekDays();
        
        render(currentUser, calendar, events, month, monthString, 
        		startOfLastMonth, daysFromLastMonth, daysFromThisMonth, 
        		daysFromNextMonth, eventDaysOfMonth, selectedDay,
        		weekdays, currentDay, year);
		
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
		ESEEvent event=calendar.getEventByID(eventID);
		
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
		ESEEvent event = calendar.getEventByID(eventID);
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
		ArrayList<ESEUser> otherUsers = ESEDatabase.getOtherUsers(currentUser.getName());
		ArrayList<ESEGroup> groups = currentUser.getGroupList();
		
		render(currentUser, groups, otherUsers,watchedUser, profile);
	}
	
	public static void forgotPassword(String username)
	{
		ESEUser currentUser = ESEDatabase.getCurrentUser();
		ArrayList<ESECalendar> calendarList = currentUser.getCalendarList();
		ArrayList<ESEUser> otherUsers = ESEDatabase.getOtherUsers(currentUser.getName());
		ArrayList<ESEGroup> groups = currentUser.getGroupList();
	
		ESEUser user=ESEDatabase.getUserByName(username);
		String question=user.getQuestion();
		
		render(user, currentUser, calendarList, otherUsers, groups, question);
	}
	
	public static void resetPassword(String username, String answer)
	{
		ESEUser currentUser = ESEDatabase.getCurrentUser();
		ArrayList<ESECalendar> calendarList = currentUser.getCalendarList();
		ArrayList<ESEUser> otherUsers = ESEDatabase.getOtherUsers(currentUser.getName());
		ArrayList<ESEGroup> groups = currentUser.getGroupList();
		
		ESEUser user=ESEDatabase.getUserByName(username);
	
		if(user.getAnswer().equals(answer))
		{
			render(user,username, currentUser,calendarList,otherUsers,groups);
		}
		else
		{
			flash.error("wrong answer!");
			params.flash();
			forgotPassword(username);
		}
	}
	
	/**
	 * ONLY used if an error happens in changePassword!
	 * @param username
	 */
	public static void resetPassword(String username)
	{
		
		ESEUser currentUser = ESEDatabase.getCurrentUser();
		ArrayList<ESECalendar> calendarList = currentUser.getCalendarList();
		ArrayList<ESEUser> otherUsers = ESEDatabase.getOtherUsers(currentUser.getName());
		ArrayList<ESEGroup> groups = currentUser.getGroupList();
		
		ESEUser user=ESEDatabase.getUserByName(username);
	
		render(user, currentUser,calendarList,otherUsers,groups);
	}
	
	public static void changePassword(@Required String username, @Required String password, @Required String confirmpassword)
	{
		 if(!password.equals(confirmpassword))
	     {
			 ESEUser user=ESEDatabase.getUserByName(username);
			 flash.error("Passwords do not match!");
	         params.flash();
	         resetPassword(username,user.getAnswer());
	     }
		 else
		 {
				ESEUser user=ESEDatabase.getUserByName(username);
				user.setPassword(password);
				Security.ownAuthenticate(username, password);
				
				showCalendars();
		 }
	}
	
	public static void createNewGroup(@Required String groupname)
	{
		ESEUser user=ESEDatabase.getCurrentUser();
		
		user.addGroup(groupname);
		ESEGroup group=user.getGroupByName(groupname);
		showUsersInGroup(group.getGroupID());
	}
	
	public static void searchUser(@Required String searchName)
	{
		
		System.out.println("SEARCH FOR: "+ searchName);
		ArrayList<ESEUser> otherUsers = ESEDatabase.searchOtherUserByName(searchName);
		
		System.out.println("SEARCH RESULT:");
		for(ESEUser user:otherUsers)
		{
			System.out.println("USER: " + user.getName());
		}
		
		ESEUser currentUser = ESEDatabase.getCurrentUser();
		ArrayList<ESECalendar> calendarList = currentUser.getCalendarList();
		ArrayList<ESEGroup> groups = currentUser.getGroupList();

		render(currentUser, groups, otherUsers, calendarList);
	}
	
	private static ArrayList<String> getWeekDays(){
		String[] weekdaysArray = {"Mon","Tue","Wed","Thu","Fri","Sat","Sun"};
		//List<String> weekdays = new ArrayList<String>();
		ArrayList<String> weekdays = new ArrayList(Arrays.asList(weekdaysArray));
		return weekdays;
	}
}