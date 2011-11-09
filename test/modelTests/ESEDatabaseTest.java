package modelTests;

import static org.junit.Assert.*;
import java.util.ArrayList;
import models.*;
import org.junit.*;

public class ESEDatabaseTest
{

	ESEDatabase DBInstance;
	ESEUser setupUser;

	@Test
	public void shouldReturnCorrectUser()
	{
		assertEquals(this.setupUser, this.DBInstance.getUserByID(0));
		assertEquals(this.setupUser, this.DBInstance.getUserByName("DB User"));
	}

	@Test(expected = IllegalArgumentException.class)
	public void usernameShouldbeCaseSensitive()
	{
		this.DBInstance.getUserByName("db User");
	}

	@Test
	public void findUserByName()
	{
		assertEquals(this.setupUser, this.DBInstance.findUser("DB User"));
		assertEquals(this.setupUser, this.DBInstance.findUser("db[^A-Za-z0-9]user"));
		assertEquals(this.setupUser, this.DBInstance.findUser(".*b [A-Z]s.*"));
		assertEquals(this.setupUser, this.DBInstance.findUser(".b..[a-z]er"));
		assertEquals(this.setupUser, this.DBInstance.findUser("db.*ser"));
		try
		{
			assertEquals(this.setupUser, this.DBInstance.findUser("use"));
			fail("Expected IllegalArgumentException");
		}
		catch(IllegalArgumentException e)
		{
			assertNotNull(e);
		}
	}

	@Test
	public void testUserRemoval()
	{
		assertEquals(1, this.DBInstance.getAllUsers().size());
		this.DBInstance.createUser("SecondUser", "pass", "", "");
		this.DBInstance.createUser("ThirdTestUser", "word", "", "");
		assertEquals(3, this.DBInstance.getAllUsers().size());
		assertEquals(1, this.DBInstance.getUserByName("SecondUser").getUserID());
		assertEquals(2, this.DBInstance.getUserByName("ThirdTestUser").getUserID());

		ESEUser secondUser = this.DBInstance.getUserByName("SecondUser");
		this.DBInstance.removeUserByID(1);

		assertEquals(2, this.DBInstance.getAllUsers().size());
		assertEquals("ThirdTestUser", this.DBInstance.getAllUsers().get(1).getName());
		assertFalse(this.DBInstance.getAllUsers().contains(secondUser));

		this.DBInstance.removeUserByName("ThirdTestUser");

		assertEquals(1, this.DBInstance.getAllUsers().size());
		assertTrue(this.DBInstance.getAllUsers().contains(this.setupUser));
	}

	@Test
	public void shouldReturnOtherUsers()
	{
		this.DBInstance.createUser("SecondUser", "pass", "", "");
		this.DBInstance.createUser("ThirdTestUser", "word", "", "");
		ArrayList<ESEUser> otherUsersList = this.DBInstance.getOtherUsers(this.setupUser.getName());
		assertEquals(2, otherUsersList.size());
		assertEquals("SecondUser", otherUsersList.get(0).getName());
		assertEquals("ThirdTestUser", otherUsersList.get(1).getName());
	}
}
