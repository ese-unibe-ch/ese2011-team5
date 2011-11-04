package modelTests;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import models.*;

import org.junit.*;

import play.test.UnitTest;

public class ESECalendarTest extends UnitTest {

	public ESECalendar testCalendar;
	public ESEUser testUser;

	@Before
	public void setUp() {
		testUser = ESEFactory.createUser("testUser", "pw");
		testCalendar = ESEFactory.createCalendar("TestCalendarName", testUser);

		testCalendar.addEvent("TestEvent1", "20.10.2011 20:00",
				"20.10.2011 20:30", "true");
		testCalendar.addEvent("TestEvent2", "21.10.2011 20:00",
				"21.10.2011 20:30", "false");
		testCalendar.addEvent("TestEvent3", "22.10.2011 20:00",
				"22.10.2011 20:30", "true");

		assertEquals(3, testCalendar.getAllEventsAsList().size());
	}

	@Test
	public void shouldReturnCorrectName() {
		assertEquals(testCalendar.getCalendarName(), "TestCalendarName");
	}

	@Test
	public void shouldReturnCorrectOwner() {
		ESECalendar testCalendar2 = ESEFactory.createCalendar("TestCalendarName", testUser);
		assertEquals(testCalendar2.owner, testUser);
	}

	@Test
	public void shouldComplainIfOverlappingEvents() {
		// TODO
	}

	@Test
	public void shouldAddAndReturnNewEvent() {
		testCalendar.addEvent("TestEvent4", "25.10.2011 20:00",
				"25.10.2011 20:30", "true");
		assertTrue(testCalendar.eventList.get(3).getEventName().equals("TestEvent4"));
		assertTrue(testCalendar.eventList.get(3).isPublic = true);
	}

	@Test
	public void shouldCorrectlyRenameCalendar() {
		testCalendar.editCalendarName("RenamedTestCalendar");
		assertTrue(testCalendar.getCalendarName().equals("RenamedTestCalendar"));
		assertFalse(testCalendar.getCalendarName().equals("TestCalendarName"));
	}

	@Test
	public void shouldReturnAllEventsOfCertainDay() {
		List<ESEEvent> testList = testCalendar
		.getListOfEventsRunningAtDay("20.10.2011 13:00", false);
		assertTrue(testList.size() == 1);

		testCalendar.addEvent("TestEvent5", "20.10.2011 14:00",
				"20.10.2011 15:00", "false");
		testList = testCalendar.getListOfEventsRunningAtDay("20.10.2011 13:00", false);
		assertTrue(testList.size() == 2);
		assertEquals(testList.get(0).getEventName(), "TestEvent1");
		assertEquals(testList.get(1).getEventName(), "TestEvent5");
	}

	@Test
	public void shouldReturnAllAndOnlyPublicEventsOfCertainDay() {
		List<ESEEvent> testList = testCalendar.getListOfEventsRunningAtDay(
				"20.10.2011 13:00", true);
		assertTrue(testList.size() == 1);

		testList.addAll(testCalendar.getListOfEventsRunningAtDay(
				"21.10.2011 13:00", true));

		assertTrue(testList.size() == 1);

		testCalendar.addEvent("TestEvent5", "25.10.2011 14:00",
				"25.10.2011 15:00", "false");
		testList.addAll(testCalendar.getListOfEventsRunningAtDay(
				"25.10.2011 13:00", true));

		assertTrue(testList.size() == 1);

		testCalendar.addEvent("TestEvent6", "26.10.2011 14:00",
				"26.10.2011 15:00", "true");
		testList.addAll(testCalendar.getListOfEventsRunningAtDay(
				"26.10.2011 13:00", true));

		assertTrue(testList.size() == 2);

		assertEquals(testList.get(0).getEventName(), "TestEvent1");
		assertEquals(testList.get(1).getEventName(), "TestEvent6");
	}

	@Test
	public void shouldReturnAllEventsOfCertainDayGivenDate() {

		Calendar compareDateCalendar = new GregorianCalendar(2011,
				Calendar.OCTOBER, 20, 15, 00);
		Date compareDate = compareDateCalendar.getTime();

		List<ESEEvent> testList = testCalendar.getListOfEventsRunningAtDay(
				compareDate, false);
		assertTrue(testList.size() == 1);

		testCalendar.addEvent("TestEvent5", "20.10.2011 14:00",
				"20.10.2011 15:00", "false");
		testList = testCalendar.getListOfEventsRunningAtDay(compareDate, false);
		assertTrue(testList.size() == 2);
		assertEquals(testList.get(0).getEventName(), "TestEvent1");
		assertEquals(testList.get(1).getEventName(), "TestEvent5");
	}

	@Test
	public void shouldReturnAllAndOnlyPublicEventsOfCertainDayGivenDate() {

		Calendar compareDateCalendar = new GregorianCalendar(2011,
				Calendar.OCTOBER, 21, 15, 00);
		Date twentyfirst = compareDateCalendar.getTime();
		compareDateCalendar = new GregorianCalendar(2011, Calendar.OCTOBER, 25,
				15, 00);
		Date twentyfifth = compareDateCalendar.getTime();
		compareDateCalendar = new GregorianCalendar(2011, Calendar.OCTOBER, 26,
				15, 00);
		Date twentysixt = compareDateCalendar.getTime();

		List<ESEEvent> testList = testCalendar.getListOfEventsRunningAtDay(
				"20.10.2011 13:00", true);
		assertTrue(testList.size() == 1);

		testList.addAll(testCalendar.getListOfEventsRunningAtDay(twentyfirst,
				true));

		assertTrue(testList.size() == 1);

		testCalendar.addEvent("TestEvent5", "25.10.2011 14:00",
				"25.10.2011 15:00", "false");
		testList.addAll(testCalendar.getListOfEventsRunningAtDay(twentyfifth,
				true));

		assertTrue(testList.size() == 1);

		testCalendar.addEvent("TestEvent6", "26.10.2011 14:00",
				"26.10.2011 15:00", "true");
		testList.addAll(testCalendar.getListOfEventsRunningAtDay(twentysixt,
				true));

		assertTrue(testList.size() == 2);

		assertEquals(testList.get(0).getEventName(), "TestEvent1");
		assertEquals(testList.get(1).getEventName(), "TestEvent6");
	}

	@Test
	public void shouldReturnIteratorOfEventsOfCertainDay() {
		// TODO
	}

	@Test
	public void shouldReturnIteratorOfPublicEventsOfCertainDay() {
		// TODO
	}

	@Test
	public void shouldReturnListOfAllEvents() {
		assertTrue(testCalendar.getAllEventsAsList().get(0).getEventName()
				.equals("TestEvent1"));
		assertTrue(testCalendar.getAllEventsAsList().get(1).getEventName()
				.equals("TestEvent2"));
		assertTrue(testCalendar.getAllEventsAsList().get(2).getEventName()
				.equals("TestEvent3"));
	}

	@Test
	public void shouldReturnListOfAllPublicEvents() {

		assertTrue(testCalendar.getPublicEventsAsList().size() == 2);

		assertTrue(testCalendar.getPublicEventsAsList().get(0).getEventName()
				.equals("TestEvent1"));
		assertTrue(testCalendar.getPublicEventsAsList().get(1).getEventName()
				.equals("TestEvent3"));
	}

	@Test
	public void shouldReturnIteratorOfAllEvents() {
		// TODO
	}

	@Test
	public void shouldReturnIteratorOfAllPublicEvents() {
		// TODO
	}

}
