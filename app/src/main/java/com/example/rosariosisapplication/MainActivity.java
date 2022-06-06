package com.example.rosariosisapplication;

//Import Statements

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;

import org.jsoup.Connection;
import org.jsoup.Jsoup;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.CheckBox;
import android.widget.Toast;

import java.io.IOException;

//Main Class
public class MainActivity extends AppCompatActivity {

    //Define the UI elements
    private EditText eName;
    private EditText ePassword;
    private Button eLogin;
    private CheckBox check;
    public boolean isValid2 = false;
    public boolean isValid = false;

    boolean canConnect = false;

    String userName = "";
    String userPassword = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        /* Bind the XML views to Java Code Elements */
        eName = findViewById(R.id.etName);
        ePassword = findViewById(R.id.etPassword);
        eLogin = findViewById(R.id.btnLogin);
        check = findViewById(R.id.checkBox);


        /* Algorithm to check if username and password are saved
        * "UserPrefs" Shared preferences stores information on whether the checkbox was checked before
        * "SavedData" Shared preferences stores information about saved username and password */
        SharedPreferences preferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        SharedPreferences SavedData = getSharedPreferences("SavedData", MODE_PRIVATE);

        String checkbox = preferences.getString("remember","");
        String savedusername = SavedData.getString("username", "");

        /* If checkbox was previously ticked and saved username has length greater than 0 */
        if (checkbox.equals("true") && savedusername.length() > 0) {

            /* Create intent with saved username and password, and start new HomePageActivity with Intent */
            Intent intent = new Intent(MainActivity.this, HomePageActivity.class);
            intent.putExtra("username", preferences.getString("username", ""));
            intent.putExtra("userpassword", preferences.getString("userpassword", ""));
            startActivity(intent);
            finish();
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
                } else {

                    /* Validate the user inputs */
                    login_webscrape lw = new login_webscrape(); //not sure if this part works
                    lw.execute();
                }
            }
        });

    }

    private class login_webscrape extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {

            try {
                final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/51.0.2704.103 Safari/537.36";
                final String LOGIN_FORM_URL = "https://rosariosis.asianhope.org/index.php";

                Connection.Response loginForm = null;

                // If the connection fails, it goes to catch, and canConnect stays false.
                try {
                    loginForm = Jsoup.connect(LOGIN_FORM_URL)
                            .method(Connection.Method.GET)
                            .userAgent(USER_AGENT)
                            .followRedirects(false)
                            .execute();
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }

                canConnect = true;

                loginForm = Jsoup.connect(LOGIN_FORM_URL)
                        .cookies(loginForm.cookies())
                        .data("USERNAME", userName)
                        .data("PASSWORD", userPassword)
                        .method(Connection.Method.POST)
                        .followRedirects(true)
                        .userAgent(USER_AGENT)
                        .execute();

                // Checks if username and password are valid
                isValid = false;

                if(!(loginForm.url().toString().equals(LOGIN_FORM_URL))){
                    isValid = true;
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {

            // Checks if it was able to connect and if the credentials were valid
            if (canConnect == true) {

                /* If not valid */
                if (!isValid) {
                    Toast.makeText(MainActivity.this, "Incorrect credentials, please try again!", Toast.LENGTH_SHORT).show();
                }
                /* If valid */
                else {

                    // If checkbox was checked, set string "remember" in UserPrefs to "true"
                    // If checkbox was checked, save username and userpassword to SavedData]
                    if (check.isChecked()) {
                        SharedPreferences SavedData = getSharedPreferences("SavedData", MODE_PRIVATE);
                        SharedPreferences preferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);

                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString("remember", "true");
                        editor.commit();

                        editor = SavedData.edit();
                        editor.putString("username", userName);
                        editor.putString("userpassword", userPassword);
                        editor.commit();
                    }
                    // Else, set string "remember" in UserPrefs to false
                    else {
                        SharedPreferences preferences1 = getSharedPreferences("UserPrefs", MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences1.edit();
                        editor.putString("remember", "false");
                        editor.commit();
                    }

                    // Allow the user in to your app by going into the next activity
                    Intent intent = new Intent(MainActivity.this, HomePageActivity.class);
                    intent.putExtra("username", userName);
                    intent.putExtra("userpassword", userPassword);
                    startActivity(intent);
                    finish();
                }
            }
            // If not able to connect, show a dialog
            else {
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Error")
                        .setMessage("We couldn't connect to RosarioSIS. Your internet might have a problem, or the website might be down.")
                        .setNegativeButton("Close", null)
                        .show();
            }
        }
    }
}