package modelTests;

import java.util.Calendar;
import java.util.Date;

import models.ESECalendar;
import models.ESEEvent;
import models.ESEGroup;
import models.ESEUser;

import org.junit.Before;
import org.junit.Test;

import play.test.UnitTest;

public class ESEGroupTests extends UnitTest{
	
	ESEGroup group1;
	ESEUser dummyOwner = new ESEUser("dummy", "pw", "firstName", "familyName");
	
	
	@Before
	public void setUp(){
		group1 = new ESEGroup(0, "Group 1", dummyOwner);
	}
	
	@Test
	public void shouldInitialize(){
		assertTrue(group1 != null);
		assertEquals(0, group1.getGroupID());
		assertEquals("Group 1", group1.getGroupName());
		assertEquals(dummyOwner, group1.getOwner());
	}

}
