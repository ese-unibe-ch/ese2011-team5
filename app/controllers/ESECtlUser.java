package controllers;

import java.util.List;
import play.mvc.*;
import play.data.validation.*;
import models.*;

@With(Secure.class)
public class ESECtlUser extends Controller
{
	private static String loggedInUser = Secure.Security.connected();

	public static void lsCalendars (String uri_user) {
		ESEUser u;
		List<ESECalendar> lc;
		String user = uri_user==null ?loggedInUser :uri_user;

		if ((u = ESEUser.getUser(user)) == null) {
			user = loggedInUser;
			u = ESEUser.getUser(user);
		}
		lc = u.getAllCalendars();
		render(user, lc);
	}

	public static void lsUsers () {
		List<ESEUser> lu;
		lu = ESEUser.getAllOtherUsers(loggedInUser);
		render(lu);
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
		ESECtlUser.lsGroups();
		params.flash();
	}
	
	public static void lsGroups () {
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
			ESECtlUser.lsUsers();
		}
		params.flash();
		validation.keep();
		ESECtlUser.addUser(username);
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
		ESECtlUser.lsUsers();
	}
	
	
}
