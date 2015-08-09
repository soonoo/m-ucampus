package com.soonoo.mobilecampus;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.ProgressBar;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class SyllabusView extends ActionBarActivity {
    Document document;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_syllabus);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Tracker t = ((Controller) getApplication()).getTracker(Controller.TrackerName.APP_TRACKER);
        t.setScreenName("SyllabusActivity");
        t.send(new HitBuilders.AppViewBuilder().build());

        Intent intent = getIntent();
        int position = intent.getIntExtra("subIndex", 1);
        String query = intent.getStringExtra("query");

        new GetSyllabus().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, User.subCode.get(position - 1), query);
    }

    public class GetSyllabus extends AsyncTask<String, Void, String> {
        ProgressBar pb = (ProgressBar) findViewById(R.id.progressbar_downloading);

        @Override
        public void onPreExecute() {
            pb.setVisibility(View.VISIBLE);
        }

        @Override
        public String doInBackground(String... str) {
            String url;
            if(str[1] == null) url = Sites.SYLLABUS_URL + Parser.getSubQuery(str[0]);
            else url = Sites.SYLLABUS_URL + str[1];

            String sylHtml = User.getHtml("GET", url, "euc-kr");
            Document document = Jsoup.parse(sylHtml);
            try {
                document.select("td").attr("style", "font-size:75%; ");
                document.select("td.bgtable1").attr("style", "background-color:#edf1f3; font-size:60%; white-space:nowrap;");
                document.select("table:contains(출력이 안될)").remove();
                document.select("table").attr("width", "100%");

                for (Element element : document.select("input[type^=text]")) {
                    element.remove();
                }

                for (Element element : document.select("td:contains(연락처), td:contains(이동전화), td:contains(이메일)")) {
                    if(element.nextElementSibling() != null)
                        element.nextElementSibling().attr("style", "color:#a70500; text-decoration:underline; font-size:80%;");
                }
                return document.toString();
            } catch (Exception e) {
                e.printStackTrace();
                WebView myWebView = (WebView) findViewById(R.id.wv);
                return null;
            }
        }

        @Override
        protected void onPostExecute(String result) {
            WebView myWebView = (WebView) findViewById(R.id.wv);
            if(!(result == null)){
                myWebView.loadDataWithBaseURL("", result.toString(), "text/html", "UTF-8", "");
            }else{
                myWebView.loadDataWithBaseURL("", getString(R.string.message_syllabus_missing), "text/html", "UTF-8", "");
            }
            pb.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
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
        //overridePendingTransition(R.anim.slide_up, R.anim.slide_down);
    }

    @Override
    protected void onStart() {
        super.onStart();
        GoogleAnalytics.getInstance(this).reportActivityStart(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        GoogleAnalytics.getInstance(this).reportActivityStop(this);
    }
}