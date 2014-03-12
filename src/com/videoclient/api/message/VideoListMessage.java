package com.videoclient.api.message;

/**
 * @author Drem Darios
 *
 */
public class VideoListMessage implements Message
{

    private String courseID;
    private String videoID;

    /**
     * Uses the courseID and videoID to create a message to the server to return
     * a video list. The video id is optional.
     * @param courseID
     * @param videoID
     */
    public VideoListMessage(String courseID)
    {
        this(courseID, null);
    }
    /**
     * Uses the courseID and videoID to create a message to the server to return
     * a video list. The video id is optional.
     * @param courseID
     * @param videoID
     */
    public VideoListMessage(String courseID, String videoID)
    {
        this.courseID = courseID;
        this.videoID = videoID;
    }
    
    @Override
    public String getMessage()
    {
        StringBuffer message = new StringBuffer("videoList;");
     
//      Format 1: videoList;course:courseID;;
        message.append("course:");
        message.append(courseID);
        
        if(videoID != null)
        {
//            Format 2: videoList;course:courseID;after:aVideoID;;
            message.append("after:");
            message.append(videoID);
        }
        
        message.append(";;");
        
        return message.toString();
    }

}
