package edu.gatech.seclass.sdpcryptogram;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class SingleCryptogramStatActivity extends AppCompatActivity {

    TextView cryptogramName;
    TextView timesSolved;
    TextView firstSolver;
    TextView secondSolver;
    TextView thirdSolver;
    TextView creationDate;

    long cryptogramCreationUnixDate;
    int cryptogramSolveCount;
    ArrayList<String> firstThreeSolvers;

    DB mydb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_cryptogram_stat);

        final String currentUser = getIntent().getStringExtra("CurrentUser");
        final String puzzleName = getIntent().getStringExtra("PuzzleName");

        mydb = new DB(this);

        cryptogramName = (TextView) findViewById(R.id.textView_SingleCryptogramStat_Name);
        timesSolved = (TextView) findViewById(R.id.textView_SingleCryptogramStat_NumSolvers);
        firstSolver = (TextView) findViewById(R.id.textView_SingleCryptogramStat_FirstSolver);
        secondSolver = (TextView) findViewById(R.id.textView_SingleCryptogramStat_SecondSolver);
        thirdSolver = (TextView) findViewById(R.id.textView_SingleCryptogramStat_ThirdSolver);
        creationDate = (TextView) findViewById(R.id.textView_SingleCryptogramStat_CreationDate);

        cryptogramName.setText(puzzleName);


        //Populate the cryptogram creation date
        String dateFormat;
        String pattern = "MM-dd-yyyy hh:mm aaa";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        cryptogramCreationUnixDate = mydb.getCryptogramCreationDate(puzzleName);
        dateFormat = simpleDateFormat.format(new Date(cryptogramCreationUnixDate));
        creationDate.setText(dateFormat);

        //Populate the number of times it was solved
        cryptogramSolveCount = mydb.getCryptogramNumTimesSolved(puzzleName);
        timesSolved.setText(String.valueOf(cryptogramSolveCount));

        //Populate the three solvers
        firstThreeSolvers = mydb.getCryptogramFirstThreeSolvers(puzzleName);
        firstSolver.setText(firstThreeSolvers.get(0));
        secondSolver.setText(firstThreeSolvers.get(1));
        thirdSolver.setText(firstThreeSolvers.get(2));

    }
}
