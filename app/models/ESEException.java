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
public class ESEException extends Exception
{
	public ESEException()
	{
		super();
	}

	public ESEException(String message)
	{
		super(message);
	}
}
