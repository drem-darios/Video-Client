package com.videoclient.gui;

import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.videoclient.api.conn.Connection;
import com.videoclient.api.conn.ConnectionManager;
import com.videoclient.api.message.LoginMessage;
import com.videoclient.api.message.Message;
import com.videoclient.api.message.MessageHelper;
import com.videoclient.api.message.NonceMessage;
import com.videoclient.api.message.SafeLoginMessage;

public class LoginActivity extends Activity {

    /**
     * Created a static manager so it can be passed grabbed by other activities.
     */
    public static ConnectionManager manager;
    /**
     * String representation of an error message
     */
    private static final String ERROR = "error:";
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        
        Button login = (Button) findViewById(R.id.login_button);
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        
        login.setOnClickListener(new OnClickListener()
        {
            
            @Override
            public void onClick(View v) 
            {
                boolean sessionActive = createSession();
                String loginReply = attemptLogin(sessionActive);
                
                if(!sessionActive | isError(loginReply))
                {
                    String error = getErrorMessage(loginReply);
                    builder.setMessage(error).setTitle("Error").setPositiveButton("Ok", 
                            new DialogInterface.OnClickListener() 
                    {
                        public void onClick(DialogInterface dialog, int whichButton) 
                        {
                            dialog.dismiss();
                        }
                    });
                    // disconnect from the server
                    manager.closeConnection();
                    
                    Dialog dialog = builder.create();
                    dialog.show(); //show error dialog
                }
                
                else
                {
                    Intent login = new Intent(getApplicationContext(), 
                            VideoListActivity.class);
                    startActivity(login);
                }
            }
            
            /**
             * Gets the error message in the login reply.
             * 
             * @param loginReply
             * @return
             */
            private String getErrorMessage(String loginReply)
            {

                String[] errorSplit = loginReply.split(ERROR);
                String error = errorSplit[errorSplit.length - 1];
                
                return error.substring(0, error.indexOf(";;"));
                
            }

        });
    }
    
    /**
     * Checks if the reply was an error.
     * 
     */
    private boolean isError(String reply)
    {
        return reply.startsWith(ERROR);
    }

    
    /**
     * Creates a new connection if one is not already created. If it is not
     * connected, a new Client will be established.
     */
    private boolean createSession()
    {         
        /**
         * Get connection from the extras. It is passed in from the caller in 
         * a bundle.
         */
        Bundle bundle = getIntent().getExtras();
        Connection connection = (Connection)bundle.get("connection");
        // Check if it is already connected
        boolean sessionActive = connection.isConnected();
        if (!sessionActive)
        {
            AsyncTask<Connection, Integer, Boolean> conn = 
                    new ConnectionTask().execute(connection);
            try
            {
                sessionActive = (Boolean) conn.get();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        
        return sessionActive;
    }
    
    /**
     * Attempt to login to the server with the credentials typed in only if the
     *  session is active. If it is not active, a blank reply is returned.
     */
    private String attemptLogin(boolean sessionActive)
    {
        String reply = "";
        if(sessionActive)
        {
            EditText usernameET = (EditText) findViewById(R.id.usernameET);
            EditText passwordET = (EditText) findViewById(R.id.passwordET);
            String username = usernameET.getText().toString();
            String password = passwordET.getText().toString();
            
            Pair<String, String> credentials = 
                    new Pair<String, String>(username, password);
            
            AsyncTask<Pair<String, String>, Integer, String> login = 
                    new LoginTask().execute(credentials);
            try
            {
                reply = (String) login.get();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        
        return reply;
    }
    
    /**
     * Task to create the connection to the server and not block the main 
     * thread. Takes in a connection and replies with a Boolean.
     * 
     * @author Drem Darios
     *
     */
    private class ConnectionTask extends AsyncTask<Connection, Integer, Boolean>
    {

        @Override
        protected Boolean doInBackground(Connection... params)
        {
            Connection conn = params[0];
            manager = new ConnectionManager(conn);
            
            return conn.isConnected();
        }
        
    }
    
    /**
     * Task to attempt to login to the server.
     * 
     * @author Drem Darios
     *
     */
    private class LoginTask extends AsyncTask<Pair<String, String>, Integer, String>
    {
        @Override
        protected String doInBackground(Pair<String, String>...credentials)
        {
            System.out.println("Attempting secure login...");
            return attemptSecureLogin(credentials[0]);
        }

        /**
         * Attempts a secure login. If it does not succeed, a regular login is 
         * attempted.
         * 
         */
        private String attemptSecureLogin(Pair<String, String> credentials)
        {
            Message message = new NonceMessage();
            manager.sendMessage(message);
            Message replyMessage = manager.getMessage();
            
            String reply = replyMessage.getMessage();
            
            // if there was an error producing the nonce then try regular login
            if(isError(reply))
            {
                System.out.println("Attempting regular login...");
                return attemptLogin(credentials);
            }
            
            else
            {
                Map<String, String> replyMap = 
                        MessageHelper.getMessageElements(replyMessage);
                String replyNonce = replyMap.get("ok"); // the key to the nonce
                
                Message safeMessage = new SafeLoginMessage(credentials.first, 
                        credentials.second, replyNonce);
                manager.sendMessage(safeMessage);
                
                replyMessage = manager.getMessage();
                reply = replyMessage.getMessage();
                
                return reply;
            }
        }    
        
        /**
         * Attempts a regular login. Password is not encrypted. 
         * 
         */
        private String attemptLogin(Pair<String, String> credentials)
        {
            manager.sendMessage(new LoginMessage(credentials.first, 
                    credentials.second));
            
            Message message = manager.getMessage();
            String reply = message.getMessage();
            
            return reply;
        }
    }
}
