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
		DateTimeFormatter dateTimeConvert = DateTimeFormat.forPattern(inputFormat);
		DateTime dateTimeParser = dateTimeConvert.parseDateTime(userDateString);
		return dateTimeParser.toDate();
	}

	/** Converts a {@link Date} to a {@link String} value.
	 * @param date The date that should be converted
	 * @return The date as a string "{@code dd.MM.yyyy HH:mm}"
	 */
	public static String convertDateToString(Date date)
	{
		SimpleDateFormat simpleDateConverter = new SimpleDateFormat(inputFormat);
		return simpleDateConverter.format(date);
	}
}
