package controllers;

import java.util.List;
import play.mvc.*;
import play.data.validation.*;
import models.*;

@With(Secure.class)
public class ESECtlUser extends Controller
{
	private static String auth_user = Secure.Security.connected();

	public static void lsCalendars (
		String uri_user
	) {
		ESEUser u;
		List<ESECalendar> lc;
		String user = uri_user==null ?auth_user :uri_user;

		if ((u = ESEUser.getUser(user)) == null) {
			user = auth_user;
			u = ESEUser.getUser(user);
		}
		lc = u.getAllCalendars();
		render(user, lc);
	}

	public static void lsUsers (
	) {
		List<ESEUser> lu;
		lu = ESEUser.getAllOtherUsers(auth_user);
		render(lu);
	}

	public static void addGroup(){
		List<ESEGroup> groups;
		groups = ESEUser.getGroupsOfUser(auth_user);
		render(groups);
	}
	
	public static void addGroupPost(@Required String groupName){
		ESEUser currentUser = ESEUser.getUser(auth_user);
		if (!validation.hasErrors()) {
			currentUser.createGroup(groupName);
		}
		ESECtlUser.lsGroups();
		params.flash();
	}
	
	public static void lsGroups () {
		List<ESEGroup> groups;
		groups = ESEUser.getGroupsOfUser(auth_user);
		render(groups);
	}
	
	public static void listUsersOfGroup(String groupName){
		render(groupName);
	}
	
	//public static void addUserToGroupPost(){
	//	
	//}

	public static void addUser (
		String uname
	) {
		render(uname);
	}

	public static void addUserPost (
		@Required String uname,
		@Required String upass,
		String ufname,
		String ulname
	) {
		ESEUser u = null;
		if (!validation.hasErrors()) {
			if ((u = ESEUser.getUser(uname)) != null) {
				u.editPassword(upass);
				u.editFirstName(upass);
				u.editFamilyName(upass);
			}
			else {
				ESEFactory.createUser(uname, upass,
					ufname, ufname);
			}
			ESECtlUser.lsUsers();
		}
		params.flash();
		validation.keep();
		ESECtlUser.addUser(uname);
	}

	public static void delUser (
		String uname
	) {
		ESEUser u = ESEUser.getUser(uname);
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
