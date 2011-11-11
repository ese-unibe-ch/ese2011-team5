package modelTests;

import static org.junit.Assert.*;
import models.*;
import org.junit.*;

public class ESEGroupTests
{

	ESEUser dummyOwner, otherDummyOwner, friend1, friend2, friend3;
	ESEGroup group1, group2;

	@Before
	public void setUp()
	{
		if(ESEDatabase.getAllUsers().size() == 0)
		{
			ESEDatabase.createUser("dummy", "pw", "firstName", "familyName");
			ESEDatabase.createUser("otherdummy", "pw", "firstName", "familyName");
			ESEDatabase.createUser("Friend1", "", "", "");
			ESEDatabase.createUser("Friend2", "", "", "");
			ESEDatabase.createUser("Friend3", "", "", "");
			this.dummyOwner = ESEDatabase.getUserByName("dummy");
			this.otherDummyOwner = ESEDatabase.getUserByName("otherdummy");
			this.dummyOwner.addGroup("Testgroup1");
			this.otherDummyOwner.addGroup("Testgroup2");
			}
		this.dummyOwner = ESEDatabase.getUserByName("dummy");
		this.otherDummyOwner = ESEDatabase.getUserByName("otherdummy");
		this.friend1 = ESEDatabase.getUserByName("Friend1");
		this.friend2 = ESEDatabase.getUserByName("Friend2");
		this.friend3 = ESEDatabase.getUserByName("Friend3");
		this.group1 = this.dummyOwner.getGroupByName("Testgroup1");
		this.group2 = this.otherDummyOwner.getGroupByName("Testgroup2");
	}

	@Test
	public void shouldInitialize()
	{
		assertTrue(this.group1 != null);
		assertTrue(this.group2 != null);
		assertEquals(5, this.group1.getGroupID());
		assertEquals(6, this.group2.getGroupID());
		assertEquals("Testgroup1", this.group1.getGroupname());
		assertEquals("Testgroup2", this.group2.getGroupname());
		assertEquals(this.dummyOwner, this.group1.getOwner());
		assertEquals(this.otherDummyOwner, this.group2.getOwner());
	}

	@Test
	public void shouldAddUserToGroup()
	{
		assertEquals(0, this.group1.getUserList().size());
		assertEquals(0, this.group2.getUserList().size());
		this.group1.addUserToGroup(this.friend1);
		this.group1.addUserToGroup(this.friend3);
		this.group2.addUserToGroup(this.friend3);
		assertEquals(2, this.group1.getUserList().size());
		assertEquals(1, this.group2.getUserList().size());
	}


	@Test
	public void shouldReturnFriendsList()
	{
		assertEquals(2, this.group1.getUserList().size());
		assertEquals(1, this.group2.getUserList().size());
		assertEquals(this.friend1, this.group1.getUserList().get(0));
		assertEquals(this.friend3, this.group1.getUserList().get(1));		
		assertEquals(this.friend3, this.group2.getUserList().get(0));		
	}

	@Test
	public void shouldRemoveUserFromGroup()
	{
		assertEquals(2, this.group1.getUserList().size());
		assertEquals(1, this.group2.getUserList().size());
		
		this.group1.removeUserFromGroup(friend3);
		assertEquals(1, this.group1.getUserList().size());
		assertEquals(1, this.group2.getUserList().size());
		assertEquals(this.friend1, this.group1.getUserList().get(0));
		assertEquals(this.friend3, this.group2.getUserList().get(0));
	}
}
