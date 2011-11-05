package modelTests;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import models.ESECalendar;
import models.ESEEvent;
import models.ESEGroup;
import models.ESEUser;

import org.junit.Before;
import org.junit.Test;

import play.test.UnitTest;

public class ESEUserTests extends UnitTest{
	
	ESEUser user1;
	ESEUser user2;
	
	@Before
	public void setUp(){
		user1 = new ESEUser(0, "user 1", "pw 1", "firstName 1", "familyName 1");
		user2 = new ESEUser(1, "user 2", "pw 2", "firstName 2", "familyName 2");

	}
	
	@Test
	public void shouldInitialize(){
		assertTrue(user1 != null);
		assertTrue(user1.getCalendarList() != null);
		assertEquals(0,user1.getCalendarList().size());
		assertTrue(user1.getGroupList() != null);
		assertEquals(0,user1.getGroupList().size());
		assertEquals(0, user1.getUserID());
		assertEquals("user 1", user1.getName());
		assertEquals("pw 1", user1.getPassword());
		assertEquals("firstName 1", user1.getFirstName());
		assertEquals("familyName 1", user1.getFamilyName());
	}
	
	@Test
	public void shouldAddCalendar(){
		ESECalendar cal1 = new ESECalendar(0, "cal 1", user1);
		ESECalendar cal2 = new ESECalendar(1, "cal 2", user1);
		user1.addCalendar(cal1);
		user1.addCalendar(cal2);
		assertEquals(2, user1.getCalendarList().size());
		assertTrue(user1.getCalendarList().contains(cal1));
		assertTrue(user1.getCalendarList().contains(cal2));
		
	}
	
	@Test
	public void shouldAddGroup(){
		ESEGroup group1 = new ESEGroup(0, "group1", user1);
		ESEGroup group2 = new ESEGroup(1, "group2", user1);
		user1.addGroup(group1);
		user1.addGroup(group2);
		assertEquals(2, user1.getGroupList().size());
		assertTrue(user1.getGroupList().contains(group1));
		assertTrue(user1.getGroupList().contains(group2));
	}
	
	@Test
	public void shouldGetAllEvents(){
		/*prepare a ESECalendar */
		ESECalendar cal1 = new ESECalendar(0, "cal 1", user1);
		user1.addCalendar(cal1);
		
		/*create some Events*/
		Calendar cal = Calendar.getInstance();
		cal.set(2011, 11, 24, 18, 00);
		Date startDate = cal.getTime();
		cal.set(2011, 11, 24, 23, 00);
		Date endDate = cal.getTime();
		ESEEvent event1 = new ESEEvent("Event1", cal1,startDate ,endDate , true);
		
		cal.set(2011, 11, 25, 18, 00);
		startDate = cal.getTime();
		cal.set(2011, 11, 25, 23, 00);
		endDate = cal.getTime();
		ESEEvent event2 = new ESEEvent("Event2", cal1,startDate ,endDate , false);
		
		cal.set(2011, 11, 26, 18, 00);
		startDate = cal.getTime();
		cal.set(2011, 11, 26, 23, 00);
		endDate = cal.getTime();
		ESEEvent event3 = new ESEEvent("Event3", cal1,startDate ,endDate , false);
		
		cal1.addEvent(event1);
		cal1.addEvent(event2);
		cal1.addEvent(event3);
		
		ArrayList<ESEEvent> eventList = user1.getAllEvents(0);
		
		assertTrue(eventList != null);
		assertEquals(3,eventList.size());
		assertTrue(eventList.contains(event1));
		assertTrue(eventList.contains(event2));
		assertTrue(eventList.contains(event3));
	}
	@Test
	public void shouldGetAllPublicEvents(){
		/*copy past setup from last test*/
		
		/*prepare a ESECalendar */
		ESECalendar cal1 = new ESECalendar(0, "cal 1", user1);
		user1.addCalendar(cal1);
		
		/*create some Events*/
		Calendar cal = Calendar.getInstance();
		cal.set(2011, 11, 24, 18, 00);
		Date startDate = cal.getTime();
		cal.set(2011, 11, 24, 23, 00);
		Date endDate = cal.getTime();
		ESEEvent event1 = new ESEEvent("Event1", cal1,startDate ,endDate , true);
		
		cal.set(2011, 11, 25, 18, 00);
		startDate = cal.getTime();
		cal.set(2011, 11, 25, 23, 00);
		endDate = cal.getTime();
		ESEEvent event2 = new ESEEvent("Event2", cal1,startDate ,endDate , false);
		
		cal.set(2011, 11, 26, 18, 00);
		startDate = cal.getTime();
		cal.set(2011, 11, 26, 23, 00);
		endDate = cal.getTime();
		ESEEvent event3 = new ESEEvent("Event3", cal1,startDate ,endDate , false);
		
		cal1.addEvent(event1);
		cal1.addEvent(event2);
		cal1.addEvent(event3);
		
		ArrayList<ESEEvent> publicEventList = user1.getAllPublicEvents(0);
		assertTrue(publicEventList != null);
		assertEquals(1, publicEventList.size());
		assertTrue(publicEventList.contains(event1));
		assertFalse(publicEventList.contains(event2));
		assertFalse(publicEventList.contains(event3));

	}
	
}
