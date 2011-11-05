package modelTests;

import java.util.Calendar;
import java.util.Date;

import models.ESECalendar;
import models.ESEEvent;
import models.ESEUser;

import org.junit.Before;
import org.junit.Test;

import play.test.UnitTest;

public class ESEUserTests extends UnitTest{
	
	ESEUser user1;
	
	@Before
	public void setUp(){
		user1 = new ESEUser(0, "user", "pw", "firstName", "familyName");
	}
	
	@Test
	public void shouldInitialize(){
		assertTrue(user1 != null);
		assert
	}
	
	@Test
	public void shouldAddCalendar(){
		
	}
	
	@Test
	public void shouldAddGroup(){
		
	}
	@Test
	public void shouldGetAllEvents(){
		
	}
	@Test
	public void shouldGetAllPublicEvents(){
		
	}
	
}
