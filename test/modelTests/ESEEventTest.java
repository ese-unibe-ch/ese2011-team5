package modelTests;

import java.util.Calendar;
import java.util.GregorianCalendar;

import models.*;

import org.junit.*;

import play.test.Fixtures;
import play.test.UnitTest;
import utils.ESEConversionHelper;

public class ESEEventTest extends UnitTest {

	private ESEEvent testEvent1;
	private ESEEvent testEvent2;
	private ESEUser testUser;
	private ESECalendar testCalendar;

	@Before
	public void setUp() {
		this.testUser = ESEFactory.createUser("testUser", "pw");
		this.testCalendar = ESEFactory.createCalendar("TestCalendarName", testUser);
		this.testEvent1 = ESEFactory.createEvent("testEvent1", "25.10.2011 08:15",
				"25.10.2011 10:00", testCalendar, "false");
		this.testEvent2 = ESEFactory.createEvent("testEvent2", "26.10.2011 08:15",
				"26.10.2011 10:00", testCalendar, "true");
	}

	@Test
	public void shouldReturnCorrectName() {
		assertEquals(testEvent1.getEventName(), "testEvent1");
	}

	@Test
	public void shouldReturnCorrectDates() {
		Calendar startDateToTest = new GregorianCalendar(2011,
				Calendar.OCTOBER, 25, 8, 15);
		Calendar endDateToTest = new GregorianCalendar(2011, Calendar.OCTOBER,
				25, 10, 0);

		assertEquals(testEvent1.getStartDate().getTime(),
				startDateToTest.getTimeInMillis());
		assertEquals(testEvent1.getEndDate().getTime(),
				endDateToTest.getTimeInMillis());
	}

	@Test
	public void shouldReturnCorrectPrivacyStatus() {
		assertTrue(testEvent1.isPublic() == false);
		assertFalse(testEvent1.isPublic() == true);

		assertEquals(testEvent2.isPublic(), true);
	}
	
	@Test
	public void shouldModifyEvent()
	{
		ESEFactory.createUser("User", "Pass");
		ESEUser testUser = ESEUser.find("byUsername", "User").first();
		
		ESECalendar testCal = ESEFactory.createCalendar("testCal", testUser);
		
		ESEEvent eventToModify = ESEFactory.createEvent("unmodified", 
		"25.10.2011 00:00", "26.10.2011 00:00", testCal, "true");
		
		assertEquals("unmodified", eventToModify.getEventName());
		assertEquals("25.10.2011 00:00", 
		ESEConversionHelper.convertDateToString(eventToModify.getStartDate()));
		assertEquals("26.10.2011 00:00", 
		ESEConversionHelper.convertDateToString(eventToModify.getEndDate()));
		assertTrue(eventToModify.isPublic());
		
		eventToModify.editEventName("modified event");
		eventToModify.editStartDate("25.10.2011 12:00");
		eventToModify.editEndDate("26.10.2011 12:00");
		eventToModify.editVisibility("false");
	
		assertEquals("modified event", eventToModify.getEventName());
		assertEquals("25.10.2011 12:00", 
		ESEConversionHelper.convertDateToString(eventToModify.getStartDate()));
		assertEquals("26.10.2011 12:00", 
		ESEConversionHelper.convertDateToString(eventToModify.getEndDate()));
		assertFalse(eventToModify.isPublic());
	}
}
