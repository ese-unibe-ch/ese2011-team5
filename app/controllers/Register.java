package controllers;

import play.data.validation.Required;
import play.mvc.Controller;
import models.*;

public class Register extends Controller
{

    public static void index()
    {
    	render();
    }

    public static void createNewUser(@Required String username, @Required String password, @Required String confirmpassword) throws Throwable
    {
        if(validation.hasErrors())
        {
            flash.error("You have to provide an username and a password!");
            params.flash();
            index();
        }
        if(!password.equals(confirmpassword))
        {
            flash.error("Passwords do not match!");
            params.flash();
            index();
        }

    	ESEDatabase.createUser(username, password, "", "");
    	Secure.authenticate(username, password, false);
    	Application.index();
    }
}
