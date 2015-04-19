package com.soonoo.mobilecampus;

import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;


public class DiningActivity extends ActionBarActivity {
    Element element;
    String day;
    ArrayList<String> list = new ArrayList<>();

    ArrayList<String> mon = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dining);
    }

    public class GetMenu extends AsyncTask<Void, Void, Document> {
        @Override
        public Document doInBackground(Void... p){
            return Jsoup.parse(User.getHtml("GET", Sites.DINING_URL, "utf-8"));
        }

        @Override
        public void onPostExecute(Document document){
            day = document.select(".search_day").text();
            element = document.select(".menu_table").first().nextElementSibling();
            element.select("tr").remove();

            for(Element ele: element.select("tr")){

            }
        }
    }

}
