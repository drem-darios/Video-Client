package com.videoclient.api.message;

/**
 * @author Drem Darios
 *
 */
public class LoginMessage implements Message
{

    /**
     * The username used to login
     */
    private String username;
    /**
     * The password used to login
     */
    private String password;
    
    /**
     * Creates a simple login message with no encryption.
     * 
     */
    public LoginMessage(String username, String password)
    {
        this.username = username;
        this.password = password;
    }
    
    @Override
    public String getMessage()
    {
        StringBuffer message = new StringBuffer("login;");
        message.append("id:");
        message.append(username + ";");
        message.append("password:");
        message.append(password + ";;");
        //login;id:userID;password:userPassword;;
        return message.toString();
    }

}
