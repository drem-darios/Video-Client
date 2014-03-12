package com.videoclient.api.message.strategy;

import com.videoclient.api.message.Message;

/**
 * Default strategy for messages. The raw message will be returned from being
 * processed.
 * 
 * @author Drem Darios
 *
 */
public class DefaultReply implements MessageStrategy
{

    @Override
    public String processMessage(Message message)
    {
        return message.getMessage();
    }

}
