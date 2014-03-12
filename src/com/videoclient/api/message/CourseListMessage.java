package com.videoclient.api.message;

/**
 * Message to request a course list from the server.
 * 
 * @author Drem Darios
 */
public class CourseListMessage implements Message
{

    @Override
    public String getMessage()
    {
        return "courseList;;";
    }

}
