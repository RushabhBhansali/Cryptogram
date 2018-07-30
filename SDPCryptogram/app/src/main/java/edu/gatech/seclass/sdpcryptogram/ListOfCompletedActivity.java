package edu.gatech.seclass.sdpcryptogram;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

public class ListOfCompletedActivity extends AppCompatActivity{

    ArrayList<DB.CompletedCryptogram> solvedPuzzles;
    ArrayList<String> expiredPuzzles;
    DB mydb;
    ListView listViewCol1;
    ListView listViewCol2;
    ListView listViewCol3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_completed);

        final String currentUser = getIntent().getStringExtra("CurrentUser");

        mydb = new DB(this);
        ArrayList column1Data = new ArrayList(); //puzzle names
        ArrayList column2Data = new ArrayList(); //status
        ArrayList column3Data = new ArrayList(); //dates completed

        //first fill the data with solved cryptograms
        solvedPuzzles = mydb.ListSolved(currentUser);

        long curDate;
        String dateFormat;
        String pattern = "MM-dd-yyyy";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

        for (int i=0; i<solvedPuzzles.size(); i++) {
            column1Data.add(solvedPuzzles.get(i).cryptoName);
            column2Data.add("Solved");
            curDate = solvedPuzzles.get(i).dateCompleted;
            dateFormat = simpleDateFormat.format(new Date(curDate));
            column3Data.add(dateFormat);
        }

        //and also get all the ones that didn't get solved, but have no attempts remaining
        expiredPuzzles = mydb.ListExpiredPuzzles(currentUser);
        for (int i=0; i<expiredPuzzles.size(); i++) {
            column1Data.add(expiredPuzzles.get(i));
            column2Data.add("Failed");
            column3Data.add("n/a");
        }

        //now we create the columns and populate them with the data one by one
        listViewCol1 = (ListView) findViewById(R.id.listview_listCompleted_CryptogramNameColumn);
        listViewCol2 = (ListView) findViewById(R.id.listview_listCompleted_StatusColumn);
        listViewCol3 = (ListView) findViewById(R.id.listview_listCompleted_DateColumn);


        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String> (
                this,
                android.R.layout.simple_list_item_1,
                column1Data );

        listViewCol1.setAdapter(arrayAdapter);

        ArrayAdapter<String> arrayAdapter2 = new ArrayAdapter<String> (
                this,
                android.R.layout.simple_list_item_1,
                column2Data );

        listViewCol2.setAdapter(arrayAdapter2);

        ArrayAdapter<String> arrayAdapter3 = new ArrayAdapter<String> (
                this,
                android.R.layout.simple_list_item_1,
                column3Data );

        listViewCol3.setAdapter(arrayAdapter3);
    }
}
