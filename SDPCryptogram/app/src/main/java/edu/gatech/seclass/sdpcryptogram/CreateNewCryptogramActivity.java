package edu.gatech.seclass.sdpcryptogram;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.view.View;
import android.view.View.OnClickListener;
import android.content.Intent;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class CreateNewCryptogramActivity extends AppCompatActivity{

    //error strings
    String CRYPTONAME_ALREADY_EXISTS = "This name already exists";
    String MISSING_FIELD = "This field is required!";
    String NO_ATTEMPTS = "Must be at least 1";

    String currentUser;

    //these are the fields chosen by the user in the form
    EditText enteredCryptoName;
    EditText enteredSolutionPhrase;
    EditText enteredMaxAttempts;
    TextView phraseDisplay;

    //these are the fields when parsed by the event listeners
    String cryptoName = "";
    String solutionPhrase = "";
    String encodedPhrase = "";
    int maxAttempts = 3;

    //some spinners for substitution letters
    Spinner destCharSpinner;
    Spinner sourceCharSpinner;

    //the spinners will map to the alphabetical substitutions, which will be initialized as a like-for-like mapping
    Map<Character, Character> characterHashMap;
    int sourceLetterSelectionIndex = 0;
    int destinationLetterSelectionIndex = 0;
    Character sourceLetter = 'A';
    Character destinationLetter = 'A';
    char[] letterArray = {'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z'};
    //and of course, the database is needed to insert new cryptograms and check for unique names
    DB mydb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_cryptogram);

        currentUser = getIntent().getStringExtra("CurrentUser");

        mydb = new DB(this);

        enteredCryptoName = (EditText) findViewById(R.id.editText_CreateNewCryptogram_puzzleName);
        enteredSolutionPhrase = (EditText) findViewById(R.id.editText_CreateNewCryptogram_solutionPhrase);
        enteredMaxAttempts = (EditText) findViewById(R.id.editText_CreateNewCryptogram_maxAttempts);
        phraseDisplay = (TextView) findViewById(R.id.textView_CreateNewCryptogram_decodedPhrase);

        //to start off, all the letters map to themselves
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

        //when the user enters a new cryptoname, save it.
        enteredCryptoName.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                cryptoName = s.toString();
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });

        enteredSolutionPhrase.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                solutionPhrase = s.toString();
                updateDisplayedEncodedPhrase(solutionPhrase);
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });

        enteredMaxAttempts.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                if (s.toString().length() == 0) {
                    maxAttempts = 0;
                } else {
                    maxAttempts = Integer.parseInt(s.toString());
                }
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });

        //populate the source char spinner with possible letter selections from strings.xml file
        sourceCharSpinner = (Spinner) findViewById(R.id.Spinner_CreateNewCryptogram_NewLetterFrom);
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
        destCharSpinner = (Spinner) findViewById(R.id.Spinner_CreateNewCryptogram_NewLetterTo);
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


        //when you hit the add button for a character mapping, you update the characters
        //Run button functionality which will set error messages
        Button addLetterButton = (Button) findViewById(R.id.Button_CreateNewCryptogram_NewLetterSave);
        addLetterButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //find the letters and substitute them in the character map
                characterHashMap.put(Character.toLowerCase(sourceLetter), Character.toLowerCase(destinationLetter));

                //and now update the display string how it looks
                updateDisplayedEncodedPhrase(solutionPhrase);
            }
        });

        //when you save a cryptogram, add it to the DB and move back to the home screen, assuming all went well.
        Button saveButton = (Button) findViewById(R.id.button_CreateNewCryptogram_saveCryptogram);
        saveButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (!cryptogramIsSolvable(solutionPhrase, encodedPhrase)) {
                    destCharSpinner.requestFocus();
                    Toast.makeText(CreateNewCryptogramActivity.this, "This cryptogram is unsolvable!", Toast.LENGTH_LONG).show();
                } else if (cryptoName.length() < 1) {
                    enteredCryptoName.requestFocus();
                    enteredCryptoName.setError(MISSING_FIELD);
                } else if (solutionPhrase.length() < 1) {
                    enteredSolutionPhrase.requestFocus();
                    enteredSolutionPhrase.setError(MISSING_FIELD);
                } else if (maxAttempts < 1) {
                    enteredMaxAttempts.requestFocus();
                    enteredMaxAttempts.setError(NO_ATTEMPTS);
                } else {
                    boolean successfulInsert = mydb.insertCryptogram(cryptoName, solutionPhrase, encodedPhrase, maxAttempts);
                    if (successfulInsert) {
                        Toast.makeText(CreateNewCryptogramActivity.this, "Cryptogram Created! Wahoo!", Toast.LENGTH_LONG).show();
                        Intent myIntent = new Intent(CreateNewCryptogramActivity.this,
                                PlayerHomeScreenActivity.class);
                        myIntent.putExtra("CurrentUser", currentUser);
                        startActivity(myIntent);
                        finish();
                    } else {
                        enteredCryptoName.requestFocus();
                        enteredCryptoName.setError(CRYPTONAME_ALREADY_EXISTS);
                    }
                }
            }
        });
    }

    /** An unsolvable cryptogram is one such that multiple letters in the solution phrase map to the same letter in the char map.
     * We will test for this and return an error String if the cryptogram is unsolvable, otherwise return an empty string
     */
    public boolean cryptogramIsSolvable(String solution, String encoded) {
        Character curSolutionLetter;
        Character curEncodedLetter;

        for (int i=0; i<solution.length(); i++) {
            curSolutionLetter = Character.toLowerCase(solution.charAt(i));
            curEncodedLetter = Character.toLowerCase(encoded.charAt(i));

            if (curEncodedLetter.charValue() != curSolutionLetter.charValue()) {
                for (int j=0; j<solution.length(); j++) {
                    if (Character.toLowerCase(encoded.charAt(j)) == curEncodedLetter) {
                        if (Character.toLowerCase(solution.charAt(j)) != curSolutionLetter) {
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }

    /**here we will update a string according to the character hash map and the phrase in question. this
     * preserves capitalization, and doesn't actually replace other characters that aren't in the hash map.
     */
    public void updateDisplayedEncodedPhrase(String phrase) {
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
        encodedPhrase = display.toString();
        phraseDisplay.setText(encodedPhrase);
    }
}
