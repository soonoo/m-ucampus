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
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.PrintWriter;
import java.io.StringWriter;


public class LibrarySeatInfoActivity extends ActionBarActivity {

    Document document = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.slide_up, R.anim.slide_down);
        setContentView(R.layout.activity_library_seat_info);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        new GetSeatHtml(new OnRequestCompleted() {
            @Override
            public void onRequestCompleted(Document doc) {
                document = doc;
            }
        }).execute();


    }

    public class GetSeatHtml extends AsyncTask<Void, Void, Document>  {
        ProgressBar pb = (ProgressBar)findViewById(R.id.progressbar_downloading);
        public OnRequestCompleted delegate = null;

        public GetSeatHtml(OnRequestCompleted caller){
            this.delegate = caller;
        }

        public void onPreExecute(){
            pb.setVisibility(View.VISIBLE);
        }

        public Document doInBackground(Void... p){
            String seatHtml = User.getHtml("GET", Sites.LIBRARY_SEAT_URL, "euc-kr");
            return Jsoup.parse(seatHtml);
        }

        @Override
        public void onPostExecute(Document doc){
            delegate.onRequestCompleted(doc);
            document = parseHtml(document);

            WebView wv = (WebView) findViewById(R.id.webview_library);
            wv.loadDataWithBaseURL("", document.toString(), "text/html", "utf-8", "");
            pb.setVisibility(View.GONE);
        }
    }

    public Document parseHtml(Document document){
        document.select("table").attr("width", "100%").attr("style", "font-size:80%;");
        document.select("tr:eq(1) > td:eq(1)").attr("width", "23%");
        document.select("br + font").first().text("정보 갱신 간격은 1분입니다.");
        document.select("a").unwrap();
        return document;
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
                    new GetSeatHtml(new OnRequestCompleted() {
                        @Override
                        public void onRequestCompleted(Document doc) {
                            document = doc;
                        }
                    }).execute();

                    WebView wv = (WebView) findViewById(R.id.webview_library);
                    wv.loadDataWithBaseURL("", document.toString(), "text/html", "euc-kr", "");
                }catch(Exception e){
                }
                Toast.makeText(getApplicationContext(), "갱신되었습니다.", Toast.LENGTH_SHORT).show();
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
