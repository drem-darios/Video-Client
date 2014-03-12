package com.videoclient.api.message;

import java.util.HashMap;
import java.util.Map;


/**
 * @author Drem Darios
 *
 */
public class MessageHelper
{
    /**
     * Returns Map from the array sent in. 
     * 
     * @param message Array of message elements parsed from a message
     * @return Map of key-values found in the message
     */
    public static Map<String, String> getMessageElements(Message message)
    {
        Map<String, String> result = new HashMap<String, String>();
        String[] messageArray = parseMessage(message);
        for(String pair : messageArray)
        {
            String[]elements = pair.split(":");
            if(elements.length == 2)
            {
                result.put(elements[0].trim(), elements[1].trim());
            }
            else if (elements.length > 2)
            {
                StringBuffer concatElements = new StringBuffer();
                
                for(int j = 1; j < elements.length; j++)
                {
                    concatElements.append(elements[j].trim());
                    if(j+1 < elements.length) // still more elements
                    {
                        concatElements.append(":");
                    }
                }
                
                result.put(elements[0].trim(), concatElements.toString());
            }
        }
        
        return result;
    }
    
    /**
     * Parses a raw message from the server into an array of message elements.
     * Message elements are in key-value pairs
     * 
     * @param input Raw message from the server
     * @return Array of the elements from the message
     */
    public static String[] parseMessage(Message message)
    {
        return message.getMessage().split(";");
    }
}
