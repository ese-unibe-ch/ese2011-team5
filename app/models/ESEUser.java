package models;

import java.util.ArrayList;

public class ESEUser
{

	private static int idCounter = 0;

	private int userID;
	private ESEProfile profile;

	private String password;
	private String secureQuestion = "Holy solution";
	private String secureAnswer = "42";

	private ArrayList<ESECalendar> calendarList;
	private ArrayList<ESEGroup> groupList;

	public ESEUser(String username, String password, String firstName, String familyName) throws ESEException
	{
		this(username, password, firstName, familyName, "Holy solution", "42");
	}

	public ESEUser(String username, String password,
			String firstName, String familyName,
			String secureQuestion, String secureAnswer) throws ESEException
	{
		if(username.isEmpty())
		{
			throw new ESEException("User name must not be empty!");
		}

		for(ESEUser user : ESEDatabase.getAllUsers())
		{
			if(username.equals(user.getName()))
			{
				throw new ESEException("This user name is already in the database!");
			}
		}

		this.userID = idCounter++;
		this.calendarList = new ArrayList<ESECalendar>();
		this.groupList = new ArrayList<ESEGroup>();
		ESEGroup friends = new ESEGroup("Friends", this);
		this.groupList.add(friends);
		this.profile = new ESEProfile(this, username, firstName, familyName);

		this.secureQuestion = secureQuestion;
		this.secureAnswer = secureAnswer;
		this.password = password;
	}

	/*
	 * Methods with read only access
	 */

	public int getUserID(){
		return this.userID;
	}

	public String getName()
	{
		return this.profile.getUsername();
	}

	public String getPassword()
	{
		return this.password;
	}

	public String getFirstName()
	{
		return this.profile.getFirstName();
	}

	public String getFamilyName()
	{
		return this.profile.getFamilyName();
	}

	public ESEProfile getProfile()
	{
		return this.profile;
	}

	public String getQuestion()
	{
		return this.secureQuestion;
	}

	public String getAnswer()
	{
		return this.secureAnswer;
	}

	public ArrayList<ESEGroup> getGroupList()
	{
		return new ArrayList<ESEGroup>(this.groupList);
	}

	public ArrayList<ESECalendar> getCalendarList()
	{
		return new ArrayList<ESECalendar>(this.calendarList);
	}

	public void addCalendar(String calendarName) throws ESEException
	{
		this.calendarList.add(new ESECalendar(calendarName, this));
	}

	public void addGroup(String groupName) throws ESEException
	{
		ESEGroup group = new ESEGroup(groupName, this);
		this.groupList.add(group);
	}

	public ESECalendar getCalendarByID(int id) throws ESEException
	{
		for (ESECalendar calendar : calendarList)
		{
			if (calendar.getID() == id)
			{
				return calendar;
			}
		}
		throw new ESEException("No calendar with ID \"" + id + " \"!");
	}
	
	public ESECalendar getCalendarByName(String name) throws ESEException
	{
		for (ESECalendar calendar : calendarList)
		{
			if (calendar.getCalendarName().equals(name))
			{
				return calendar;
			}
		}
		throw new ESEException("No calendar with name \"" + name + "\"!");
	} 
	
	public ESEGroup getGroupByID(int id) throws ESEException
	{
		for (ESEGroup group : groupList)
		{
			if (group.getGroupID() == id)
			{
				return group;
			}
		}
		throw new ESEException("No group with ID \"" + id + "\"!");
	}

	public ESEGroup getGroupByName(String name) throws ESEException
	{
		for (ESEGroup group : groupList)
		{
			if (group.getGroupName().equals(name))
				return group;
		}
		throw new ESEException("No group with name \"" + name + "\"!");
	}

	public void removeCalendar(int calendarID) throws ESEException
	{
		ESECalendar calendarToRemove = this.getCalendarByID(calendarID);
		this.calendarList.remove(calendarToRemove);
	}

	public ArrayList<ESEEvent> getAllEvents(int calendarID) throws ESEException
	{
		for (ESECalendar calendar : this.calendarList)
		{
			if (calendar.getID() == calendarID)
			{
				return calendar.getAllEvents();
			}
		}
		throw new ESEException("No calendar with this ID!");
	}

	public ArrayList<ESEEvent> getAllPublicEvents(int calendarID) throws ESEException
	{
		for (ESECalendar calendar : this.calendarList)
		{
			if (calendar.getID() == calendarID)
			{
				return calendar.getAllPublicEvents();
			}
		}
		throw new ESEException("No calendar with this ID!");
	}

	/*
	 * Methods with read-write access
	 */

	public void addCalendar(ESECalendar calendarToAdd) throws ESEException
	{
		if(!this.equals(calendarToAdd.getOwner()))
		{
			throw new ESEException("Taking a foreign calendar is not allowed!");
		}
		this.calendarList.add(calendarToAdd);
	}

	public void addGroup(ESEGroup groupToAdd) throws ESEException
	{
		if(!this.equals(groupToAdd.getOwner()))
		{
			throw new ESEException("Taking a foreign group is not allowed!");
		}
		this.groupList.add(groupToAdd);
	}

	public void setPassword(String newPassword)
	{
		this.password = newPassword;
	}
	
	public void setQuestion(String newQuestion)
	{
		this.secureQuestion = newQuestion;
	}
	
	public void setAnswer(String newAnswer)
	{
		this.secureAnswer = newAnswer;
	}

	public static void resetIdCounter()
	{
		idCounter = 0;
	}

}
