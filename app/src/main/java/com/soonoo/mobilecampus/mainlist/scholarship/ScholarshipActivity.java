package com.soonoo.mobilecampus.mainlist.scholarship;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.CheckBox;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.soonoo.mobilecampus.Controller;
import com.soonoo.mobilecampus.LoginView;
import com.soonoo.mobilecampus.R;
import com.soonoo.mobilecampus.Sites;
import com.soonoo.mobilecampus.mainlist.HomeView;
import com.soonoo.mobilecampus.util.User;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;


public class ScholarshipActivity extends AppCompatActivity {
    Document document;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scholarship);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Tracker t = ((Controller) getApplication()).getTracker(Controller.TrackerName.APP_TRACKER);
        t.setScreenName("ScholarshipActivity");
        t.send(new HitBuilders.AppViewBuilder().build());

        try {
            new GetScholarshipHtml().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } catch(Exception e){
            ActivityCompat.finishAffinity(this);
            startActivity(new Intent(this, LoginView.class));
        }
    }


    public class GetScholarshipHtml extends AsyncTask<Void, Void, Document> {
        ProgressBar pb = (ProgressBar) findViewById(R.id.progressbar_downloading);

        @Override
        public void onPreExecute() {
            pb.setVisibility(View.VISIBLE);
        }

        @Override
        public Document doInBackground(Void... p) {
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
                document.select("th").attr("style", "font-size:80%; background-color:#e5e5e5;");

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

    @Override
    public void finish() {
        super.finish();
        //overridePendingTransition(R.anim.slide_up, R.anim.slide_down);
    }

    @Override
    public void onRestart() {
        super.onRestart();
        new LoginView.OnBack().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
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
