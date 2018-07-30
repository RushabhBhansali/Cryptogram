package edu.gatech.seclass.sdpcryptogram;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

public class PlayCryptogramActivity extends AppCompatActivity {

    //error strings
    String INCORRECT_SOLUTION_ATTEMPT = "Incorrect!";
    String PUZZLE_EXPIRED = "YOU BIG FAILURE! NO ATTEMPTS REMAINING!";
    String PUZZLE_SOLVED = "SOLVED! YOU ARE JUST WONDERFUL!";

    TextView puzzleNameView;
    TextView attemptsTakenView;
    TextView attemptsRemainingView;
    TextView currentSolutionView;
    TextView encodedPhraseView;

    String currentSolution;
    String encodedPhrase;

    //the spinners will map to the alphabetical substitutions, which will be initialized as a like-for-like mapping
    Map<Character, Character> characterHashMap;
    int sourceLetterSelectionIndex = 0;
    int destinationLetterSelectionIndex = 0;
    Character sourceLetter = 'A';
    Character destinationLetter = 'A';
    char[] letterArray = {'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z'};

    //and of course, the database is needed
    DB mydb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_cryptogram);

        final String currentUser = getIntent().getStringExtra("CurrentUser");
        final String puzzleName = getIntent().getStringExtra("PuzzleName");
        final String attemptsRemainingText = getIntent().getStringExtra("AttemptsRemaining");
        String attemptsTakenText = getIntent().getStringExtra("AttemptsTaken");

        puzzleNameView = (TextView) findViewById(R.id.textView_PlayCryptogram_puzzleName);
        attemptsTakenView = (TextView) findViewById(R.id.textView_PlayCryptogram_AttemptsTaken);
        attemptsRemainingView = (TextView) findViewById(R.id.textView_PlayCryptogram_AttemptsRemaining);
        currentSolutionView = (TextView) findViewById(R.id.textView_PlayCryptogram_decodedPhrase);
        encodedPhraseView = (TextView) findViewById(R.id.textView_PlayCryptogram_EncodedPhrase);

        puzzleNameView.setText(puzzleName);
        attemptsTakenView.setText(attemptsTakenText);
        attemptsRemainingView.setText(attemptsRemainingText);

        mydb = new DB(this);

        //now we need to pull from the database and get the cryptogram text itself, and set it for the user
        encodedPhrase = mydb.getCryptogramEncodedText(puzzleName);
        currentSolution = encodedPhrase;
        currentSolutionView.setText(encodedPhrase);
        encodedPhraseView.setText(currentSolution);

        //and we initialize the mappings to be like-for-like
        characterHashMap = new HashMap<>();
        characterHashMap.put('a', 'a');
        characterHashMap.put('b', 'b');
        characterHashMap.put('c', 'c');
        characterHashMap.put('d', 'd');
        characterHashMap.put('e', 'e');
        characterHashMap.put('f', 'f');
        characterHashMap.put('g', 'g');
        characterHashMap.put('h', 'h');
        characterHashMap.put('i', 'i');
        characterHashMap.put('j', 'j');
        characterHashMap.put('k', 'k');
        characterHashMap.put('l', 'l');
        characterHashMap.put('m', 'm');
        characterHashMap.put('n', 'n');
        characterHashMap.put('o', 'o');
        characterHashMap.put('p', 'p');
        characterHashMap.put('q', 'q');
        characterHashMap.put('r', 'r');
        characterHashMap.put('s', 's');
        characterHashMap.put('t', 't');
        characterHashMap.put('u', 'u');
        characterHashMap.put('v', 'v');
        characterHashMap.put('w', 'w');
        characterHashMap.put('x', 'x');
        characterHashMap.put('y', 'y');
        characterHashMap.put('z', 'z');

        //populate the source char spinner with possible letter selections from strings.xml file
        Spinner sourceCharSpinner = (Spinner) findViewById(R.id.Spinner_PlayCryptogram_NewLetterFrom);
        ArrayAdapter<CharSequence> sourceCharAdapter = ArrayAdapter.createFromResource(
                this, R.array.letters_array, android.R.layout.simple_spinner_item);

        sourceCharAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sourceCharSpinner.setAdapter(sourceCharAdapter);

        //define a listener for the spinner to set the Text
        sourceCharSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                sourceLetterSelectionIndex = position;
                sourceLetter = letterArray[position];
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });

        //do the same for the destination char spinner
        Spinner destCharSpinner = (Spinner) findViewById(R.id.Spinner_PlayCryptogram_NewLetterTo);
        ArrayAdapter<CharSequence> destCharAdapter = ArrayAdapter.createFromResource(
                this, R.array.letters_array, android.R.layout.simple_spinner_item);

        destCharAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        destCharSpinner.setAdapter(destCharAdapter);

        //define a listener for the spinner to set the Text
        destCharSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                destinationLetterSelectionIndex = position;
                destinationLetter = letterArray[position];
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });

        //when a letter cipher substitution is done, we need to update the display phrase for the user
        Button addLetterButton = (Button) findViewById(R.id.Button_PlayCryptogram_NewLetterSave);
        addLetterButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //find the letters and substitute them in the character map
                characterHashMap.put(Character.toLowerCase(sourceLetter), Character.toLowerCase(destinationLetter));

                //and now update the display string to how it looks
                updateDisplayedSolutionPhrase(encodedPhrase);
            }
        });

        /**when the user checks a solution, several things need to be done, namely
         * 1. maybe add this to the puzzle table, if this is the first solution attempts
         * 2. check the solution
         * 3. update the attempts remaining / taken
         * 4. if the solution is correct, update the date
         * 5. if the solution is incorrect, maybe lock the puzzle
         */
        Button checkSolutionButton = (Button) findViewById(R.id.button_PlayCryptogram_attemptSolution);
        checkSolutionButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                //first thing we need to do is maybe insert this into the puzzles table
                if (!mydb.puzzleExists(currentUser, puzzleName)) {
                    boolean insertedPuzzle = mydb.insertPuzzle(currentUser, puzzleName);
                }

                //now that the puzzle has been inserted, we can check the solution
                int solutionResult = mydb.checkSolution(currentUser, puzzleName, currentSolution);

                //System.out.println(solutionResult);

                if (solutionResult == -1) {
                    Toast.makeText(PlayCryptogramActivity.this, "An error has occurred", Toast.LENGTH_LONG).show();
                } else if (solutionResult == 0) {

                    //update attempts remaining
                    int attemptsRemaining = Integer.parseInt(String.valueOf(attemptsRemainingView.getText()));
                    attemptsRemaining -= 1;
                    attemptsRemainingView.setText(String.valueOf(attemptsRemaining));

                    //update attempts taken
                    int attemptsTaken = Integer.parseInt(String.valueOf(attemptsTakenView.getText()));
                    attemptsTaken += 1;
                    attemptsTakenView.setText(String.valueOf(attemptsTaken));

                    attemptsRemainingView.requestFocus();
                    if (attemptsRemaining==0) {
                        Toast.makeText(PlayCryptogramActivity.this, PUZZLE_EXPIRED, Toast.LENGTH_LONG).show();
                        Intent intent  = new Intent(PlayCryptogramActivity.this, PlayerHomeScreenActivity.class);
                        intent.putExtra("CurrentUser", currentUser);
                        startActivity(intent);
                        finish(); //no going back!
                    } else {
                        attemptsRemainingView.setError(INCORRECT_SOLUTION_ATTEMPT);
                    }
                } else {
                    Toast.makeText(PlayCryptogramActivity.this, PUZZLE_SOLVED, Toast.LENGTH_LONG).show();
                    Intent intent  = new Intent(PlayCryptogramActivity.this, PlayerHomeScreenActivity.class);
                    intent.putExtra("CurrentUser", currentUser);
                    startActivity(intent);
                    finish(); //no going back!
                }
            }
        });
    }

    public void updateDisplayedSolutionPhrase(String phrase) {
        Character cur;
        StringBuilder display = new StringBuilder();
        for (int i=0; i<phrase.length(); i++) {
            cur = phrase.charAt(i);

            //substitute characters accordingly
            if (characterHashMap.containsKey(Character.toLowerCase(cur))) {
                if (Character.isUpperCase(cur)) {
                    display.append(Character.toUpperCase(characterHashMap.get(Character.toLowerCase(cur))));
                } else {
                    display.append(characterHashMap.get(cur));
                }

            } else {
                display.append(cur);
            }
        }
        currentSolution = display.toString();
        currentSolutionView.setText(currentSolution);
    }
}
