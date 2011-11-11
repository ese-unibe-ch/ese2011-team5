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

	/** Converts a {@link String} to a {@link Date} value.
	 * @param userDateString The string in the form of "{@code dd.MM.yyyy HH:mm}" that should be parsed
	 * @return The date value
	 */
	public static Date convertStringToDate(String userDateString)
	{
		System.out.println("INPUT DATE: " + userDateString);
		
		DateTimeFormatter dateTimeConvert = DateTimeFormat.forPattern(inputFormat);
		
		DateTime dateTimeParser = dateTimeConvert.parseDateTime(userDateString);
		return dateTimeParser.toDate();
	}
	
	public static Date convertBirthdayStringToDate(String userDateString) 
	{
				
		String birthdayFormat = "dd.MM.yyyy";

		DateTimeFormatter dateTimeConvert = DateTimeFormat.forPattern(birthdayFormat);
		try{
			DateTime dateTimeParser = dateTimeConvert.parseDateTime(userDateString);
			return dateTimeParser.toDate();
		}
		catch(Exception e)
		{
			return null;
		}
		
	}

	/** Converts a {@link Date} to a {@link String} value.
	 * @param date The date that should be converted
	 * @return The date as a string "{@code dd.MM.yyyy HH:mm}"
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
	
	
	
	public static String convertBirthdayDateToString(Date date)
	{
		String birthdayFormat = "dd.MM.yyyy";
		
		SimpleDateFormat simpleDateConverter = new SimpleDateFormat(birthdayFormat);
		if(date!=null)
		{
			return simpleDateConverter.format(date);
		}
		else
		{
			return "-";
		}
	}
	
}
