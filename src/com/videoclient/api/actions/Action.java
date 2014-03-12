package com.videoclient.api.actions;

import java.util.logging.Logger;

import com.videoclient.api.message.Message;

/**
 * This defines Actions that can be performed between the client and server.
 * Actions will have a name. They will be able to create a message. They will
 * be able to handle a message.
 * @author Drem Darios
 *
 */
public interface Action
{
    
    Logger LOG = Logger.getLogger("actionlog");
    /**
     * Creates a message attributed to this action
     * @return The message attributed to the action
     */
    Message createMessage();
    /**
     * Gets the name of the action.
     * @return The action name
     */
    String getActionName();
    /**
     * Processes the reply for this action
     * @param message - The message holding the reply
     * @return Reply info
     */
    String processReply(Message message);
}
