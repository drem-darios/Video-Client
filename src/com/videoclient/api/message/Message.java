package com.videoclient.api.message;

import java.util.logging.Logger;

/**
 * @author Drem Darios
 *
 */
public interface Message
{
    Logger LOG = Logger.getLogger("message");
    /**
     * Gets the message string attributed with this message
     */
    String getMessage();
}
