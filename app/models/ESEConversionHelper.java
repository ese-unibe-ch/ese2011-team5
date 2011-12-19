/*
 * Project: ESECalendar team 5
 * Authors:
 * 		Rafael Breu
 * 		Renato Corti
 * 		Lukas Keller
 */
package models;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/** Helper class for conversions between String and Date
 */
public class ESEConversionHelper
{
	private static final String inputFormat = "dd.MM.yyyy HH:mm";
	private static final String birthdayFormat = "dd.MM.yyyy";

	/** Converts a {@link String} to a {@link Date} value.
	 * @param userDateString String of the form of {@value #inputFormat} that should be parsed.
	 * @return The date value.
	 */
	public static Date convertStringToDate(String userDateString)
	{
		DateTimeFormatter dateTimeConvert = DateTimeFormat.forPattern(inputFormat);
		
		DateTime dateTimeParser = dateTimeConvert.parseDateTime(userDateString);
		return dateTimeParser.toDate();
	}
	/**
	 * Converts a {@link Date} to a {@link String} value.
	 * @param userDateString String of the form {@value #birthdayFormat}.
	 * @return Date of birthday.
	 * @see #convertBirthdayDateToString(Date)
	 */
	public static Date convertBirthdayStringToDate(String userDateString) 
	{
		DateTimeFormatter dateTimeConvert = DateTimeFormat.forPattern(birthdayFormat);
		try
		{
			DateTime dateTimeParser = dateTimeConvert.parseDateTime(userDateString);
			return dateTimeParser.toDate();
		}
		catch(IllegalArgumentException e)
		{
			return null;
		}
	}

	/** Converts a {@link Date} to a {@link String} value.
	 * @param date The date that should be converted.
	 * @return The date as a string of the form {@value #inputFormat}.
	 */
	public static String convertDateToString(Date date)
	{
		SimpleDateFormat simpleDateConverter = new SimpleDateFormat(inputFormat);
		if(date != null)
		{
			return simpleDateConverter.format(date);
		}
		else
		{
			return "-";
		}
	}
	/**
	 * Converts a {@link Date} to a {@link String} value.<br>
	 * If a <code>null</code> Date is passed, the birthday String is set to "-".
	 * @param date to be converted.
	 * @return String representation of birthday of the form {@value #birthdayFormat}.
	 * @see #convertBirthdayStringToDate(String)
	 */
	public static String convertBirthdayDateToString(Date date)
	{
		SimpleDateFormat simpleDateConverter = new SimpleDateFormat(birthdayFormat);
		if(date != null)
		{
			return simpleDateConverter.format(date);
		}
		else
		{
			return "-";
		}
	}
	
	public static int getDay(Date date)
	{
		String inputStr = convertDateToString(date);
		String dayStr = inputStr.substring(0, 2);
		return Integer.parseInt(dayStr);
	}
	
	public static int getMonth(Date date)
	{
		String inputStr = convertDateToString(date);
		String monthStr = inputStr.substring(3, 5);
		return Integer.parseInt(monthStr);
	}

	public static int getYear(Date date)
	{
		String inputStr = convertDateToString(date);
		String yearStr = inputStr.substring(6, 10);
		return Integer.parseInt(yearStr);
	}
}
