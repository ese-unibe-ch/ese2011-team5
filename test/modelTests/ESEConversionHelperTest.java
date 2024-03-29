package modelTests;

import static org.junit.Assert.*;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import models.*;
import org.junit.*;

public class ESEConversionHelperTest
{

	@Test
	public void shouldCorrectlyConvertStringToDate()
	{
		Date date = ESEConversionHelper.convertStringToDate("13.04.2011 14:00");

		Calendar cal = new GregorianCalendar();
		cal.setTime(date);

		assertEquals(13, cal.get(cal.DAY_OF_MONTH));
	}

	@Test
	public void shouldCorrectlyConvertDateToString()
	{
		Date date = ESEConversionHelper.convertStringToDate("13.04.2011 14:00");

		String dateAsString = ESEConversionHelper.convertDateToString(date);

		assertEquals("13.04.2011 14:00", dateAsString);
	}

	@Test
	public void shouldCorrectlyConvertBirthdayStringToDate()
	{
		Date date = ESEConversionHelper.convertBirthdayStringToDate("13.04.2011");

		Calendar cal = new GregorianCalendar();
		cal.setTime(date);

		assertEquals(13, cal.get(cal.DAY_OF_MONTH));
	}

	@Test
	public void shouldCorrectlyConvertBirthdayDateToString()
	{
		Date date = ESEConversionHelper.convertBirthdayStringToDate("13.04.2011");

		String dateAsString = ESEConversionHelper.convertBirthdayDateToString(date);

		assertEquals("13.04.2011", dateAsString);
	}
}
