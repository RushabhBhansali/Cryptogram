package edu.gatech.seclass.sdpcryptogram;

import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.view.View;
import android.view.View.OnClickListener;
import android.content.Intent;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        System.out.println("Calling out to create database");
        DB myDB = new DB(this);
        configureLoginButton();
        configureNewUserButton();
    }

    //send the user to the login page
    private void configureLoginButton() {
        Button LoginButton = (Button) findViewById(R.id.GoLoginActivity);

        // Capture button clicks
		LoginButton.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {

				// Transition activities
				Intent myIntent = new Intent(MainActivity.this,
						LoginActivity.class);
				startActivity(myIntent);
			}
		});
    }


    //send the user to the new account creation page
    private void configureNewUserButton() {
        Button NewUserButton = (Button) findViewById(R.id.GoNewUserActivity);

        // Capture button clicks
		NewUserButton.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {

				// Transition activities
				Intent myIntent = new Intent(MainActivity.this,
						NewUserActivity.class);
				startActivity(myIntent);
			}
		});
    }



}
