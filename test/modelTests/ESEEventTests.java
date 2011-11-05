package modelTests;

import models.ESEEvent;

import org.junit.Before;

import play.test.UnitTest;

public class ESEEventTests extends UnitTest{

	public static int ids = 0;
	@Before
	public void setUp(){
		ESEEvent event1 = new ESEEvent("Event 1", null, null, null, true);
	}
	
}
