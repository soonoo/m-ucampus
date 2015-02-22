package com.soonoo.mobilecampus;

import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.PrintWriter;
import java.io.StringWriter;


public class ScholarshipActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scholarship);
        overridePendingTransition(R.anim.slide_up, R.anim.slide_down);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        try {
            Document doc = new GetScholarshipHtml().execute().get();

            doc.select("table").attr("width", "100%").attr("style", "margin-bottom:15px;");
            doc.select("table:has(img)").remove();
            doc.select("td").attr("style", "font-size:85%;");
            doc.select("th").attr("style", "font-size:80%; background-color:#bfbfbf;");

            WebView myWebView = (WebView)findViewById(R.id.webview_scholar);
            myWebView.loadDataWithBaseURL("", doc.select("table").toString(), "text/html", "euc-kr", "");

        } catch(Exception e){
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            String exceptionAsStrting = sw.toString();
            Log.e("INFO", exceptionAsStrting);
            e.printStackTrace();
        }
    }
    public class GetScholarshipHtml extends AsyncTask<Void, Void, Document>{
        @Override
        public Document doInBackground(Void... p){
            String scholarHtml = User.getHtml("GET", Sites.SCHOLARSHIP_URL, "euc-kr");
            return Jsoup.parse(scholarHtml);
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
        overridePendingTransition(R.anim.slide_up, R.anim.slide_down);
    }
}
