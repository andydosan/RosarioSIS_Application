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
import org.jsoup.select.Elements;
import org.w3c.dom.Document;

import java.net.SocketException;
import java.util.Map;

public class HomePageActivity extends AppCompatActivity {

    TextView text;
    String code;

    //NOTE: password is a RosarioSis password stored in strings.xml. DO NOT OPEN STRINGS.XML!
    String password;
    ArrayList<ArrayList<String>> grades = new ArrayList<ArrayList<String>>();
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
        //text = (TextView) findViewById(R.id.html);
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
                        .data("USERNAME", "rseah")
                        .data("PASSWORD",                                               "significantcookie52")
                        .method(Connection.Method.POST)
                        .followRedirects(true)
                        .userAgent(USER_AGENT)
                        .execute();

                org.jsoup.nodes.Document doc = Jsoup.connect(GRADES_URL)
                        .cookies(loginForm.cookies())
                        .userAgent(USER_AGENT)
                        .get();

                code = doc.html();

                org.jsoup.nodes.Element table = doc.select("table[class=list widefat rt]").get(0);
                Elements rows = table.select("tr");

                for (int i = 1; i < rows.size(); i++) { //first row is the col names so skip it.
                    org.jsoup.nodes.Element row = rows.get(i);
                    Elements cols = row.select("td");
                    Elements link = row.select("a[href]");

                    ArrayList<String> temp = new ArrayList<String>();
                    temp.add(cols.get(0).text()); //Class name
                    temp.add(cols.get(1).text()); //Teacher name
                    temp.add(cols.get(3).text()); //Grade (percent)
                    temp.add(link.get(0).attr("href"));
                    grades.add(temp);
                }

                for (int i = 0; i < grades.size(); i++) {
                    for (int j = 0; j < grades.get(i).size(); j++) {
                        Log.d("Myapp", grades.get(i).get(j));
                    }
                }



            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            /*if(code != null){
                text.setText(code);
            }
            else {
                text.setText("Something went wrong! There was nothing downloaded.");
            }
*/
        }
    }


}