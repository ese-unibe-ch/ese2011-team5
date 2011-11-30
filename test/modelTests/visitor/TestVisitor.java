package modelTests.visitor;

import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;

import models.ESECalendar;
import models.ESEDatabase;
import models.ESEEvent;
import models.ESEException;
import models.ESEUser;
import models.visitor.SearchEventVisitor;

import org.junit.Before;
import org.junit.Test;

import play.test.UnitTest;

public class TestVisitor extends UnitTest {
	Calendar cal = Calendar.getInstance();
	private ESEUser user;
	private ESECalendar calendar;
	private ESEEvent event1, event2;
	private Date startDate;
	private Date endDate;

	@Before
	public void setUp() throws ESEException {
		ESEDatabase.clearAll();
		this.user = new ESEUser("dummy", "pw", "firstName", "familyName");
		this.calendar = new ESECalendar("Calendar", user);
		this.startDate = this.cal.getTime();
		this.cal.set(2011, 11, 24, 23, 00);
		this.endDate = this.cal.getTime();

		this.event1 = new ESEEvent("Weihnachten 2010", this.calendar,
				this.startDate, this.endDate, false);
		this.calendar.addEvent(event1);

		this.event2 = new ESEEvent("Weihnachten 2011", this.calendar,
				this.startDate, this.endDate, false);
		this.calendar.addEvent(event2);

		this.user.addCalendar(this.calendar);

	}

	@Test
	public void testVisitorForCalendar() {
		SearchEventVisitor visitor = new SearchEventVisitor("Weihnachten");
		this.calendar.accept(visitor);

		Iterator<ESEEvent> iterator = visitor.results();
		assertTrue(iterator.hasNext());
	}

	@Test
	public void testVisitorForUser() {
		SearchEventVisitor visitor = new SearchEventVisitor("Weihnachten");
		this.user.accept(visitor);

		Iterator<ESEEvent> iterator = visitor.results();
		assertTrue(iterator.hasNext());
	}

}
