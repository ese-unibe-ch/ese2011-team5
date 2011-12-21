package controllers;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import models.*;
import play.data.validation.Required;
import play.mvc.Controller;

public class Application extends Controller
{
	public static void betweenShowCalendarsAndShowCalendarView(int calendarID,
			String currentUser) throws ESEException
	{
		int currentMonth = Calendar.getInstance().get(Calendar.MONTH);
		int currentYear = Calendar.getInstance().get(Calendar.YEAR);
		int currentDay = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
		showCalendarView(calendarID, currentUser, currentDay, currentMonth, currentYear);
	}

	public static void betweenShowCalendarsAndShowCalendarView(int calendarID,
			String currentUser, int day, int month, int year) throws ESEException
	{
		int currentMonth = month;
		int currentYear = year;
		int currentDay = day;
		showCalendarView(calendarID, currentUser, currentDay, currentMonth, currentYear);
	}

	public static void showOtherCalendars(String username) throws ESEException
	{
		List<ESEUser> onlineUsers = ESEDatabase.getOnlineUsers();
		ESEUser currentUser = null;
		ArrayList<ESEUser> otherUsers = new ArrayList<ESEUser>();
		ArrayList<ESEGroup> groups = new ArrayList<ESEGroup>();
		ArrayList<ESEUser> otherUsersList = new ArrayList<ESEUser>();

		ESEUser otherUser = ESEDatabase.getUserByName(username);
		ArrayList<ESECalendar> calendarList = otherUser.getCalendarList();

		try
		{
			currentUser = ESEDatabase.getCurrentUser();
			otherUsers = ESEDatabase.getOtherUsers(currentUser.getName());
			groups = currentUser.getGroupList();
			otherUsersList = otherUsers;
			if(currentUser.equals(otherUser))
			{
				index();
			}
			else
			{
				render(currentUser, otherUsers, otherUser, calendarList, groups, otherUsersList, onlineUsers);
			}
		}
		catch(ESEExceptionGuestUser e)
		{
			render(currentUser, otherUsers, otherUser, calendarList, groups, otherUsersList, onlineUsers);
		}
	}

	public static void addCalendar(String calendarName) throws ESEExceptionGuestUser
	{
		ESEUser currentUser = null;
		try
		{
			currentUser = ESEDatabase.getCurrentUser();
			currentUser.addCalendar(calendarName);
		}
		catch(ESEException e)
		{
			flash.error(e.getMessage());
			params.flash();
		}
		index();
	}

	public static void removeCalendar(int calendarId) throws ESEExceptionGuestUser
	{
		ESEUser currentUser = null;
		try
		{
			currentUser = ESEDatabase.getCurrentUser();
			currentUser.removeCalendar(calendarId);
		}
		catch(ESEException e)
		{
			flash.error(e.getMessage());
			params.flash();
		}
		index();
	}
	public static void showCalendarView(int calendarID, String username,
			int selectedDay, int month, int year) throws ESEException
	{
		if(month == 12)
		{
			month = 0;
			year++;
		}
		if(month == -1)
		{
			month = 11;
			year--;
		}

		ESEUser currentUser = null;
		ArrayList<ESEUser> otherUsersList = null;
		List<ESEUser> onlineUsers = ESEDatabase.getOnlineUsers();
		try
		{
			currentUser = ESEDatabase.getCurrentUser();
			otherUsersList = ESEDatabase.getOtherUsers(currentUser.getName());
		}
		catch(ESEExceptionGuestUser e)
		{
			//Don't do anything. When the currentUser is NULL, this method
			//still should run. Our model makes sure, however, that only public events
			//are displayed
		}

		ESEUser calendarUser = ESEDatabase.getUserByName(username);
		try
		{
			ESECalendar calendar = calendarUser.getCalendarByID(calendarID);

			int currentDay = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
			if(month != Calendar.getInstance().get(Calendar.MONTH))
			{
				currentDay = 100;
			}

			SimpleDateFormat dfs = new SimpleDateFormat();
			String monthString = dfs.getDateFormatSymbols().getMonths()[month];

			List<Integer> daysFromLastMonth = new ArrayList<Integer>();
			List<Integer> daysFromNextMonth = new ArrayList<Integer>();
			daysFromLastMonth = calendar.getDaysFromLastMonth(month, year);
			daysFromNextMonth = calendar.getDaysFromNextMonth(month, year);
			List<Integer> daysFromThisMonth = calendar.getDaysFromThisMonth(month, year);

			int startOfLastMonth = 0;
			if(!daysFromLastMonth.isEmpty())
			{
				startOfLastMonth = daysFromLastMonth.get(0);
			}

			ArrayList<ESEEvent> events = calendar.getAllAllowedEventsOfMonth(month, year);
			ArrayList<Integer> eventDaysOfMonth = calendar.getEventDaysOfMonth(month, year);
			ArrayList<String> weekdays = getWeekDays();

			render(calendarUser, currentUser, calendar, events, month, monthString,
					startOfLastMonth, daysFromLastMonth, daysFromThisMonth,
					daysFromNextMonth, eventDaysOfMonth, selectedDay, weekdays,
					currentDay, year, otherUsersList, onlineUsers);
		}
		catch(ESEException e)
		{
			flash.error(e.getMessage());
			params.flash();
			index();
		}
	}

	public static void showEvents(int calendarID, String username) throws ESEException
	{
		List<ESEUser> onlineUsers = ESEDatabase.getOnlineUsers();
		ESEUser currentUser = null;
		ArrayList<ESEUser> otherUsers = new ArrayList<ESEUser>();
		ArrayList<ESEGroup> groups = new ArrayList<ESEGroup>();
		try
		{
			currentUser = ESEDatabase.getCurrentUser();
			otherUsers = ESEDatabase.getOtherUsers(currentUser.getName());
			groups = currentUser.getGroupList();
		}
		catch(ESEExceptionGuestUser e)
		{
			//Don't do anything. When the currentUser is NULL, this method
			//still should run. Our model makes sure, however, that only public events
			//are displayed
		}
		ESEUser otherUser = ESEDatabase.getUserByName(username);
		ArrayList<ESEEvent> eventList = new ArrayList<ESEEvent>();
		eventList = otherUser.getCalendarByID(calendarID).getAllAllowedEvents();

		ESECalendar calendar = otherUser.getCalendarByID(calendarID);
		render(calendar, currentUser, otherUser, otherUsers, eventList, groups, onlineUsers);
	}

	public static void addEvent(int calendarID, String eventName,
			String eventStart, String eventEnd, boolean isPublic,
			int selectedDay, int month, int year) throws ESEException, ESEExceptionGuestUser
	{
		ESECalendar calendar = ESEDatabase.getCurrentUser().getCalendarByID(calendarID);
		try
		{
			calendar.addEvent(eventName, eventStart, eventEnd, isPublic);
		}
		catch(IllegalArgumentException e)
		{
			flash.error(e.getMessage());
			params.flash();
		}
		catch(ESEException e)
		{
			flash.error(e.getMessage());
			params.flash();
		}

		showCalendarView(calendarID, calendar.getOwner().getName(), selectedDay, month, year);
	}

	public static void removeEvent(int calendarID, int eventID,
			int selectedDay, int month, int year) throws ESEException, ESEExceptionGuestUser
	{
		ESECalendar calendar = ESEDatabase.getCurrentUser().getCalendarByID(calendarID);
		try
		{
			calendar.removeEvent(eventID);
		}
		catch(ESEException e)
		{
			flash.error(e.getMessage());
			params.flash();
		}
		showCalendarView(calendarID, calendar.getOwner().getName(), selectedDay, month, year);
	}

	public static void doEditEvent(int calendarID, int eventID,
			String eventName, String eventStart, String eventEnd,
			boolean isPublic, int selectedDay, int month, int year) throws ESEException, ESEExceptionGuestUser
	{
		ESECalendar calendar = ESEDatabase.getCurrentUser().getCalendarByID(calendarID);
		try
		{
			ESEEvent event = calendar.getEventByID(eventID);

			event.setEventName(eventName);
			event.setStartDateAndEndDate(
					ESEConversionHelper.convertStringToDate(eventStart),
					ESEConversionHelper.convertStringToDate(eventEnd));
			event.setVisibility(isPublic);

			if(event.checkForOverlapping(calendar.getAllEvents()))
			{
				throw new ESEException("Event \"" + event.getEventName() + "\" overlaps with existing event!");
			}
			showCalendarView(calendarID, calendar.getOwner().getName(), selectedDay, month, year);
		}
		catch(IllegalArgumentException e)
		{
			flash.error(e.getMessage());
			params.flash();
			showCalendarView(calendarID, calendar.getOwner().getName(), selectedDay, month, year);
		}
		catch(ESEException e)
		{
			flash.error(e.getMessage());
			params.flash();
			showCalendarView(calendarID, calendar.getOwner().getName(), selectedDay, month, year);
		}
	}
	
	public static void editCalendar(int calendarID) throws ESEExceptionGuestUser, ESEException{
		List<ESEUser> onlineUsers = ESEDatabase.getOnlineUsers();
		ESEUser currentUser = ESEDatabase.getCurrentUser();
		ESECalendar calendarToEdit = currentUser.getCalendarByID(calendarID);
		render(currentUser, calendarToEdit, onlineUsers);
	}
	
	public static void doEditCalendarName(int calendarID, String calendarName) throws ESEExceptionGuestUser, ESEException{
		ESEUser currentUser = ESEDatabase.getCurrentUser();
		currentUser.getCalendarByID(calendarID).setCalendarName(calendarName);
		
		index();
	}
	
	public static void editGroup(int groupID) throws ESEExceptionGuestUser, ESEException{
		List<ESEUser> onlineUsers = ESEDatabase.getOnlineUsers();
		ESEUser currentUser = ESEDatabase.getCurrentUser();
		ESEGroup groupToEdit = currentUser.getGroupByID(groupID);
		render(currentUser, groupToEdit, onlineUsers);
	}
	
	public static void doEditGroup(int groupID, String groupName) throws ESEExceptionGuestUser, ESEException{
		ESEUser currentUser = ESEDatabase.getCurrentUser();
		currentUser.getGroupByID(groupID).setGroupName(groupName);
		
		index();
	}

	public static void editEvent(int calendarID, int eventID, int selectedDay,
			int month, int year) throws ESEException, ESEExceptionGuestUser
	{
		ESEUser currentUser = ESEDatabase.getCurrentUser();
		ESECalendar calendar = currentUser.getCalendarByID(calendarID);
		try
		{
			ArrayList<ESEUser> otherUsers = ESEDatabase.getOtherUsers(currentUser.getName());
			ESEEvent event = calendar.getEventByID(eventID);
			ArrayList<ESEUser> otherUsersList = otherUsers;

			render(currentUser, calendar, calendarID, event, calendar.getOwner().getName(),
					selectedDay, month, year, otherUsersList);
		}
		catch(ESEException e)
		{
			flash.error(e.getMessage());
			params.flash();
			showCalendarView(calendarID, calendar.getOwner().getName(), selectedDay, month, year);
		}
	}

	public static void showUsersInGroup(int groupID) throws ESEExceptionGuestUser
	{
		showGroups();
	}

	public static void addUserToGroup(String username, int groupID) throws ESEExceptionGuestUser
	{
		try
		{
			ESEUser currentUser = ESEDatabase.getCurrentUser();
			ESEUser userToAdd = ESEDatabase.getUserByName(username);
			ESEGroup group = currentUser.getGroupByID(groupID);
			
			group.addUserToGroup(userToAdd);
			showUsersInGroup(groupID);
		}
		catch(ESEException e)
		{
			flash.error(e.getMessage());
			params.flash();
			showUsersInGroup(groupID);
		}
	}


	public static void removeUserFromGroup(String username, int groupID) throws ESEException, ESEExceptionGuestUser
	{
		ESEUser currentUser = ESEDatabase.getCurrentUser();
		ESEGroup group = currentUser.getGroupByID(groupID);
		ESEUser userToRemove = ESEDatabase.getUserByName(username);
		group.removeUserFromGroup(userToRemove);

		showUsersInGroup(groupID);
	}

	public static void showUserProfile(String username) throws ESEException, ESEExceptionGuestUser
	{
		ESEUser user = ESEDatabase.getUserByName(username);
		showAndEditProfile(user.getUserID());
	}

	public static void showAndEditProfile(int userID) throws ESEException, ESEExceptionGuestUser
	{
		List<ESEUser> onlineUsers = ESEDatabase.getOnlineUsers();
		ESEUser currentUser = ESEDatabase.getCurrentUser();
		ArrayList<ESEUser> otherUsers = ESEDatabase.getOtherUsers(currentUser.getName());
		ArrayList<ESEGroup> groups = currentUser.getGroupList();

		ArrayList<ESEUser> otherUsersList = otherUsers;

		ESEUser watchedUser = ESEDatabase.getUserByID(userID);
		//ESEProfile profile = watchedUser.getProfile();

		render(currentUser, groups, otherUsers, watchedUser, /*profile,*/ otherUsersList, onlineUsers);
	}

	public static void forgotPassword(String username) throws ESEException
	{
		List<ESEUser> onlineUsers = ESEDatabase.getOnlineUsers();
		ESEUser currentUser = null;
		ArrayList<ESECalendar> calendarList = new ArrayList<ESECalendar>();
		ArrayList<ESEUser> otherUsers = new ArrayList<ESEUser>();
		ArrayList<ESEGroup> groups = new ArrayList<ESEGroup>();
		try
		{
			currentUser = ESEDatabase.getCurrentUser();
			calendarList = currentUser.getCalendarList();
			otherUsers = ESEDatabase.getOtherUsers(currentUser.getName());
			groups = currentUser.getGroupList();
		}
		catch(ESEExceptionGuestUser e)
		{
			//Don't do anything. When the currentUser is NULL, this user should still
			//be able to reset his password!
		}
		ESEUser user = ESEDatabase.getUserByName(username);
		String question = user.getQuestion();

		render(user, currentUser, calendarList, otherUsers, groups, question, onlineUsers);
	}

	public static void resetPassword(String username, String answer) throws ESEException
	{
		List<ESEUser> onlineUsers = ESEDatabase.getOnlineUsers();
		ESEUser currentUser = null;
		ArrayList<ESECalendar> calendarList = new ArrayList<ESECalendar>();
		ArrayList<ESEUser> otherUsers = new ArrayList<ESEUser>();
		ArrayList<ESEGroup> groups = new ArrayList<ESEGroup>();
		try
		{
			currentUser = ESEDatabase.getCurrentUser();
			calendarList = currentUser.getCalendarList();
			otherUsers = ESEDatabase.getOtherUsers(currentUser.getName());
			groups = currentUser.getGroupList();
		}
		catch(ESEExceptionGuestUser e)
		{
			//Don't do anything. When the currentUser is NULL, this user should still
			//be able to reset his password!
		}

		ESEUser user = ESEDatabase.getUserByName(username);
		if(user.getAnswer().equals(answer))
		{
			render(user, username, currentUser, calendarList, otherUsers, groups, onlineUsers);
		}
		else
		{
			flash.error("Wrong answer!");
			params.flash();
			forgotPassword(username);
		}
	}

	// ONLY used if an error happens in changePassword!
	public static void resetPassword(String username) throws ESEException
	{
		List<ESEUser> onlineUsers = ESEDatabase.getOnlineUsers();
		ESEUser currentUser = null;
		ArrayList<ESECalendar> calendarList = new ArrayList<ESECalendar>();
		ArrayList<ESEUser> otherUsers = new ArrayList<ESEUser>();
		ArrayList<ESEGroup> groups = new ArrayList<ESEGroup>();

		try
		{
			currentUser = ESEDatabase.getCurrentUser();
			calendarList = currentUser.getCalendarList();
			otherUsers = ESEDatabase.getOtherUsers(currentUser.getName());
			groups = currentUser.getGroupList();
		}
		catch(ESEExceptionGuestUser e)
		{
			//Don't do anything. When the currentUser is NULL, this user should still
			//be able to reset his password!
		}
		ESEUser user = ESEDatabase.getUserByName(username);

		render(user, currentUser, calendarList, otherUsers, groups, onlineUsers);
	}

	public static void changePassword(@Required String username,
			@Required String password, @Required String confirmpassword)
			throws ESEException
	{
		ESEUser user = ESEDatabase.getUserByName(username);
		if(!password.equals(confirmpassword))
		{
			flash.error("Passwords do not match!");
			params.flash();
			resetPassword(username, user.getAnswer());
		}
		else
		{
			user.setPassword(password);
			Security.ownAuthenticate(username, password);
			index();
		}
	}

	public static void createNewGroup(@Required String groupname) throws ESEExceptionGuestUser
	{
		ESEUser user = ESEDatabase.getCurrentUser();
		ESEGroup group = null;
		try
		{
			user.addGroup(groupname);
			group = user.getGroupByName(groupname);
			showUsersInGroup(group.getGroupID());
		}
		catch(ESEException e)
		{
			flash.error(e.getMessage());
			params.flash();
			showGroups();
		}
	}

	public static void searchUser(@Required String searchName)
	{
		List<ESEUser> onlineUsers = ESEDatabase.getOnlineUsers();
		ArrayList<ESEUser> otherUsers = ESEDatabase.getOtherUsers(searchName);

		if(!searchName.equals(""))
		{
			otherUsers = ESEDatabase.findUser(searchName);
		}
		else
		{
			otherUsers = ESEDatabase.getAllUsers();
		}

		render(otherUsers, onlineUsers);
	}

	// For Ajax
	public static void search(@Required String searchName)
	{
		List<ESEUser> results = new ArrayList<ESEUser>();
		ESEUser currentUser = null;
		
		try 
		{
			currentUser = ESEDatabase.getCurrentUser();
		} 
		catch (ESEExceptionGuestUser e) 
		{
			
		}
		finally
		{
			if(!searchName.equals(""))
			{
				results = ESEDatabase.findUser(searchName);
			}
			else
			{
				results = ESEDatabase.getAllUsers();
			}

			render(results, currentUser);
		}
		
		
	}

	// For Ajax
	public static void search()
	{
		List<ESEUser> results = ESEDatabase.getAllUsers();

		render(results);
	}

	public static void copyEvent(int eventID2, int userID2, int otherUserCalendarID2) throws ESEExceptionGuestUser, ESEException
	{
		List<ESEUser> onlineUsers = ESEDatabase.getOnlineUsers();
		ESEUser user = ESEDatabase.getUserByID(userID2);
		ESECalendar calendar = user.getCalendarByID(otherUserCalendarID2);
		ESEEvent event = calendar.getEventByID(eventID2);

		int userID = userID2;
		int eventID = eventID2;
		int otherUserCalendarID = otherUserCalendarID2;

		ESEUser currentUser = ESEDatabase.getCurrentUser();
		List<ESECalendar> calendarList = currentUser.getCalendarList();

		render(userID, otherUserCalendarID, eventID, calendarList, currentUser, event, onlineUsers);
	}

	public static void doCopyEvent(int otherUserID, int otherUserCalendarID,
			int eventID, String selectedCalendarName) throws ESEException, ESEExceptionGuestUser
	{
		ESEUser user = ESEDatabase.getCurrentUser();

		ESEUser otherUser = ESEDatabase.getUserByID(otherUserID);
		ESECalendar selectedCalendar = null;
		try
		{
			selectedCalendar = user.getCalendarByName(selectedCalendarName);
			ESECalendar otherCalendar = otherUser.getCalendarByID(otherUserCalendarID);
			ESEEvent event = otherCalendar.getEventByID(eventID);
			event.addCorrespondingCalendar(selectedCalendar);
			selectedCalendar.addEvent(event);
			event.addParticipant(user);
			int day = ESEConversionHelper.getDay(event.getStartDate());
			int month = ESEConversionHelper.getMonth(event.getStartDate());
			int year = ESEConversionHelper.getYear(event.getStartDate());
			betweenShowCalendarsAndShowCalendarView(selectedCalendar.getID(), ESEDatabase.getCurrentUser().getName(), day, month, year);
		}
		catch(ESEException e)
		{
			flash.error(e.getMessage());
			params.flash();
			index();
		}
	}

	private static ArrayList<String> getWeekDays()
	{
		String[] weekdaysArray = {"Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"};
		ArrayList<String> weekdays = new ArrayList<String>(Arrays.asList(weekdaysArray));
		return weekdays;
	}

	public static void showAndEditProfile() throws ESEExceptionGuestUser
	{
		List<ESEUser> onlineUsers = ESEDatabase.getOnlineUsers();
		ESEUser currentUser = ESEDatabase.getCurrentUser();
		ArrayList<ESECalendar> calendarList = currentUser.getCalendarList();
		ArrayList<ESEUser> otherUsers = ESEDatabase.getOtherUsers(currentUser.getName());
		ArrayList<ESEGroup> groups = currentUser.getGroupList();

		render(currentUser, groups, otherUsers, calendarList, onlineUsers);
	}

	public static void doEditProfile(@Required int userID, String firstName,
			String familyName, String birthday, String mail,
			String street, String city) throws ESEException, ESEExceptionGuestUser
	{
		ESEUser user = ESEDatabase.getUserByID(userID);
		
		if(isStringNotEmpty(firstName))
		{
			user.setFirstName(firstName);
		}
		if(isStringNotEmpty(familyName))
		{
			user.setFamilyName(familyName);
		}
		if(isStringNotEmpty(mail))
		{
			user.setMail(mail);
		}
		if(isStringNotEmpty(birthday))
		{
			user.setBirthday(birthday);
		}
		if(isStringNotEmpty(street))
		{
			user.setStreet(street);
		}
		if(isStringNotEmpty(city))
		{
			user.setCity(city);
		}

		showAndEditProfile(userID);
	}

	private static boolean isStringNotEmpty(String input)
	{
		return !input.isEmpty();
	}

	public static void showGroups() throws ESEExceptionGuestUser
	{
		List<ESEUser> onlineUsers = ESEDatabase.getOnlineUsers();
		ESEUser currentUser = ESEDatabase.getCurrentUser();
		List<ESEGroup> groupList = currentUser.getGroupList();
		ArrayList<ESEUser> otherUsersList = ESEDatabase.getOtherUsers(currentUser.getName());

		render(groupList, currentUser, otherUsersList, onlineUsers);
	}

	public static void login()
	{
		render();
	}

	public static void index()
	{
		ESEUser currentUser = null;
		List<ESEUser> onlineUsers = ESEDatabase.getOnlineUsers();
		try
		{
			currentUser = ESEDatabase.getCurrentUser();
		}
		catch(ESEExceptionGuestUser e)
		{
			//Don't do anything. When the currentUser is NULL, this method
			//still should run. However, this guest user only sees the welcome page
			//and can only see public events then.
		}
		render(currentUser, onlineUsers);
	}
}
