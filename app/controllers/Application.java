package controllers;

import play.*;
import play.mvc.*;

public class Application extends Controller
{

    public static void index()
    {
    	String username = controllers.Secure.Security.connected();

        render(username);
    }

}