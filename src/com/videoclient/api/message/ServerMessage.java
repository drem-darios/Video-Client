package com.videoclient.api.message;



/**
 * @author Drem Darios
 *
 */
public class ServerMessage implements Message
{

    private String message;
    /**
     * Message indicating an error occurred with the request
     */
    private final static String ERROR = "error:";
    
    /**
     * Creates a message with the string sent from the server
     * @param message
     */
    public ServerMessage(String message)
    {
        this.message = message;
    }
    
    @Override
    public String getMessage()
    {
        if (message.startsWith(ERROR))
        {
            String[] split = message.split(ERROR);
            String exception = split[split.length - 1];
            
            LOG.info("Error found... " + exception);
        }
        
        return message;
    }

}
