package com.soonoo.mobilecampus;

import android.app.ActionBar;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.PrintWriter;
import java.io.StringWriter;


public class LibrarySeatInfoActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.slide_up, R.anim.slide_down);
        setContentView(R.layout.activity_library_seat_info);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        try{
            Document doc = parseHtml(new GetSeatHtml().execute().get());

            WebView wv = (WebView) findViewById(R.id.webview_library);
            wv.loadDataWithBaseURL("", doc.toString(), "text/html", "euc-kr", "");

        } catch(Exception e){
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            String exceptionAsStrting = sw.toString();
            Log.e("INFO", exceptionAsStrting);
            e.printStackTrace();
        }
    }

    public class GetSeatHtml extends AsyncTask<Void, Void, Document> {
        public Document doInBackground(Void... p){
            String seatHtml = User.getHtml("GET", Sites.LIBRARY_SEAT_URL, "euc-kr");
            return Jsoup.parse(seatHtml);
        }
    }

    public Document parseHtml(Document doc){
        doc.select("table").attr("width", "100%").attr("style", "font-size:80%;");
        doc.select("tr:eq(1) > td:eq(1)").attr("width", "23%");
        doc.select("br + font").remove();
        doc.select("a").unwrap();
        return doc;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_seat_info, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.actionbar_refresh:
                try {
                    Document doc = parseHtml(new GetSeatHtml().execute().get());

                    WebView wv = (WebView) findViewById(R.id.webview_library);
                    wv.loadDataWithBaseURL("", doc.toString(), "text/html", "euc-kr", "");
                }catch(Exception e){
                }
                return true;
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_up, R.anim.slide_down);
    }
}
