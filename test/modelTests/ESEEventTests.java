package modelTests;

import static org.junit.Assert.*;
import java.util.Calendar;
import java.util.Date;
import models.*;
import org.junit.*;
import play.test.UnitTest;

public class ESEEventTests extends UnitTest
{

	Calendar cal = Calendar.getInstance();
	Date startDate;
	Date endDate;

	ESEEvent event1;
	ESEEvent event2;
	ESEEvent event3;
	ESEUser dummyUser;
	ESECalendar dummyCal;

	@Before
	public void setUp() throws ESEException		/* Sets up three Events for testing purposes */
	{
		ESEDatabase.clearAll();

		this.dummyUser = new ESEUser("TestUser", "testpass", "Dummy", "Test");
		this.dummyCal = new ESECalendar("Dummy", this.dummyUser); /*for the moment id = 0*/

		this.cal.set(2011, 11, 24, 18, 00);
		this.startDate = this.cal.getTime();
		this.cal.set(2011, 11, 24, 23, 00);
		this.endDate = this.cal.getTime();
		this.event1 = new ESEEvent("Weihnachten", this.dummyCal, this.startDate, this.endDate, false);

		this.cal.set(2011, 11, 6, 18, 00);
		this.startDate = this.cal.getTime();
		this.cal.set(2011, 11, 24, 19, 00);
		this.endDate = this.cal.getTime();
		this.event2 = new ESEEvent("Samichlaus", this.dummyCal, this.startDate, this.endDate, true);

		this.cal.set(2011, 11, 24, 16, 00);
		this.startDate = this.cal.getTime();
		this.cal.set(2012, 01, 8, 18, 00);
		this.endDate = this.cal.getTime();
		this.event3 = new ESEEvent("Winter Ferien", this.dummyCal, this.startDate, this.endDate, true);
	}

	@Test
	public void shouldBeInitialized()
	{
		/*Event 1*/
		assertTrue(this.event1 != null);
		assertEquals(0, this.event1.getEventID());
		assertTrue("Weihnachten".equals(this.event1.getEventName()));
		this.cal.set(2011, 11, 24, 18, 00);
		this.startDate = this.cal.getTime();
		assertEquals(this.startDate, this.event1.getStartDate());
		this.cal.set(2011, 11, 24, 23, 00);
		this.endDate = this.cal.getTime();
		assertEquals(this.endDate, this.event1.getEndDate());
		assertEquals(this.dummyCal, this.event1.getCorrespondingCalendar());

		/*Event 2*/
		assertTrue(this.event2 != null);
		assertEquals(1, this.event2.getEventID());
		assertTrue("Samichlaus".equals(this.event2.getEventName()));
		this.cal.set(2011, 11, 6, 18, 00);
		this.startDate = this.cal.getTime();
		assertEquals(this.startDate, this.event2.getStartDate());
		this.cal.set(2011, 11, 24, 19, 00);
		this.endDate = this.cal.getTime();
		assertEquals(this.endDate, this.event2.getEndDate());
		assertEquals(this.dummyCal, this.event2.getCorrespondingCalendar());

		/*Event 3*/
		assertTrue(this.event3 != null);
		assertEquals(2, this.event3.getEventID());
		assertTrue("Winter Ferien".equals(this.event3.getEventName()));
		this.cal.set(2011, 11, 24, 16, 00);
		this.startDate = this.cal.getTime();
		assertEquals(this.startDate, this.event3.getStartDate());
		this.cal.set(2012, 01, 8, 18, 00);
		this.endDate = this.cal.getTime();
		assertEquals(this.endDate, this.event3.getEndDate());
		assertEquals(this.dummyCal, this.event3.getCorrespondingCalendar());
	}

	@Test
	public void isEventDay()
	{
		dummyCal.addEvent(event1);
		dummyCal.addEvent(event2);
		dummyCal.addEvent(event3);
		assertTrue(dummyCal.getAllEvents().get(0).isEventDay(24, 11, 2011));
		assertTrue(dummyCal.getAllEvents().get(1).isEventDay(24, 11, 2011));
		assertFalse(dummyCal.getAllEvents().get(1).isEventDay(1, 11, 2011));
		assertFalse(dummyCal.getAllEvents().get(1).isEventDay(25, 11, 2011));
		assertTrue(dummyCal.getAllEvents().get(2).isEventDay(24, 11, 2011));
		assertTrue(dummyCal.getAllEvents().get(2).isEventDay(25, 11, 2011));
	}
}
