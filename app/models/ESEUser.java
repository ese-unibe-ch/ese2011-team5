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

	public ESEUser(String username, String password, String firstName, String familyName)
	{
		this(username, password, firstName, familyName, "Holy solution", "42");
	}

	public ESEUser(String username, String password, String firstName, String familyName, String secureQuestion, String secureAnswer)
	{
		assert(username != "");

		for(ESEUser user : ESEDatabase.getAllUsers())
		{
			if(username.equals(user.getName()))
			{
				throw new IllegalArgumentException("This username is already in the database");
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

	public void addCalendar(String calendarName)
	{
		this.calendarList.add(new ESECalendar(calendarName, this));
	}

	public void addGroup(String groupName)
	{
		ESEGroup group = new ESEGroup(groupName, this);
		this.groupList.add(group);
	}

	public ESECalendar getCalendarByID(int id) throws IllegalArgumentException
	{
		for (ESECalendar calendar : calendarList)
		{
			if (calendar.getID() == id)
			{
				return calendar;
			}
		}
		throw new IllegalArgumentException("No calendar with this ID " + id);
	}
	
	public ESECalendar getCalendarByName(String name){
		for (ESECalendar calendar : calendarList){
			if (calendar.getCalendarName().equals(name))
				System.out.println("FOUND CALENDAR!");
				return calendar;
		}
		System.out.println("FOUND NO CALENDAR!");
		throw new IllegalArgumentException("No calendar with this name "+ name);
	} 
	
	public ESEGroup getGroupByID(int id){
		for (ESEGroup group : groupList){
			if (group.getGroupID() == id)
				return group;
		}
		throw new IllegalArgumentException("No group with this ID " + id);
	}

	public ESEGroup getGroupByName(String name)
	{
		for (ESEGroup group : groupList)
		{
			if (group.getGroupName().equals(name))
				return group;
		}
		throw new IllegalArgumentException("No group with this name " + name);
	}

	public void removeCalendar(int calendarID) throws IllegalArgumentException
	{
		ESECalendar calendarToRemove = this.getCalendarByID(calendarID);
		this.calendarList.remove(calendarToRemove);
	}

	public ArrayList<ESEEvent> getAllEvents(int calendarID) throws IllegalArgumentException
	{
		for (ESECalendar calendar : this.calendarList)
		{
			if (calendar.getID() == calendarID)
			{
				return calendar.getAllEvents();
			}
		}
		throw new IllegalArgumentException("No calendar with this ID");
	}

	public ArrayList<ESEEvent> getAllPublicEvents(int calendarID) throws IllegalArgumentException
	{
		for (ESECalendar calendar : this.calendarList)
		{
			if (calendar.getID() == calendarID)
			{
				return calendar.getAllPublicEvents();
			}
		}
		throw new IllegalArgumentException("No calendar with this ID");
	}

	public ArrayList<ESEEvent> getAllowedEvents(boolean currentUserIsOwner, int calendarID) throws IllegalArgumentException
	{
  		for (ESECalendar calendar : this.calendarList)
		{
			if (calendar.getID() == calendarID)
			{
				  return calendar.getAllAllowedEvents();
			}
		}
		throw new IllegalArgumentException("No calendar with this ID");
	}

	/*
	 * Methods with read-write access
	 */

	public void addCalendar(ESECalendar calendarToAdd)
	{
		assert this.equals(calendarToAdd.getOwner());
		this.calendarList.add(calendarToAdd);
	}

	public void addGroup(ESEGroup groupToAdd)
	{
		assert this.equals(groupToAdd.getOwner());
		this.groupList.add(groupToAdd);
	}

	public void setPassword(String newPassword)
	{
		this.password = newPassword;
	}
	
	public void setQuestion(String newQuestion)
	{
		this.secureQuestion=newQuestion;
	}
	
	public void setAnswer(String newAnswer)
	{
		this.secureAnswer=newAnswer;
	}

}
