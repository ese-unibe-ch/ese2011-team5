package modelTests;

import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import models.*;
import org.junit.*;
import play.test.UnitTest;

public class ESEDatabaseTest extends UnitTest
{

	ESEUser setupUser;

	@Before
	public void setUp() throws ESEException
	{
		ESEDatabase.clearAll();

		ESEDatabase.createUser("DB User", "pw", "firstName", "familyName");
		assertEquals(1, ESEDatabase.getAllUsers().size());
		this.setupUser = ESEDatabase.getAllUsers().get(0);
	}

	@Test
	public void shouldReturnCorrectUser() throws ESEException
	{
		assertEquals(this.setupUser, ESEDatabase.getUserByID(0));
		assertEquals(this.setupUser, ESEDatabase.getUserByName("DB User"));
	}

	@Test(expected = ESEException.class)
	public void usernameShouldbeCaseSensitive() throws ESEException
	{
		ESEDatabase.getUserByName("db User");
	}

	@Test
	public void testUserRemoval() throws ESEException
	{
		assertEquals(1, ESEDatabase.getAllUsers().size());
		ESEDatabase.createUser("SecondUser", "pass", "", "");
		ESEDatabase.createUser("ThirdTestUser", "word", "", "");
		assertEquals(3, ESEDatabase.getAllUsers().size());
		assertEquals(1, ESEDatabase.getUserByName("SecondUser").getUserID());
		assertEquals(2, ESEDatabase.getUserByName("ThirdTestUser").getUserID());

		ESEUser secondUser = ESEDatabase.getUserByName("SecondUser");
		ESEDatabase.removeUserByID(1);

		assertEquals(2, ESEDatabase.getAllUsers().size());
		assertEquals("ThirdTestUser", ESEDatabase.getAllUsers().get(1).getName());
		assertFalse(ESEDatabase.getAllUsers().contains(secondUser));

		ESEDatabase.removeUserByName("ThirdTestUser");

		assertEquals(1, ESEDatabase.getAllUsers().size());
		assertTrue(ESEDatabase.getAllUsers().contains(this.setupUser));
	}

	@Test
	public void shouldReturnOtherUsers() throws ESEException
	{
		ESEDatabase.createUser("SecondUser", "pass", "", "");
		ESEDatabase.createUser("ThirdTestUser", "word", "", "");
		ArrayList<ESEUser> otherUsersList = ESEDatabase.getOtherUsers(this.setupUser.getName());
		assertEquals(2, otherUsersList.size());
		assertEquals("SecondUser", otherUsersList.get(0).getName());
		assertEquals("ThirdTestUser", otherUsersList.get(1).getName());
	}

	@Test
	public void shouldFindUserByName() throws ESEException
	{
		ESEUser newUser, testUser, someone, registered;

		ESEDatabase.createUser("New User", "pw", "", "");
		newUser = ESEDatabase.getUserByName("New User");
		ESEDatabase.createUser("Test User", "pw", "", "");
		testUser = ESEDatabase.getUserByName("Test User");
		ESEDatabase.createUser("Calendar User", "pw", "", "");
		someone = ESEDatabase.getUserByName("Calendar User");
		ESEDatabase.createUser("Registered User", "pw", "", "");
		registered = ESEDatabase.getUserByName("Registered User");

		ArrayList<ESEUser> searchResults;
		ArrayList<String> strFind = new ArrayList<String>(Arrays.asList("DB User", "db[^A-Za-z0-9]user", ".*b [A-Z]s.*", ".b..[a-z]er", "db.*ser"));

		for(int i=0;i<strFind.size();i++)
		{
			searchResults = ESEDatabase.findUser(strFind.get(0));
			assertEquals(this.setupUser, searchResults.get(0));
			assertEquals(new ArrayList<ESEUser>(Arrays.asList(this.setupUser)), searchResults);
		}

		assertEquals(new ArrayList<ESEUser>(Arrays.asList(this.setupUser, newUser, testUser, someone, registered)), ESEDatabase.findUser(" Use"));
	}
}
