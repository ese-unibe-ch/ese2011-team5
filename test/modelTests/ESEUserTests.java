package modelTests;

import java.util.ArrayList;
import models.*;
import org.junit.*;
import play.test.UnitTest;

public class ESEUserTests extends UnitTest
{

	ESEUser user1;
	ESEUser user2;

	@Before
	public void setUp() throws ESEException
	{
		ESEDatabase.clearAll();

		ESEDatabase.createUser("user 1", "pw 1", "firstName 1", "familyName 1");
		ESEDatabase.createUser("user 2", "pw 2", "firstName 2", "familyName 2");
		this.user1 = ESEDatabase.getUserByName("user 1");
		this.user2 = ESEDatabase.getUserByName("user 2");
	}

	@Test
	public void shouldInitialize()
	{
		assertTrue(this.user1 != null);
		assertTrue(this.user1.getCalendarList() != null);
		assertEquals(0, this.user1.getCalendarList().size());
		assertTrue(this.user1.getGroupList() != null);
		assertEquals(1, this.user1.getGroupList().size());
		assertEquals(0, this.user1.getUserID());
		assertEquals("user 1", this.user1.getName());
		assertEquals("pw 1", this.user1.getPassword());
		assertEquals("firstName 1", this.user1.getFirstName());
		assertEquals("familyName 1", this.user1.getFamilyName());
	}

	@Test(expected = ESEExceptionGuestUser.class)
	public void shouldReturnGuestUserException() throws ESEExceptionGuestUser
	{
		ESEDatabase.getCurrentUser();
	}

	@Test
	public void shouldAddCalendar() throws ESEException
	{
		this.user1.addCalendar("calendar1");
		this.user1.addCalendar("calendar2");
		assertEquals(2, this.user1.getCalendarList().size());
		assertTrue(this.user1.getCalendarList().get(0).getCalendarName().equals("calendar1"));
		assertTrue(this.user1.getCalendarList().get(1).getCalendarName().equals("calendar2"));
	}

	@Test
	public void shouldAddGroup() throws ESEException
	{
		this.user1.addGroup("group1");
		this.user1.addGroup("group2");
		assertEquals(3, this.user1.getGroupList().size());
		assertTrue(this.user1.getGroupList().get(0).getGroupName().equals("Friends"));
		assertTrue(this.user1.getGroupList().get(1).getGroupName().equals("group1"));
		assertTrue(this.user1.getGroupList().get(2).getGroupName().equals("group2"));
	}

	@Test
	public void shouldReturnCorrectCalendar() throws ESEException
	{
		this.user1.addCalendar("calendar1");
		this.user1.addCalendar("calendar2");
		ESECalendar testCalendar1 = this.user1.getCalendarList().get(0);
		ESECalendar testCalendar2 = this.user1.getCalendarList().get(1);
		ArrayList<ESECalendar> calendars = this.user1.getCalendarList();
		assertEquals(2, calendars.size());
		assertEquals(testCalendar2, this.user1.getCalendarList().get(1));
		assertEquals(testCalendar1, calendars.get(0));
	}

	@Test
	public void shouldGetGroupByName() throws ESEException
	{
		this.user1.addGroup("group1");
		this.user1.addGroup("group2");
		ESEGroup groupFriends = this.user1.getGroupByName("Friends");
		ESEGroup group1 = this.user1.getGroupByName("group1");
		ESEGroup group2 = this.user1.getGroupByName("group2");
		assertEquals(this.user1.getGroupList().get(0), groupFriends);
		assertEquals(this.user1.getGroupList().get(1), group1);
		assertEquals(this.user1.getGroupList().get(2), group2);
	}

	@Test
	public void shouldRemoveCalendar() throws ESEException
	{
		this.user1.addCalendar("calendar1");
		this.user1.addCalendar("calendar2");
		int testCalendar2ID = this.user1.getCalendarList().get(1).getID();
		this.user1.removeCalendar(testCalendar2ID);
		assertEquals(1, this.user1.getCalendarList().size());
		assertEquals("calendar1", this.user1.getCalendarList().get(0).getCalendarName());
	}

	@Test
	public void shouldGetAllowedEvents() throws Throwable
	{
		this.user1.addCalendar("calendar1");
		this.user1.addCalendar("calendar2");
		ESECalendar testCalendar1 = this.user1.getCalendarList().get(0);
		testCalendar1.addEvent("testEvent1", "12.11.2011 16:00", "14.11.2011 18:00", true);
		testCalendar1.addEvent("testEvent2", "15.11.2011 13:40", "16.11.2011 13:00", false);

		ArrayList<ESEEvent> publicEvents = this.user1.getAllPublicEvents(testCalendar1.getID());
		ArrayList<ESEEvent> allowedEvents = testCalendar1.getAllAllowedEvents();

		ArrayList<ESEEvent> onlyAllowedEvents = testCalendar1.getAllAllowedEvents();

		assertNotSame(allowedEvents, onlyAllowedEvents);
		assertEquals(publicEvents, onlyAllowedEvents);
	}

	@Test
	public void shouldEditProfile()
	{
		this.user1.setBirthday("16.11.2011");
		this.user1.setCity("Here");
		this.user1.setFamilyName("Test");
		this.user1.setFirstName("User");
		this.user1.setMail("-");
		this.user1.setPostCode("0000");
		this.user1.setStateMessage("I was here!");
		this.user1.setStreet("Street");
		this.user1.setUsername("TestUser");

		assertEquals("16.11.2011", this.user1.getBirthdayString());
		assertEquals("Here", this.user1.getCity());
		assertEquals("Test", this.user1.getFamilyName());
		assertEquals("User", this.user1.getFirstName());
		assertEquals("-", this.user1.getMail());
		assertEquals("0000", this.user1.getPostCode());
		assertEquals("I was here!", this.user1.getStateMessage());
		assertEquals("Street", this.user1.getStreet());
		assertEquals("TestUser", this.user1.getName());
	}
}
