/*
 * Project: ESECalendar team 5
 * Authors:
 * 		Rafael Breu
 * 		Renato Corti
 * 		Lukas Keller
 */

package models;
/**
 * An ESEExceptionGuestUser is thrown whenever there is an indication
 * that the user is not logged in.
 */
public class ESEExceptionGuestUser extends Exception
{
	public ESEExceptionGuestUser()
	{
		super("You are not logged in. You are only a guest!");
	}
}
