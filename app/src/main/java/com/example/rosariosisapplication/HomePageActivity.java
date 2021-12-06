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
    String password;
    final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/51.0.2704.103 Safari/537.36";
    final String LOGIN_FORM_URL = "https://rosariosis.asianhope.org/index.php";
    //rather than the grades, the initial log in action url is the portral page possibly?
    final String LOGIN_ACTION_URL = "https://rosariosis.asianhope.org/Modules.php?modname=misc/Portal.php";
    final String GRADES_URL = "https://rosariosis.asianhope.org/Modules.php?modname=Grades/StudentGrades.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        password = getResources().getString(R.string.andy_password);
        text = (TextView) findViewById(R.id.html);
        password = getString(R.string.andy_password);

        description_webscrape dw = new description_webscrape();
        dw.execute();

    }

    private class description_webscrape extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            try {
                Connection.Response loginForm = Jsoup.connect(LOGIN_FORM_URL)
                        .method(Connection.Method.GET)
                        .userAgent(USER_AGENT)
                        .execute();

                loginForm = Jsoup.connect(LOGIN_FORM_URL)
                        .cookies(loginForm.cookies())
                        .data("USERNAME", "adosan")
                        .data("PASSWORD", password)
                        .method(Connection.Method.POST)
                        .followRedirects(true)
                        .userAgent(USER_AGENT)
                        .execute();

                org.jsoup.nodes.Document doc = Jsoup.connect(GRADES_URL)
                        .cookies(loginForm.cookies())
                        .userAgent(USER_AGENT)
                        .get();

                code = doc.html();

            } catch (IOException e) {
                e.printStackTrace();
            }



            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if(code != null){
                text.setText(code);
            }
            else {
                text.setText("Something went wrong! There was nothing downloaded.");
            }

        }
    }


}