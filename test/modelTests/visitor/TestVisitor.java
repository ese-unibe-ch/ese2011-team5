package modelTests.visitor;

import java.util.List;
import models.ESECalendar;
import models.ESEDatabase;
import models.ESEEvent;
import models.ESEException;
import models.ESEUser;
import models.visitor.SearchCalendarVisitor;
import models.visitor.SearchEventVisitor;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.junit.Before;
import org.junit.Test;
import play.test.UnitTest;

public class TestVisitor extends UnitTest
{
	DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy.MM.dd HH:mm");
	private ESEUser user;
	private ESECalendar myCalendar, mySportCalendar;
	private ESEEvent event1, event2;
	private DateTime startDate1;
	private DateTime endDate1;
	private DateTime startDate2;
	private DateTime endDate2;

	@Before
	public void setUp() throws ESEException
	{
		ESEDatabase.clearAll();
		this.user = new ESEUser("dummy", "pw", "firstName", "familyName");

		this.user.addCalendar("My Calendar");
		this.user.addCalendar("My Sport Calendar");

		this.myCalendar = this.user.getCalendarByName("My Calendar");
		this.mySportCalendar = this.user.getCalendarByName("My Sport Calendar");

		this.startDate1 = this.formatter.parseDateTime("2011.12.24 08:00");
		this.endDate1 = this.formatter.parseDateTime("2011.12.24 12:00");

		this.startDate2 = this.formatter.parseDateTime("2013.12.24 08:00");
		this.endDate2 = this.formatter.parseDateTime("2013.12.24 12:00");

		this.event1 = new ESEEvent("Weihnachten 2010", this.myCalendar,
				this.startDate1.toDate(), this.endDate1.toDate(), false);
		this.myCalendar.addEvent(this.event1);

		this.event2 = new ESEEvent("Weihnachten 2011", this.myCalendar,
				this.startDate2.toDate(), this.endDate2.toDate(), false);
		this.myCalendar.addEvent(this.event2);

	}

	@Test
	public void testSearchEventWithCalendarAndEventName()
	{
		SearchEventVisitor visitor = new SearchEventVisitor("Weihnachten");
		this.myCalendar.accept(visitor);

		List<ESEEvent> events = visitor.results();

		assertTrue(events.contains(this.event1));
		assertTrue(events.contains(this.event2));
	}

	@Test
	public void testSearchEventWithLimits()
	{
		DateTime lowerLimit = this.formatter.parseDateTime("2010.12.24 8:00");
		DateTime upperLimit = this.formatter.parseDateTime("2012.12.24 12:00");

		SearchEventVisitor visitor = new SearchEventVisitor("Weihnachten", lowerLimit, upperLimit);
		this.myCalendar.accept(visitor);

		List<ESEEvent> events = visitor.results();

		assertTrue(events.contains(this.event1));
		assertFalse(events.contains(this.event2));
	}

	@Test
	public void testSearchEventWithLimits2()
	{
		DateTime lowerLimit = this.formatter.parseDateTime("2011.12.20 8:00");
		DateTime upperLimit = this.formatter.parseDateTime("2011.12.30 12:00");

		SearchEventVisitor visitor = new SearchEventVisitor("Weihnachten", lowerLimit, upperLimit);
		this.user.accept(visitor);

		List<ESEEvent> events = visitor.results();

		assertTrue(events.contains(this.event1));
		assertFalse(events.contains(this.event2));
	}

	@Test
	public void testSearchEventWithUserAndEventName()
	{
		SearchEventVisitor visitor = new SearchEventVisitor("Weihnachten");
		this.user.accept(visitor);

		List<ESEEvent> events = visitor.results();

		assertTrue(events.contains(this.event1));
		assertTrue(events.contains(this.event2));
	}

	@Test
	public void testSearchEventWithUserAndEventId()
	{
		SearchEventVisitor visitor = new SearchEventVisitor(this.event1.getEventID());
		this.user.accept(visitor);

		List<ESEEvent> events = visitor.results();

		assertTrue(events.contains(this.event1));
		assertFalse(events.contains(this.event2));
	}

	@Test
	public void testSearchCalendarWithUserAndCalendarName()
	{
		SearchCalendarVisitor visitor = new SearchCalendarVisitor("Calendar");
		this.user.accept(visitor);

		List<ESECalendar> calendars = visitor.results();

		assertTrue(calendars.contains(this.myCalendar));
		assertTrue(calendars.contains(this.mySportCalendar));
	}

	@Test
	public void testSearchCalendarWithUserAndCalendarName2()
	{
		SearchCalendarVisitor visitor = new SearchCalendarVisitor("My Calendar");
		this.user.accept(visitor);

		List<ESECalendar> calendars = visitor.results();

		assertTrue(calendars.contains(this.myCalendar));
		assertFalse(calendars.contains(this.mySportCalendar));
	}

	@Test
	public void testSearchCalendarWithUserAndCalendarId()
	{
		SearchCalendarVisitor visitor = new SearchCalendarVisitor(this.myCalendar.getID());
		this.user.accept(visitor);

		List<ESECalendar> calendars = visitor.results();

		assertTrue(calendars.contains(this.myCalendar));
		assertFalse(calendars.contains(this.mySportCalendar));
	}
}
