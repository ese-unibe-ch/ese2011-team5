/*
 * Project: ESECalendar team 5
 * Authors:
 * 		Rafael Breu
 * 		Renato Corti
 * 		Lukas Keller
 * Last Change:
 * 	by:		xxx
 * 	date:	xxx
 */

package models;
/**
 * ESEState describes the current state of an ESEUser. The state 
 * is controlled in the ESEProfile of an ESEUser.<br>
 * Options of states are: <p>
 * 
 * OFFLINE or<br>
 * ONLINE
 *
 */
public enum ESEState
{
	ONLINE,
	OFFLINE;
}
