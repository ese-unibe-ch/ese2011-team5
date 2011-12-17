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

	private static boolean validLogin = true;

	//private static ArrayList<ESEUser> otherUsers = new ArrayList<ESEUser>();

	public static void showCalendars()
	{
		ESEUser currentUser = null;
		ArrayList<ESECalendar> calendarList = new ArrayList<ESECalendar>();
		ArrayList<ESEGroup> groups = new ArrayList<ESEGroup>();
		ArrayList<ESEUser> otherUsersList = new ArrayList<ESEUser>();
		try
		{
			currentUser = ESEDatabase.getCurrentUser();
			calendarList = currentUser.getCalendarList();

			if (ESEDatabase.getOtherUsers(currentUser.getName()).isEmpty())
			{
				otherUsersList = ESEDatabase.getOtherUsers(currentUser.getName());
			}

			groups = currentUser.getGroupList();
			if (!validLogin) 
			{
				flash.error("You have to provide an username and a password!");
				params.flash();
			}
			//otherUsersList = otherUsers;
			render(currentUser, groups, otherUsersList, calendarList);
		} 
		catch (ESEExceptionGuestUser e)
		{
			render(currentUser, groups, otherUsersList, calendarList);
		}
	}

//	public static void showCalendars(ArrayList<ESEUser> otherUsers) throws ESEException {
//		ESEUser currentUser=null;
//		ArrayList<ESECalendar> calendarList = new ArrayList<ESECalendar>();
//		ArrayList<ESEGroup> groups = new ArrayList<ESEGroup>();
//		System.out.println("Show calendars()");
//		
//		try 
//		{
//			currentUser = ESEDatabase.getCurrentUser();
//			calendarList = currentUser.getCalendarList();
//			groups = currentUser.getGroupList();
//		} 
//		catch (ESEExceptionGuestUser e)
//		{
////			// TODO Auto-generated catch block
////			e.printStackTrace();				//DON'T DO ANYTHING
//		}
//		finally
//		{
//			if (!validLogin)
//			{
//				flash.error("You have to provide an username and a password!");
//				params.flash();
//			}
//
//			render(currentUser, groups, otherUsers, calendarList);
//		}
//
//		
//	}

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

//	public static void showCalendars(boolean valid) {
//		validLogin = valid;
//		showCalendars();
//	}

	public static void showOtherCalendars(String username) throws ESEException
	{
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
				showCalendars();
			}
			else
			{
				render(currentUser, otherUsers, otherUser, calendarList, groups, otherUsersList);
			}
		}
		catch (ESEExceptionGuestUser e)
		{
			render(currentUser, otherUsers, otherUser, calendarList, groups, otherUsersList);
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
		catch (ESEException e) 
		{
			flash.error(e.getMessage());
			params.flash();
		} 
		index();
	}

	public static void showCalendarView(int calendarID, String username,
			int selectedDay, int month, int year) throws ESEException
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

		ESEUser currentUser = null;
		ArrayList<ESEUser> otherUsersList = null;
		try 
		{
			currentUser = ESEDatabase.getCurrentUser();
			otherUsersList = ESEDatabase.getOtherUsers(currentUser.getName());
		} 
		catch (ESEExceptionGuestUser e)
		{
//			// TODO Auto-generated catch block
//			e.printStackTrace();				//DON'T DO ANYTHING
		}

		ESEUser calendarUser = ESEDatabase.getUserByName(username);
		ESECalendar calendar = calendarUser.getCalendarByID(calendarID);

		int currentDay = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
		if (month != Calendar.getInstance().get(Calendar.MONTH))
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
		if (!daysFromLastMonth.isEmpty())
		{
			startOfLastMonth = daysFromLastMonth.get(0);
		}

		ArrayList<ESEEvent> events = calendar.getAllAllowedEventsOfMonth(month, year);
		ArrayList<Integer> eventDaysOfMonth = calendar.getEventDaysOfMonth(month, year);
		ArrayList<String> weekdays = getWeekDays();

		//ArrayList<ESEUser> otherUsersList = otherUsers;
		render(calendarUser, currentUser, calendar, events, month, monthString,
				startOfLastMonth, daysFromLastMonth, daysFromThisMonth,
				daysFromNextMonth, eventDaysOfMonth, selectedDay, weekdays,
				currentDay, year, otherUsersList);
	}

	public static void showEvents(int calendarID, String username) throws ESEException
	{
		ESEUser currentUser = null;
		ArrayList<ESEUser> otherUsers = new ArrayList<ESEUser>();
		ArrayList<ESEGroup> groups = new ArrayList<ESEGroup>();
		try 
		{
			currentUser = ESEDatabase.getCurrentUser();
			otherUsers = ESEDatabase.getOtherUsers(currentUser.getName());
			groups = currentUser.getGroupList();
		}
		catch (ESEExceptionGuestUser e)
		{
			//				// TODO Auto-generated catch block
			//				e.printStackTrace();				//DON'T DO ANYTHING
		}
		ESEUser otherUser = ESEDatabase.getUserByName(username);
		ArrayList<ESEEvent> eventList = new ArrayList<ESEEvent>();
		eventList = otherUser.getCalendarByID(calendarID).getAllAllowedEvents();

		ESECalendar calendar = otherUser.getCalendarByID(calendarID);
		render(calendar, currentUser, otherUser, otherUsers, eventList, groups);
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
		catch (IllegalArgumentException e)
		{
			flash.error(e.getMessage());
			params.flash();
		} 
		catch (ESEException e)
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
		calendar.removeEvent(eventID);
		showCalendarView(calendarID, calendar.getOwner().getName(), selectedDay, month, year);
	}

	public static void doEditEvent(int calendarID, int eventID,
			String eventName, String eventStart, String eventEnd,
			boolean isPublic, int selectedDay, int month, int year) throws ESEException, ESEExceptionGuestUser
	{
		try
		{
			ESECalendar calendar = ESEDatabase.getCurrentUser().getCalendarByID(calendarID);
			ESEEvent event = calendar.getEventByID(eventID);

			event.setEventName(eventName);
			event.setStartDateAndEndDate(
					ESEConversionHelper.convertStringToDate(eventStart),
					ESEConversionHelper.convertStringToDate(eventEnd));
			event.setVisibility(isPublic);

			if (event.checkForOverlapping(calendar.getAllEvents()))
			{
				throw new ESEException("Event \"" + event.getEventName() + "\" overlaps with existing event!");
			}
			showCalendarView(calendarID, calendar.getOwner().getName(), selectedDay, month, year);
		}
		catch (IllegalArgumentException e)
		{
			flash.error(e.getMessage());
			params.flash();
			editEvent(calendarID, eventID, selectedDay, month, year);
		}
		catch (ESEException e)
		{
			flash.error(e.getMessage());
			params.flash();
			editEvent(calendarID, eventID, selectedDay, month, year);
		}
	}

	public static void editEvent(int calendarID, int eventID, int selectedDay,
			int month, int year) throws ESEException, ESEExceptionGuestUser
	{
		ESEUser currentUser = ESEDatabase.getCurrentUser();
		ArrayList<ESEUser> otherUsers = ESEDatabase.getOtherUsers(currentUser.getName());
		ESECalendar calendar = currentUser.getCalendarByID(calendarID);
		ESEEvent event = calendar.getEventByID(eventID);
		ArrayList<ESEUser> otherUsersList = otherUsers;

		render(currentUser, calendar, calendarID, event, calendar.getOwner().getName(),
				selectedDay, month, year, otherUsersList);
	}

	public static void showUsersInGroup(int groupID)
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
		catch (ESEException e)
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

	public static void profile(int userID) throws ESEException, ESEExceptionGuestUser
	{
		ESEUser currentUser = ESEDatabase.getCurrentUser();
		ArrayList<ESEUser> otherUsers = ESEDatabase.getOtherUsers(currentUser.getName());
		ArrayList<ESEGroup> groups = currentUser.getGroupList();
		ESEUser watchedUser = ESEDatabase.getUserByID(userID);
		render(currentUser, groups, otherUsers, watchedUser/*, profile*/);
	}
	
	public static void showUserProfile(String username) throws ESEException, ESEExceptionGuestUser
	{
		ESEUser user = ESEDatabase.getUserByName(username);
		showAndEditProfile(user.getUserID());
	}

	public static void showCurrentUserProfile() throws ESEException, ESEExceptionGuestUser
	{
//		if(ESEDatabase.isUserLogedIn())
//		{
//			ESEUser user = ESEDatabase.getCurrentUser();
//			showAndEditProfile(user.getUserID());
//		}
		
		if(controllers.Secure.Security.isConnected())
		{
			ESEUser user = ESEDatabase.getCurrentUser(); //Exception can be thrown, because this exception should not happen
			showAndEditProfile(user.getUserID());
		}
		// DO NOTHING TODO: go back to the last page!
	}
	
	
	public static void showAndEditProfile(int userID) throws ESEException, ESEExceptionGuestUser
	{
		ESEUser currentUser = ESEDatabase.getCurrentUser();
		ArrayList<ESEUser> otherUsers = ESEDatabase.getOtherUsers(currentUser.getName());
		ArrayList<ESEGroup> groups = currentUser.getGroupList();

		ArrayList<ESEUser> otherUsersList = otherUsers;

		ESEUser watchedUser = ESEDatabase.getUserByID(userID);
		//ESEProfile profile = watchedUser.getProfile();

		render(currentUser, groups, otherUsers, watchedUser, /*profile,*/ otherUsersList);
	}

	public static void forgotPassword(String username) throws ESEException
	{
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
		catch (ESEExceptionGuestUser e)
		{
			//				// TODO Auto-generated catch block
			//				e.printStackTrace();				//DON'T DO ANYTHING
		}
		ESEUser user = ESEDatabase.getUserByName(username);
		String question = user.getQuestion();

		render(user, currentUser, calendarList, otherUsers, groups, question);
	}

	public static void resetPassword(String username, String answer) throws ESEException
	{
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
			//			// TODO Auto-generated catch block
			//				e.printStackTrace();				//DON'T DO ANYTHING
		}

		ESEUser user = ESEDatabase.getUserByName(username);
		if (user.getAnswer().equals(answer)) 
		{
			render(user, username, currentUser, calendarList, otherUsers, groups);
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
	 * 
	 * @param username
	 * @throws ESEException
	 */
	public static void resetPassword(String username) throws ESEException
	{
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
		catch (ESEExceptionGuestUser e) 
		{
			//				// TODO Auto-generated catch block
			//				e.printStackTrace();				//DON'T DO ANYTHING
		}
		ESEUser user = ESEDatabase.getUserByName(username);

		render(user, currentUser, calendarList, otherUsers, groups);
	}

	public static void changePassword(@Required String username,
			@Required String password, @Required String confirmpassword)
			throws ESEException
	{
		if (!password.equals(confirmpassword))
		{
			ESEUser user = ESEDatabase.getUserByName(username);
			flash.error("Passwords do not match!");
			params.flash();
			resetPassword(username, user.getAnswer());
		}
		else
		{
			ESEUser user = ESEDatabase.getUserByName(username);
			user.setPassword(password);
			Security.ownAuthenticate(username, password);

			showCalendars();
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
		catch (ESEException e)
		{
			flash.error(e.getMessage());
			params.flash();
			showCalendars();
		}
	}

	public static void searchUser(@Required String searchName)
	{
		//ArrayList<ESEUser> otherUsersLocal = new ArrayList<ESEUser>();
		ArrayList<ESEUser> otherUsers = ESEDatabase.getOtherUsers(searchName);

		if (!searchName.equals(""))
		{
			otherUsers = ESEDatabase.findUser(searchName);
		}
		else
		{
			otherUsers = ESEDatabase.getAllUsers();
		}

//		ArrayList<ESEUser> otherUserLocalOriginal = new ArrayList<ESEUser>(otherUsersLocal);
//		for (ESEUser u : otherUserLocalOriginal)
//		{
//			if (u.getName().equals("guest") || u.getName().equals(ESEDatabase.getCurrentUser().getName()))
//			{
//				otherUsersLocal.remove(u);						//NOT LONGER NECESSARY BECAUSE THERE ISN'T A GUEST USER!
//			}
//		}

		//otherUsers = otherUsersLocal;

		//showCalendars();
		render(otherUsers);
	}
	
	/**
	 * For ajax
	 * @param searchName
	 */
	public static void search(@Required String searchName)
	{
		System.out.println("search: " + searchName);
		//ArrayList<ESEUser> otherUsersLocal = new ArrayList<ESEUser>();
		List<ESEUser> results = new ArrayList<ESEUser>();
		if (!searchName.equals(""))
		{
			results = ESEDatabase.findUser(searchName);
		}
		else
		{
			results = ESEDatabase.getAllUsers();
		}
		
		System.out.println("RESULT: " + results);
//		ArrayList<ESEUser> otherUserLocalOriginal = new ArrayList<ESEUser>(otherUsersLocal);
//		for (ESEUser u : otherUserLocalOriginal)
//		{
//			if (u.getName().equals("guest") || u.getName().equals(ESEDatabase.getCurrentUser().getName()))
//			{
//				otherUsersLocal.remove(u);						//NOT LONGER NECESSARY BECAUSE THERE ISN'T A GUEST USER!
//			}
//		}

		//otherUsers = otherUsersLocal;

		//showCalendars();
		render(results);
	}
	
	/**
	 * For ajax
	 */
	public static void search()
	{
		//ArrayList<ESEUser> otherUsersLocal = new ArrayList<ESEUser>();
		List<ESEUser> results = ESEDatabase.getAllUsers();
		
		System.out.println("RESULT ALL: " + results);
//		ArrayList<ESEUser> otherUserLocalOriginal = new ArrayList<ESEUser>(otherUsersLocal);
//		for (ESEUser u : otherUserLocalOriginal)
//		{
//			if (u.getName().equals("guest") || u.getName().equals(ESEDatabase.getCurrentUser().getName()))
//			{
//				otherUsersLocal.remove(u);						//NOT LONGER NECESSARY BECAUSE THERE ISN'T A GUEST USER!
//			}
//		}

		//otherUsers = otherUsersLocal;

		//showCalendars();
		render(results);
	}

	/**
	 * 
	 * @param eventID2 
	 * @param userID2 Of the other user
	 * @param otherUserCalendarID2 Of the other user's calendar
	 * @throws ESEExceptionGuestUser 
	 */
	public static void copyEvent(int eventID2, int userID2, int otherUserCalendarID2) throws ESEExceptionGuestUser
	{
		// ESEUser user=ESEDatabase.getUserByID(userID);
		// ESECalendar calendar=user.getCalendarByID(calendarID);
		// ESEEvent event=calendar.getEventById(eventID);
		int userID = userID2;
		int eventID = eventID2;
		int otherUserCalendarID = otherUserCalendarID2;

		ESEUser currentUser = ESEDatabase.getCurrentUser();
		List<ESECalendar> calendarList = currentUser.getCalendarList();

		render(userID, otherUserCalendarID, eventID, calendarList, currentUser);
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
			// betweenShowCalendarsAndShowCalendarView(ESEDatabase.getCurrentUser().getCalendarList().get(0).getID(),
			// ESEDatabase.getCurrentUser().getName());
			int day = ESEConversionHelper.getDay(event.getStartDate());
			int month = ESEConversionHelper.getMonth(event.getStartDate());
			int year = ESEConversionHelper.getYear(event.getStartDate());
			betweenShowCalendarsAndShowCalendarView(selectedCalendar.getID(), ESEDatabase.getCurrentUser().getName(), day, month, year);
		} 
		catch (ESEException e) 
		{
			flash.error(e.getMessage());
			params.flash();
			showCalendars();
		}
	}

	private static ArrayList<String> getWeekDays()
	{
		String[] weekdaysArray = { "Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun" };
		// List<String> weekdays = new ArrayList<String>();
		ArrayList<String> weekdays = new ArrayList<String>(Arrays.asList(weekdaysArray));
		return weekdays;
	}

	public static void showAndEditProfile() throws ESEExceptionGuestUser
	{
		ESEUser currentUser = ESEDatabase.getCurrentUser();
		ArrayList<ESECalendar> calendarList = currentUser.getCalendarList();
		ArrayList<ESEUser> otherUsers = ESEDatabase.getOtherUsers(currentUser.getName());
		ArrayList<ESEGroup> groups = currentUser.getGroupList();			

		render(currentUser, groups, otherUsers, calendarList);
	}

	public static void doEditProfile(@Required int userID, String firstName,
			String familyName, String birthday, String mail,
			String street, String city) throws ESEException, ESEExceptionGuestUser
	{
		ESEUser user = ESEDatabase.getUserByID(userID);
		//ESEProfile profile = user.getProfile();

		if (isStringNotEmpty(birthday))
		{
			user.setBirthday(birthday);
		}
		if (isStringNotEmpty(city))
		{
			user.setCity(city);
		}
		if (isStringNotEmpty(familyName))
		{
			user.setFamilyName(familyName);
		}
		if (isStringNotEmpty(firstName))
		{
			user.setFirstName(firstName);
		}
		if (isStringNotEmpty(mail))
		{
			user.setMail(mail);
		}
		
		showAndEditProfile(userID);
	}

	private static void changePassword(int userID, String password,
			String confirmpassword) throws ESEException, ESEExceptionGuestUser
	{
		if (!password.equals(confirmpassword))
		{
			flash.error("Passwords do not match!");
			params.flash();
			showAndEditProfile();
		}
		else
		{
			ESEUser user = ESEDatabase.getUserByID(userID);
			user.setPassword(password);
		}
	}

	private static boolean isStringNotEmpty(String input)
	{
		return !input.isEmpty();
	}

	public static void showGroups()
	{
		ESEUser currentUser = null;
		List<ESEGroup> groupList = new ArrayList<ESEGroup>();
		ArrayList<ESEUser> otherUsersList = null;
		
		try 
		{
			currentUser = ESEDatabase.getCurrentUser();
			groupList = currentUser.getGroupList();
			otherUsersList = ESEDatabase.getOtherUsers(currentUser.getName());
		} 
		
		catch (ESEExceptionGuestUser e) 
		{
			//TODO Auto-generated catch block
			//e.printStackTrace();				//DON'T DO ANYTHING
		}
		//ArrayList<ESEUser> otherUsersList = otherUsers;
		render(groupList, currentUser, otherUsersList);
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
		catch (ESEExceptionGuestUser e) 
		{
			//TODO Auto-generated catch block
			//e.printStackTrace();				//DON'T DO ANYTHING
		}
		render(currentUser, onlineUsers);
	}
}