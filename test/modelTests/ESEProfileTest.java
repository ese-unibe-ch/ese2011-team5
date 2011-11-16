package modelTests;

import models.*;
import org.junit.*;
import play.test.UnitTest;

public class ESEProfileTest extends UnitTest
{

	ESEUser user1;

	@Before
	public void setUp() throws ESEException
	{
		ESEDatabase.clearAll();
 
		ESEDatabase.createUser("user profile", "pw 1", "firstName 1", "familyName 1");
		this.user1 = ESEDatabase.getUserByName("user profile");
	}

	@Test
	public void shouldEditProfile()
	{
		ESEProfile user1Profile = this.user1.getProfile();

		user1Profile.setBirthday("16.11.2011");
		user1Profile.setCity("Here");
		user1Profile.setFamilyName("Test");
		user1Profile.setFirstName("User");
		user1Profile.setMail("-");
		user1Profile.setPostCode("0000");
		user1Profile.setStateMessage("I was here!");
		user1Profile.setStreet("Street");
		user1Profile.setUsername("TestUser");

		assertEquals("16.11.2011", user1Profile.getBirthdayString());
		assertEquals("Here", user1Profile.getCity());
		assertEquals("Test", user1Profile.getFamilyName());
		assertEquals("User", user1Profile.getFirstName());
		assertEquals("-", user1Profile.getMail());
		assertEquals("0000", user1Profile.getPostCode());
		assertEquals("I was here!", user1Profile.getStateMessage());
		assertEquals("Street", user1Profile.getStreet());
		assertEquals("TestUser", user1Profile.getUsername());
	}
}
