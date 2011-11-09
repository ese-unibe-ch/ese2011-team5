package modelTests;

import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.Arrays;
import models.*;
import org.junit.*;

public class ESEDatabaseTest
{

	ESEDatabase DBInstance;
	ESEUser setupUser;

	@Before
	public void setUp()
	{
		if(ESEDatabase.getAllUsers().size() == 0)
		{
			ESEDatabase.createUser("DB User", "pw", "firstName", "familyName");
			assertEquals(1, ESEDatabase.getAllUsers().size());
		}
		this.setupUser = ESEDatabase.getAllUsers().get(0);
	}

	@Test
	public void shouldReturnCorrectUser()
	{
		assertEquals(this.setupUser, ESEDatabase.getUserByID(0));
		assertEquals(this.setupUser, ESEDatabase.getUserByName("DB User"));
	}

	@Test(expected = IllegalArgumentException.class)
	public void usernameShouldbeCaseSensitive()
	{
		ESEDatabase.getUserByName("db User");
	}

	@Test
	public void testUserRemoval()
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
	public void shouldReturnOtherUsers()
	{
		ESEDatabase.createUser("SecondUser", "pass", "", "");
		ESEDatabase.createUser("ThirdTestUser", "word", "", "");
		ArrayList<ESEUser> otherUsersList = ESEDatabase.getOtherUsers(this.setupUser.getName());
		assertEquals(2, otherUsersList.size());
		assertEquals("SecondUser", otherUsersList.get(0).getName());
		assertEquals("ThirdTestUser", otherUsersList.get(1).getName());
	}

	@Test
	public void findUserByName()
	{
		ESEUser newUser, testUser, someone, registered;

		ESEDatabase.createUser("New User", "pw", "", "");
		newUser = ESEDatabase.getAllUsers().get(1);
		ESEDatabase.createUser("Test", "pw", "", "");
		testUser = ESEDatabase.getAllUsers().get(2);
		ESEDatabase.createUser("Someone", "pw", "", "");
		someone = ESEDatabase.getAllUsers().get(3);
		ESEDatabase.createUser("Registered", "pw", "", "");
		registered = ESEDatabase.getAllUsers().get(4);

		ArrayList<ESEUser> searchResults;
		ArrayList<String> strFind = new ArrayList<String>(Arrays.asList("DB User", "db[^A-Za-z0-9]user", ".*b [A-Z]s.*", ".b..[a-z]er", "db.*ser"));

		for(int i=0;i<strFind.size();i++)
		{
			searchResults = ESEDatabase.findUser(strFind.get(0));
			assertEquals(this.setupUser, searchResults.get(0));
		}

		assertEquals(new ArrayList<ESEUser>(), ESEDatabase.findUser("use"));
	}
}
