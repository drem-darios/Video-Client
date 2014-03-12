package com.videoclient.gui;

import java.util.Map;

import android.app.ListActivity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.videoclient.api.conn.ConnectionManager;
import com.videoclient.api.message.CourseListMessage;
import com.videoclient.api.message.Message;
import com.videoclient.api.message.MessageHelper;
import com.videoclient.api.message.VideoListMessage;

public class VideoListActivity extends ListActivity {
    
    private TextView selection;
    private ConnectionManager manager = LoginActivity.manager;    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /**
         * NOTE:
         * 
         * Next version make course a list view so user can choose from list
         * of courses.
         * 
         */
        String courseId = "";
        
        AsyncTask<String, Integer, Message> courseTask = 
                new CourseListTask().execute("");
        
        try
        {
            Message courseListMessage = (Message) courseTask.get();
            Map<String, String> courseListMap = 
                    MessageHelper.getMessageElements(courseListMessage);
            courseId = courseListMap.get("id");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        
        // Run a task to get the video list.
        AsyncTask<String, Integer, Message> videoListTask = 
                new VideoListTask().execute(courseId);
        
        String[] items = null;    

        try
        {
            Message videoListMessage = (Message) videoListTask.get();
            String videoListString  = videoListMessage.getMessage();
            if (videoListString.startsWith("error:"))
            {
                //DisplayError message;
                items = new String[0];
            }
            else
            {
                String[] videoListArray = MessageHelper.parseMessage(videoListMessage);
                
                int videoCount = getVideoCount(videoListArray[0]);
                items = getItems(videoCount, videoListArray);
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        setContentView(R.layout.activity_video_list);
        setListAdapter(new ArrayAdapter<String>(this, 
                android.R.layout.simple_list_item_1, items));
        
        selection = (TextView) findViewById(R.id.video_selection);
        
        getListView().setTextFilterEnabled(true);
    }
  
    /**
     * Gets the video count from a string.
     * 
     */
    private int getVideoCount(String videoCountString)
    {
        String[] countArray = videoCountString.split(":");
        return Integer.parseInt(countArray[countArray.length - 1]);
    }

    /**
     * Gets the items to be displayed on the screen. Pass in the number of videos
     * on the list and an array of tokens from the server that need to be parsed.
     * 
     */
    private String[] getItems(int videoCount, String[] tokens)
    {
        String[] items = new String[videoCount];
        int listStart = 1;
        int listEnd = (tokens.length - 1);
        for(int i = 0; i < videoCount; i++)
        {
            StringBuilder sb = new StringBuilder(tokens[listStart]);
            listStart++;
            for(int j = listStart; j < listEnd; j++)
            {
                if(tokens[j].startsWith("\r"))
                {
                    listStart = j; break;
                }
                else
                {
                    sb.append(tokens[j]);   
                }
            }
            
            items[i] = sb.toString();
        }
        
        return items;
    }

    @Override
    public void onListItemClick(ListView parent, View v, int position, long id) 
    {
        selection.setText((String)getListView().getItemAtPosition(position));
    }
    
    /**
     * Task to get a video list. It requires a course id. Later the course id will
     * be passed in from another activity.
     * @author Drem Darios
     *
     */
    private class VideoListTask extends AsyncTask<String, Integer, Message>
    {

        @Override
        protected Message doInBackground(String... params)
        {
            String courseId = params[0];
            Message message = new VideoListMessage(courseId);
//          Response: ok:N;name:CourseName1;id:CourseId1;date:date1;url:url1;
            manager.sendMessage(message);
            Message videoList = manager.getMessage();
            return videoList;
        }
        
    }
    
    /**
     * Task to get the course list. Later on abstract this out into 
     * another activity.
     * @author Drem Darios
     *
     */
    private class CourseListTask extends AsyncTask<String, Integer, Message>
    {
        @Override
        protected Message doInBackground(String... params)
        {
            // request course list
            Message message = new CourseListMessage();
            manager.sendMessage(message);
            Message reply = manager.getMessage();
            
            return reply;
        }
        
    }

}
