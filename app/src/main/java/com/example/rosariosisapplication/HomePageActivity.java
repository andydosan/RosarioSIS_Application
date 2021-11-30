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
    String password;
    String bruh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        password = getResources().getString(R.string.andy_password);

        text = (TextView) findViewById(R.id.html);

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
            //With this you login and a session is created
            Connection.Response res = null;

            String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko)" +
                    " Chrome/56.0.2924.87 Safari/537.36";
            try {
                res = Jsoup.connect("https://rosariosis.asianhope.org/index.php/")
                        .data("login", "adosan")
                        .data("password", password)
                        .userAgent(USER_AGENT)
                        .method(Connection.Method.POST)
                        .execute();
                //This will get you cookies
                Map<String, String> loginCookies = res.cookies();
                org.jsoup.nodes.Document doc = Jsoup.connect("https://rosariosis.asianhope.org/Modules.php?modname=misc/Portal.php").cookies(loginCookies).get();
                code = doc.html();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
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