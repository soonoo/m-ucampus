package com.soonoo.mobilecampus.mainlist.scholarship;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.ProgressBar;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.soonoo.mobilecampus.AnalyticsApplication;
import com.soonoo.mobilecampus.R;
import com.soonoo.mobilecampus.Sites;
import com.soonoo.mobilecampus.util.User;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;


public class ScholarshipActivity extends AppCompatActivity {
    Document document;
    private Tracker mTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scholarship);

        AnalyticsApplication application = (AnalyticsApplication) getApplication();
        mTracker = application.getDefaultTracker();
        mTracker.setScreenName("Scholar");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        new GetScholarshipHtml().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }


    public class GetScholarshipHtml extends AsyncTask<Void, Void, Document> {
        ProgressBar pb = (ProgressBar) findViewById(R.id.progressbar_downloading);

        @Override
        public void onPreExecute() {
            pb.setVisibility(View.VISIBLE);
        }

        @Override
        public Document doInBackground(Void... p) {
            String scholarHtml = null;
            try {
                scholarHtml = User.getHtml("GET", Sites.SCHOLARSHIP_URL, "euc-kr");
            } catch (Exception e) {
                finish();
            }
            return Jsoup.parse(scholarHtml);
        }

        @Override
        protected void onPostExecute(Document result) {
            document = result;
            try {
                document.select("table:has(img)").remove();
                document.select("td").attr("style", "font-size:85%;");
                document.select("th").attr("style", "font-size:80%; background-color:#e5e5e5;");
                document.select("table").attr("width", "100%").attr("style", "margin-bottom:15px;");
                WebView myWebView = (WebView) findViewById(R.id.webview_scholar);
                myWebView.loadDataWithBaseURL("", document.select("table").toString(), "text/html", "utf-8", "");
            } catch (Exception e) {
                WebView myWebView = (WebView) findViewById(R.id.webview_scholar);
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
}
