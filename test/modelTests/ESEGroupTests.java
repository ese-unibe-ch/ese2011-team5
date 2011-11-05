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
		this.group1 = new ESEGroup("Testgroup1", this.dummyOwner);
	}
	
	@Test
	public void shouldInitialize(){
		assertTrue(this.group1 != null);
		assertEquals(0, this.group1.getGroupID());
		assertEquals("Testgroup1", this.group1.getGroupName());
		assertEquals(this.dummyOwner, this.group1.getOwner());
	}

}
