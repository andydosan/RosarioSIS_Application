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


            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {

            text.setText(code);
        }
    }

    /* below is the unfixed version to log into the actuality
    public void connectWebsite(){
        try{
            Connection.Response res = Jsoup.connect("https://rosariosis.asianhope.org/")
                    .data("username",name, "password", "x")
                    .method(Connection.Method.POST)
                    .execute();
            //for cookies
            Map<String, String> cookies = res.cookies();

            //remain in session?
            Document doc = (Document) Jsoup.connect("url").cookies(cookies).get();
            tv1.setText((CharSequence) doc);

        }catch (SocketException e){
            e.printStackTrace();
        }
        catch (UncheckedIOException e){
            e.printStackTrace();
        }
        catch(Exception e){

        }
    }
     */
}