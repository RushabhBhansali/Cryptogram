package edu.gatech.seclass.sdpcryptogram;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;

public class ListOfUnSolvedActivity extends AppCompatActivity {

    HashMap<String, Integer[]> unsolvedPuzzles;
    DB mydb;
    private ArrayList column1Data;
    private ArrayList column2Data;
    private ArrayList column3Data;
    ListView listViewCol1;
    ListView listViewCol2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_unsolved);

        final String currentUser = getIntent().getStringExtra("CurrentUser");

        mydb = new DB(this);

        unsolvedPuzzles = mydb.ListUnsolved(currentUser);

        //fill the data of what will be in the columns as array lists built from the database return object.
        column1Data = new ArrayList(); //puzzle names
        column2Data = new ArrayList(); //attempts taken
        column3Data = new ArrayList(); //attempts remaining

        Iterator it = unsolvedPuzzles.entrySet().iterator();
        while (it.hasNext()) {
            HashMap.Entry pair = (HashMap.Entry) it.next();
            String cur = (String) pair.getKey();
            Integer[] values = (Integer[]) pair.getValue();
            column1Data.add(new String( (String)pair.getKey() ));
            column2Data.add(new String(values[1].toString()));
            column3Data.add(new String(values[0].toString()));
            it.remove(); // avoids a ConcurrentModificationException
        }

        //now we create the columns and populate them with the data one by one
        listViewCol1 = (ListView) findViewById(R.id.listview_unsolvedCryptogram_cryptogramNameColumn);
        listViewCol2 = (ListView) findViewById(R.id.listview_unsolvedCryptogram_falseAttemptsColumn);

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

        //when the user clicks on something in the table, we should bring them to that puzzle
        listViewCol1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                String puzzleName = (String) column1Data.get(position);
                String attemptsTakenText = (String) column2Data.get(position);
                String attemptsRemainingText = (String) column3Data.get(position);

                Intent intent  = new Intent(ListOfUnSolvedActivity.this, PlayCryptogramActivity.class);
                intent.putExtra("CurrentUser", currentUser);
                intent.putExtra( "PuzzleName", puzzleName);
                intent.putExtra("AttemptsTaken", attemptsTakenText);
                intent.putExtra( "AttemptsRemaining", attemptsRemainingText);
                startActivity(intent);
                finish();
            }
        });
    }
}
