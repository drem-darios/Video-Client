package com.videoclient.test;

import java.util.Map;

import junit.framework.Assert;
import android.test.AndroidTestCase;

import com.videoclient.api.conn.Connection;
import com.videoclient.api.conn.ConnectionManager;
import com.videoclient.api.message.CourseListMessage;
import com.videoclient.api.message.LoginMessage;
import com.videoclient.api.message.LogoutMessage;
import com.videoclient.api.message.Message;
import com.videoclient.api.message.MessageHelper;
import com.videoclient.api.message.NonceMessage;
import com.videoclient.api.message.SafeLoginMessage;
import com.videoclient.api.message.VideoListMessage;

/**
 * @author Drem Darios
 *
 */
public class ClientTest extends AndroidTestCase
{

    private static ConnectionManager manager;
    private static final String USER = "1102";
    private static final String PASS = "Korea";
    
    protected void setUp()
    {
        Connection connection = new Connection("bismarck.sdsu.edu", 8008);
        manager = new ConnectionManager(connection);
    }
    
    protected void tearDown()
    {
        manager.closeConnection();
    }
    
    public void testLogin()
    {
        Message message = new LoginMessage(USER, PASS);
        manager.sendMessage(message);
        Message messageString = manager.getMessage();

        String reply = messageString.getMessage();
        Assert.assertEquals("ok:success;;", reply);
    }
    
    public void testNonce()
    {
        Message message = new NonceMessage();
        manager.sendMessage(message);
        Message messageString = manager.getMessage();
        
        String reply = messageString.getMessage();
        System.out.println(reply);
    }
    
    public void testSafeLogin()
    {
        Message nonce = new NonceMessage();
        manager.sendMessage(nonce);
        Message replyMessage = manager.getMessage();
        
        String replyNonce = replyMessage.getMessage();
        System.out.println(replyNonce);

        Map<String, String> replyMap = 
                MessageHelper.getMessageElements(replyMessage);
        replyNonce = replyMap.get("ok");
        
        System.out.println(replyNonce);
        
        Message message = new SafeLoginMessage(USER, PASS, replyNonce);
        
        System.out.println(message.getMessage());
        manager.sendMessage(message);
        Message messageString = manager.getMessage();
        
        String reply = messageString.getMessage();
        System.out.println(reply);
        Assert.assertEquals("ok:success;;", reply);
    }
    
    public void testCourseList()
    {
        testLogin();
        Message message = new CourseListMessage();
        manager.sendMessage(message);
        Message messageString = manager.getMessage();
        String reply = messageString.getMessage();
        System.out.println(reply);
        Assert.assertTrue(reply.startsWith("ok:"));
    }
    
    public void testVideoListFormat1()
    {
     
//        Example of course list: 2;name:CS 580;id:1238;
//        Format 1: videoList;course:courseID;;
        
        testLogin();
        Message message = new VideoListMessage("1238");
//        Response: ok:N;name:CourseName1;id:CourseId1;date:date1;url:url1;
        manager.sendMessage(message);
        Message messageString = manager.getMessage();
        
        Map<String, String> replyMap = 
                MessageHelper.getMessageElements(messageString);
        
        System.out.println(replyMap.get("name"));
        System.out.println(replyMap.get("id"));
        System.out.println(replyMap.get("date"));
        System.out.println(replyMap.get("url"));
        
        
        replyMap.get("name");
        replyMap.get("id");
        replyMap.get("date");
        replyMap.get("url");
        
        String reply = messageString.getMessage();
        System.out.println(reply);
    }
    
    public void testVideoListFormat2()
    {
//      Format 2: videoList;course:courseID;after:aVideoID;;      
      testLogin();
      Message message = new VideoListMessage("1238", "1229");
//      Response: ok:N;name:CourseName1;id:CourseId1;date:date1;url:url1;
      manager.sendMessage(message);
      Message messageString = manager.getMessage();
      System.out.println(message.getMessage());
      
      Map<String, String> replyMap = 
              MessageHelper.getMessageElements(messageString);
      
      System.out.println(replyMap.get("name"));
      System.out.println(replyMap.get("id"));
      System.out.println(replyMap.get("date"));
      System.out.println(replyMap.get("url"));
      
      
      replyMap.get("name");
      replyMap.get("id");
      replyMap.get("date");
      replyMap.get("url");
      replyMap.get("questions");
      
      String reply = messageString.getMessage();
      System.out.println(reply);        
    }
    
    public void testVideoListNoVideos()
    {
//      Format 2: videoList;course:courseID;after:aVideoID;;      
      testLogin();
      Message message = new VideoListMessage("9876");
//      Response: ok:N;name:CourseName1;id:CourseId1;date:date1;url:url1;
      manager.sendMessage(message);
      Message messageString = manager.getMessage();
      String reply = messageString.getMessage();
      System.out.println(reply);        
    }
    
    public void testQuestionList()
    {
        
    }
    
    public void testQuestionAdd()
    {
        
    }
    
    public void testAnswerList()
    {
        
    }
    
    public void testAnswerAdd()
    {
        
    }
    
    public void testLogout()
    {
        Message message = new LogoutMessage();
        manager.sendMessage(message);
        Message messageString = manager.getMessage();
        
        String reply = messageString.getMessage();
        Assert.assertEquals("ok:success;;", reply);
    }

}
