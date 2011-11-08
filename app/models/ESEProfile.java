package models;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class ESEProfile {

	private static int idCounter = 0;

	private int profileID;
	private ESEUser owner;
	private Date registrationDate;
	private String stateMessage="Hi there!";
	private ESEState state=ESEState.OFFLINE;
	
	
	private String username;
	private String firstName="-";
	private String familyName="-";
	
	private Date birthday;
	

	private String eMail="-";
		
	private String street="-";
	private String city="-";
	private String postCode="-";
	

	public ESEProfile(ESEUser owner, String username) {
		assert username != "";
		assert(owner != null);
		
		this.profileID = idCounter++;
		this.owner = owner;
		this.username=username;
		
		this.registrationDate=new Date();
	}
	
	public ESEProfile(ESEUser owner, String username, String firstName, String familyName)
	{
		assert username != "";
		assert(owner != null);
		
		this.profileID = idCounter++;
		this.owner = owner;
		this.username=username;
		this.firstName=firstName;
		this.familyName=familyName;
		
		this.registrationDate=new Date();
	}

	public int getID() {
		return this.profileID;
	}
	
	public ESEUser getOwner(){
		return this.owner;
	}
	
	public String getUsername() {
		return username;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getFamilyName() {
		return familyName;
	}

	public Date getBirthday() {
		return birthday;
	}
	
	public String getBirthdayString()
	{
		return ESEConversionHelper.convertDateToString(this.birthday);
	}
	
	public Date getRegistrationDate()
	{
		return this.registrationDate;
	}
	
	public String getRegistrationDateString()
	{
		return ESEConversionHelper.convertDateToString(this.registrationDate);
	}

	public String getMail() {
		return eMail;
	}

	public String getStreet() {
		return street;
	}

	public String getCity() {
		return city;
	}

	public String getPostCode() {
		return postCode;
	}
	
	public String getState()
	{
		return this.state.toString();
	}
	
	public String getStateMessage()
	{
		return this.stateMessage;
	}
	
	public void setStateMessage(String stateMessage)
	{
		this.stateMessage=stateMessage;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public void setFamilyName(String familyName) {
		this.familyName = familyName;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public void setMail(String eMail) {
		this.eMail = eMail;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public void setPostCode(String postCode) {
		this.postCode = postCode;
	}
	
	public void changeState(ESEState state)
	{
		this.state=state;
	}
	

	
	
	
	
}
