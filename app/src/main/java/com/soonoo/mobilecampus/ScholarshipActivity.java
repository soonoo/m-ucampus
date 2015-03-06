package com.soonoo.mobilecampus;

import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.PrintWriter;
import java.io.StringWriter;


public class ScholarshipActivity extends ActionBarActivity {
    Document document;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scholarship);
        //overridePendingTransition(R.anim.slide_up, R.anim.slide_down);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        new GetScholarshipHtml().execute();
    }


    public class GetScholarshipHtml extends AsyncTask<Void, Void, Document>{
        ProgressBar pb = (ProgressBar)findViewById(R.id.progressbar_downloading);

        @Override
        public void onPreExecute(){
            pb.setVisibility(View.VISIBLE);
        }

        @Override
        public Document doInBackground(Void... p){
            String scholarHtml = User.getHtml("GET", Sites.SCHOLARSHIP_URL, "euc-kr");
            return Jsoup.parse(scholarHtml);
        }

        @Override
        protected void onPostExecute(Document result) {
            document = result;
            try {
                document.select("table").attr("width", "100%").attr("style", "margin-bottom:15px;");
                document.select("table:has(img)").remove();
                document.select("td").attr("style", "font-size:85%;");
                document.select("th").attr("style", "font-size:80%; background-color:#bfbfbf;");

                WebView myWebView = (WebView)findViewById(R.id.webview_scholar);
                myWebView.loadDataWithBaseURL("", document.select("table").toString(), "text/html", "utf-8", "");
            }catch (Exception e){
                WebView myWebView = (WebView)findViewById(R.id.webview_scholar);
                myWebView.loadDataWithBaseURL("", getString(R.string.message_scholar_missing), "text/html", "utf-8", "");
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
}
