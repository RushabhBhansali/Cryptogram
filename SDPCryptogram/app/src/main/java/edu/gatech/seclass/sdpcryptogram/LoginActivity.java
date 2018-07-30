package edu.gatech.seclass.sdpcryptogram;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class LoginActivity extends AppCompatActivity {

    String USERNAME_DOES_NOT_EXIST = "This username doesn't exist";
    String myUsername;
    EditText enteredUsername;
    DB mydb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mydb = new DB(this);

        enteredUsername = (EditText) findViewById(R.id.edittext_login_screen_username);

        enteredUsername.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
                myUsername = s.toString();
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });

        Button runButton = (Button) findViewById(R.id.button_login_screen_LogIn);
        runButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                //TODO - finish this input validation
                boolean successfulLogin = mydb.loginPlayer(myUsername);
                if (successfulLogin) {
                    Intent myIntent = new Intent(LoginActivity.this,
                            PlayerHomeScreenActivity.class);
                    myIntent.putExtra("CurrentUser", myUsername);
                    startActivity(myIntent);
                    finish();
                } else {
                    enteredUsername.requestFocus();
                    enteredUsername.setError(USERNAME_DOES_NOT_EXIST);
                }

            }

        });

    }
}
