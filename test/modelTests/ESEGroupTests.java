package modelTests;

import java.util.Calendar;
import java.util.Date;

import models.*;

import org.junit.*;

import play.test.UnitTest;

public class ESEGroupTests extends UnitTest{
	
	ESEGroup group1;
	ESEUser dummyOwner = new ESEUser("dummy", "pw", "firstName", "familyName");
	
	
	@Before
	public void setUp(){
		group1 = new ESEGroup("Testgroup1", dummyOwner);
	}
	
	@Test
	public void shouldInitialize(){
		assertTrue(group1 != null);
		assertEquals(0, group1.getGroupID());
		assertEquals("Testgroup1", group1.getGroupName());
		assertEquals(dummyOwner, group1.getOwner());
	}

}
