package com.videoclient.api.message;

/**
 * @author Drem Darios
 *
 */
public class LogoutMessage implements Message
{

    @Override
    public String getMessage()
    {
        return "logout;;";
    }

}
