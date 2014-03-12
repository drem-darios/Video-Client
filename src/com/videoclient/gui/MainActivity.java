package com.videoclient.gui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.videoclient.api.conn.Connection;


public class MainActivity extends Activity {

    private static Connection connection = new Connection("bismarck.sdsu.edu", 8008);
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        // Pass the login a connection.
        Intent login = new Intent(getApplicationContext(), LoginActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("connection",connection);
        login.putExtra("connection", connection);
        
        startActivity(login);

    }
}
