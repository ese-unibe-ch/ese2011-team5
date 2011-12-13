/*
 * Project: ESECalendar team 5
 * Authors:
 * 		Rafael Breu
 * 		Renato Corti
 * 		Lukas Keller
 */

package models;
/**
 * An ESEException is thrown whenever there is an 
 * invalid input from a user.
 */
public class ESEExceptionGuestUser extends Exception
{
	public ESEExceptionGuestUser()
	{
		super("ERROR: Your not logged in. Your only a guest!");
	}

	public ESEExceptionGuestUser(String message)
	{
		super(message);
	}
}
