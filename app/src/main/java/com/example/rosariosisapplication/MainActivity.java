package com.example.rosariosisapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.CheckBox;
import android.widget.Toast;

import java.io.IOException;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    /* Define the UI elements */
    private EditText eName;
    private EditText ePassword;
    private TextView eAttemptsInfo;
    private Button eLogin;
    private CheckBox check;
    private int counter = 5;

    String userName = "";
    String userPassword = "";


    /* Class to hold credentials */
    class Credentials
    {
        String name = "bing";
        String password = "bong";
    }

    boolean isValid = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /* Bind the XML views to Java Code Elements */
        eName = findViewById(R.id.etName);
        ePassword = findViewById(R.id.etPassword);
        eLogin = findViewById(R.id.btnLogin);
        check = findViewById(R.id.checkBox);

        SharedPreferences preferences = getSharedPreferences("checkbox", MODE_PRIVATE);
        String checkbox = preferences.getString("remember","");
        if(checkbox.equals("true")){
            Intent intent = new Intent(MainActivity.this, HomePageActivity.class);
            startActivity(intent);
        }else if(checkbox.equals("false")){
            Toast.makeText(this,"Please Sign In.", Toast.LENGTH_SHORT).show();
        }

        /* Describe the logic when the login button is clicked */
        eLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                /* Obtain user inputs */
                userName = eName.getText().toString();
                userPassword = ePassword.getText().toString();

                /* Check if the user inputs are empty */
                if(userName.isEmpty() || userPassword.isEmpty())
                {
                    /* Display a message toast to user to enter the details */
                    Toast.makeText(MainActivity.this, "Please enter name and password!", Toast.LENGTH_SHORT).show();

                }else {

                    /* Validate the user inputs */
                    isValid = validate(userName, userPassword);

                    /* Validate the user inputs */

                    /* If not valid */
                    if (!isValid) { {
                        Toast.makeText(MainActivity.this, "Incorrect credentials, please try again!", Toast.LENGTH_SHORT).show();
                    }
                    }
                    /* If valid */
                    else {

                        /* Allow the user in to your app by going into the next activity */
                        startActivity(new Intent(MainActivity.this, HomePageActivity.class));
                    }

                }
            }
        });

        check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (compoundButton.isChecked()) {

                    SharedPreferences preferences = getSharedPreferences("checkbox", MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("remember", "true");
                    editor.apply();
                    Toast.makeText(MainActivity.this, "Checked", Toast.LENGTH_SHORT).show();

                } else if (!compoundButton.isChecked()) {

                    SharedPreferences preferences = getSharedPreferences("checkbox", MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("remember", "false");
                    editor.apply();
                    Toast.makeText(MainActivity.this, "Save Credentials?", Toast.LENGTH_SHORT).show();

                }
            }
        });

    }

    /* Validate the credentials */
    private boolean validate(String userName, String userPassword)
    {
        /* Get the object of Credentials class */
        Credentials credentials = new Credentials();

        /* Check the credentials */
        if(userName.equals(credentials.name) && userPassword.equals(credentials.password))
        {
            return true;
        }

        return false;
    }
}