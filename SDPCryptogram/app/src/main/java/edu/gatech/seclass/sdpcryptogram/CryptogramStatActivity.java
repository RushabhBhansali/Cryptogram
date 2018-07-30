package edu.gatech.seclass.sdpcryptogram;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class CryptogramStatActivity extends AppCompatActivity {

    ArrayList<String> allPuzzles;
    ListView cryptNameTable;
    private ArrayList column1Data;
    DB mydb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cryptogram_stat);

        final String currentUser = getIntent().getStringExtra("CurrentUser");
        mydb = new DB(this);

        //get all cryptogram names and put them into a table
        allPuzzles = mydb.getAllCryptogramNames();
        column1Data = new ArrayList(); //puzzle names

        for (int i=0; i<allPuzzles.size(); i++) {
            column1Data.add(allPuzzles.get(i));
        }

        cryptNameTable = (ListView) findViewById(R.id.listview_listCompleted_CryptogramNameColumn);

        //populate the table with an adapter that grabs from the crypNameData
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String> (
                this,
                android.R.layout.simple_list_item_1,
                column1Data );

        cryptNameTable.setAdapter(arrayAdapter);

        //when the user clicks on something in the table, we should bring them to that puzzle
        cryptNameTable.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                String puzzleName = (String) column1Data.get(position);
                //System.out.println(puzzleName);
                Intent intent  = new Intent(CryptogramStatActivity.this, SingleCryptogramStatActivity.class);
                intent.putExtra("CurrentUser", currentUser);
                intent.putExtra( "PuzzleName", puzzleName);
                startActivity(intent);
            }
        });
    }


}
