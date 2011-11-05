package modelTests;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import models.*;

import org.junit.*;

import play.test.UnitTest;

public class ESEUserTests extends UnitTest{
	
	ESEUser user1;
	ESEUser user2;
	
	@Before
	public void setUp(){
		this.user1 = new ESEUser("user 1", "pw 1", "firstName 1", "familyName 1");
		this.user2 = new ESEUser("user 2", "pw 2", "firstName 2", "familyName 2");
	}
	
	@Test
	public void shouldInitialize(){
		assertTrue(this.user1 != null);
		assertTrue(this.user1.getCalendarList() != null);
		assertEquals(0,this.user1.getCalendarList().size());
		assertTrue(this.user1.getGroupList() != null);
		assertEquals(0,this.user1.getGroupList().size());
		assertEquals(0, this.user1.getUserID());
		assertEquals("user 1", this.user1.getName());
		assertEquals("pw 1", this.user1.getPassword());
		assertEquals("firstName 1", this.user1.getFirstName());
		assertEquals("familyName 1", this.user1.getFamilyName());
	}
	
	@Test
	public void shouldAddCalendar(){
		this.user1.addCalendar("calendar1");
		this.user1.addCalendar("calendar2");
		assertEquals(2, this.user1.getCalendarList().size());
		assertTrue(this.user1.getCalendarList().get(0).getCalendarName().equals("calendar1"));
		assertTrue(this.user1.getCalendarList().get(1).getCalendarName().equals("calendar2"));	
	}
	
	@Test
	public void shouldAddGroup(){
		this.user1.addGroup("group1");
		this.user1.addGroup("group2");
		assertEquals(2, this.user1.getGroupList().size());
		assertTrue(this.user1.getGroupList().get(0).getGroupName().equals("group1"));
		assertTrue(this.user1.getGroupList().get(1).getGroupName().equals("group2"));
	}
}
