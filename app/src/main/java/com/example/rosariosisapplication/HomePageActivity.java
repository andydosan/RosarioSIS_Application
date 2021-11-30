package com.example.rosariosisapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.UncheckedIOException;
import org.w3c.dom.Document;

import java.net.SocketException;
import java.util.Map;

public class HomePageActivity extends AppCompatActivity {

    TextView text;
    String code;

    //NOTE: password is a RosarioSis password stored in strings.xml. DO NOT OPEN STRINGS.XML!
    String password = getString(R.string.andy_password);
    final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/51.0.2704.103 Safari/537.36";
    final String LOGIN_FORM_URL = "https://rosariosis.asianhope.org/";
    final String LOGIN_ACTION_URL = "https://rosariosis.asianhope.org/Modules.php?modname=Grades/StudentGrades.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        text = findViewById(R.id.html);

    }

    private class description_webscrape extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            //With this you login and a session is created
            Connection.Response res = null;
            try {
                res = Jsoup.connect("https://rosariosis.asianhope.org/")
                        //DO NOT SCROLL RIGHT
                        .data("USERNAME", "adosan", "PASSWORD", password)
                        .method(Connection.Method.POST)
                        .execute();
            } catch (IOException e) {
                e.printStackTrace();
            }

            //This will get you cookies
            Map<String, String> loginCookies = res.cookies();

            //Here you parse the page that you want. Put the url that you see when you have logged in
            try {
                //this should work in pulling html of the login page :) -yc
                org.jsoup.nodes.Document doc = Jsoup.connect("https://rosariosis.asianhope.org/Modules.php?modname=Grades/StudentGrades.php").cookies(loginCookies).get();
                code = doc.html();

            } catch (IOException e) {
                e.printStackTrace();
            }
            // idk im just doing things at this point https://sodocumentation.net/jsoup/topic/4631/logging-into-websites-with-jsoup (first example here)
            /* this is the code from recently yes yes (haven't tested it out yet)
            try {
                Connection.Response loginForm = Jsoup.connect(LOGIN_FORM_URL)
                        .method(Connection.Method.GET)
                        .userAgent(USER_AGENT)
                        .execute();

                // this is the document containing response html
                org.jsoup.nodes.Document loginDoc = loginForm.parse();
                // save the cookies to be passed on to next request
                HashMap <String, String> cookies = new HashMap<>(loginForm.cookies());

                // # Prepare login credentials
                String authToken = loginDoc.select("#login > form > div:nth-child(1) > input[type=\"hidden\"]:nth-child(2)")
                        .first()
                        .attr("value");

                HashMap <String, String> formData = new HashMap<>();
                formData.put("commit", "Sign in");
                formData.put("utf8", "e2 9c 93");
                formData.put("login", "adosan");
                formData.put("password", password);
                formData.put("authenticity_token", authToken);

                // # Now send the form for login
                Connection.Response homePage = Jsoup.connect(LOGIN_ACTION_URL)
                        .cookies(cookies)
                        .data(formData)
                        .method(Connection.Method.POST)
                        .userAgent(USER_AGENT)
                        .execute();
                text.setText(homePage.parse().html());
            } catch (IOException e) {
                e.printStackTrace();
            } */



            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {

            text.setText(code);
        }
    }


}