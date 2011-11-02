package controllers;

import models.*;

public class ESECtlSecurity extends Secure.Security
{
	static boolean authenticate (
		String username,
		String password
	) {
		ESEUser u = ESEUser.getUser(username);
		if (u == null) {
			return false;
		}
		return u.validatePassword(password);
	}
}
