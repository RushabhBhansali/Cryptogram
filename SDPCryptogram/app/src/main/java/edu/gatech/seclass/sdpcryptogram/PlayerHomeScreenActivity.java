package edu.gatech.seclass.sdpcryptogram;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.view.View;
import android.view.View.OnClickListener;
import android.content.Intent;

public class PlayerHomeScreenActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState){

        final String currentUser = getIntent().getStringExtra("CurrentUser");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_home_screen);

        Button createNewCryptogramButton = (Button) findViewById(R.id.button_playerHomeScreen_CreateNewCryptogram);
        Button listOfUnsolvedCryptogram = (Button) findViewById(R.id.button_playerHomeScreen_ListOfUnsolvedCryptograms);
        Button listOfCompletedCryptogram = (Button) findViewById(R.id.button_playerHomeScreen_ListOfCompletedCryptogram);
        Button cryptogramStat = (Button) findViewById(R.id.button_playerHomeScreen_CryptogramStatistics);
        Button switchUser = (Button) findViewById(R.id.button_playerHomeScreen_SwitchUser);

        createNewCryptogramButton.setOnClickListener(new OnClickListener(){
            public void onClick(View arg0){
                Intent intent  = new Intent(PlayerHomeScreenActivity.this, CreateNewCryptogramActivity.class);
                intent.putExtra("CurrentUser", currentUser);
                startActivity(intent);
            }
        });

        listOfUnsolvedCryptogram.setOnClickListener(new OnClickListener(){
            public void onClick(View arg0){
                Intent intent  = new Intent(PlayerHomeScreenActivity.this, ListOfUnSolvedActivity.class);
                intent.putExtra("CurrentUser", currentUser);
                startActivity(intent);
            }
        });

        listOfCompletedCryptogram.setOnClickListener(new OnClickListener(){
            public void onClick(View arg0){
                Intent intent  = new Intent(PlayerHomeScreenActivity.this, ListOfCompletedActivity.class);
                intent.putExtra("CurrentUser", currentUser);
                startActivity(intent);
            }
        });

        cryptogramStat.setOnClickListener(new OnClickListener(){
            public void onClick(View arg0){
                Intent intent  = new Intent(PlayerHomeScreenActivity.this, CryptogramStatActivity.class);
                intent.putExtra("CurrentUser", currentUser);
                startActivity(intent);
            }
        });

        switchUser.setOnClickListener(new OnClickListener(){
            public void onClick(View arg0){
                Intent intent  = new Intent(PlayerHomeScreenActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });



    }





}
