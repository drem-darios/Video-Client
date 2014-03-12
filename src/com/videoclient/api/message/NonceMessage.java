package com.videoclient.api.message;

/**
 * Message to request a Nonce from the server.
 * 
 * @author Drem Darios
 */
public class NonceMessage implements Message
{

    @Override
    public String getMessage()
    {
        return "nonce;;";
    }

}
