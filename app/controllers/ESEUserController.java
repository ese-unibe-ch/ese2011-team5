package controllers;

import java.util.List;
import play.mvc.*;
import play.data.validation.*;
import models.*;

@With(Secure.class)
public class ESEUserController extends Controller
{
	private static String loggedInUser = Secure.Security.connected();

	public static void listCalendars (String uriUser) {
		ESEUser u;
		List<ESECalendar> calendarList;
		String user = uriUser==null ?loggedInUser :uriUser;

		if ((u = ESEUser.getUser(user)) == null) {
			user = loggedInUser;
			u = ESEUser.getUser(user);
		}
		calendarList = u.getAllCalendars();
		render(user, calendarList);
	}

	public static void listUsers () {
		List<ESEUser> userList;
		userList = ESEUser.getAllOtherUsers(loggedInUser);
		render(userList);
	}

	public static void addGroup(){
		List<ESEGroup> groups;
		groups = ESEUser.getGroupsOfUser(loggedInUser);
		render(groups);
	}
	
	public static void addGroupPost(@Required String groupName){
		ESEUser currentUser = ESEUser.getUser(loggedInUser);
		if (!validation.hasErrors()) {
			currentUser.createGroup(groupName);
		}
		ESEUserController.listGroups();
		params.flash();
	}
	
	public static void listGroups () {
		List<ESEGroup> groups;
		groups = ESEUser.getGroupsOfUser(loggedInUser);
		render(groups);
	}
	
	public static void addUserToGroup(long groupID, String username){
		System.out.println(username);
		ESEGroup group = ESEGroup.findById(groupID);
		group.addUser(username);
		System.out.println("Start listing");
		for(int index=0;index<group.userList.size();index++)
		{
			System.out.println("unten" + group.userList.get(index));
		}
		System.out.println("End listing");
		listUsersOfGroup(groupID);
	}
	
	public static void listUsersOfGroup(long groupID){
		ESEGroup group = ESEGroup.findById(groupID);
		
		List<ESEUser> usersOfGroup = group.getAllUser();
		List<ESEUser> allOtherUsers = ESEUser.getAllOtherUsers(loggedInUser);
		render(group, usersOfGroup, allOtherUsers);
	}
	
	public static void addUser (
		String uname
	) {
		render(uname);
	}

	public static void addUserPost (@Required String username, 
			@Required String password, String firstName, String familyName) {
		ESEUser potentialNewUser = null;
		if (!validation.hasErrors()) {
			if ((potentialNewUser = ESEUser.getUser(username)) != null) {
				potentialNewUser.editPassword(password);
				potentialNewUser.editFirstName(password);
				potentialNewUser.editFamilyName(password);
			}
			else {
				ESEFactory.createUser(username, password,
					firstName, firstName);
			}
			ESEUserController.listUsers();
		}
		params.flash();
		validation.keep();
		ESEUserController.addUser(username);
	}

	public static void delUser (String username	) {
		ESEUser u = ESEUser.getUser(username);
		if (u != null) {
			/**
			 *	XXX: ESEUser.removeUser() should exist
			 *	and not only delete the user but also
			 *	all his/her Calendars and/or Events..
			 */
			u.delete();
		}
		ESEUserController.listUsers();
	}
	
	
}
