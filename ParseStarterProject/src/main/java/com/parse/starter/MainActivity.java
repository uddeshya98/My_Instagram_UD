/*
 * Copyright (c) 2015-present, Parse, LLC.
 * All rights reserved.
 *
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree. An additional grant
 * of patent rights can be found in the PATENTS file in the same directory.
 */
package com.parse.starter;
import android.app.Activity;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseAnonymousUtils;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

import java.util.List;


public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnKeyListener {

    EditText username;
    EditText password;

    Button button;

    TextView changeMode;

    Boolean signUpModeActive = true;

    ConstraintLayout background;

    ImageView logo;

    private Boolean exit = false;


    @Override
    public void onBackPressed() {
        if (exit) {
            finish(); // finish activity
        } else {
            Toast.makeText(this, "Press Back again to Exit.",
                    Toast.LENGTH_SHORT).show();
            exit = true;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    exit = false;
                }
            }, 3 * 1000);

        }

    }

    public void showUserList() {

        Intent intent = new Intent(getApplicationContext(), UserListActivity.class);
        intent.putExtra("username", ParseUser.getCurrentUser().getUsername());
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);

    }

    @Override
    public void onClick(View v) {


        if (v.getId() == R.id.changeSignUp){

            if(signUpModeActive){

                button.setText("Login");
                changeMode.setText("Or, Sign up");
                signUpModeActive = false;

            }else {

                button.setText("Sign up");
                changeMode.setText("or, Login");
                signUpModeActive = true;

            }


        }else if (v.getId() == R.id.background || v.getId() == R.id.logoImageView){

            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService
                    (INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);


        }

    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {

        if (keyCode == 66 && event.getAction() == 0){

            signUp(v);

        }


        return false;
    }



    public void signUp(View view){

        if (username.getText().toString().matches("") || password.getText().toString().matches
                ("")){

            Toast.makeText(this, "A Username and Password are required", Toast.LENGTH_SHORT).show();

        }else {

            if (signUpModeActive) {

                ParseUser user = new ParseUser();

                user.setUsername(username.getText().toString());
                user.setPassword(password.getText().toString());

                user.signUpInBackground(new SignUpCallback() {
                    @Override
                    public void done(ParseException e) {

                        if (e == null) {
                            Toast.makeText(MainActivity.this, "Signed Up as " + username.getText()
                                    .toString(), Toast
                                    .LENGTH_SHORT).show();
                            showUserList();

                        } else {
                            Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }else {

                ParseUser.logInInBackground(username.getText().toString(), password.getText()
                        .toString(), new LogInCallback() {


                    @Override
                    public void done(ParseUser user, ParseException e) {

                        if ( user != null){

                            Toast.makeText(MainActivity.this, "Logged In successfully as " +
                                    username.getText().toString(), Toast
                                    .LENGTH_SHORT).show();
                            showUserList();

                        }else {

                            Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                        }

                    }
                });

            }
        }
    }




    @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    setTitle("My Instagram");





    username = (EditText) findViewById(R.id.username);

    password = (EditText) findViewById(R.id.passsword);

    changeMode = (TextView) findViewById(R.id.changeSignUp);

    button = (Button) findViewById(R.id.button);

    changeMode.setOnClickListener(this);

    password.setOnKeyListener(this);

    background = (ConstraintLayout) findViewById(R.id.background);

    logo = (ImageView) findViewById(R.id.logoImageView);

    background.setOnClickListener(this);
    logo.setOnClickListener(this);

    if (ParseUser.getCurrentUser() != null){
        showUserList();
    }


    
    ParseAnalytics.trackAppOpenedInBackground(getIntent());


  }


}