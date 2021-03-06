package com.soonoo.mobilecampus.mainlist.grade;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
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
import org.jsoup.nodes.Element;


public class GradeView extends AppCompatActivity {
    Document doc_grade;
    WebView webView;
    String html = null;
    private Tracker mTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grade);

        AnalyticsApplication application = (AnalyticsApplication) getApplication();
        mTracker = application.getDefaultTracker();
        mTracker.setScreenName("Grade");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //if (User.isConnected(GradeView.this)) {
            new GetRankHtml().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
      //  } else Snackbar.make(findViewById(R.id.), "nono", Snackbar.LENGTH_SHORT).show();
    }


    public class GetGradeHtml extends AsyncTask<Void, Void, String> {
        ProgressBar pb = (ProgressBar) findViewById(R.id.progressbar_downloading);

        @Override
        public String doInBackground(Void... p) {
            String html = null;
            try {
                html = User.getHtml("GET", Sites.GRADE_URL, "euc-kr");
            } catch (Exception e) {
                finish();
            }
            return html;
        }

        @Override
        public void onPostExecute(String doc) {
            html += doc;
            if (html != null) doc_grade = Jsoup.parse(html);

            try {
                /*Display display = getWindowManager().getDefaultDisplay();
                Point size = new Point();
                display.getSize(size);

                int width = size.x;
                int height = size.y;*/
                doc_grade.select("table:contains(지도교수), table:contains(학위과정)").remove();
                doc_grade.select("th:contains(학정번호), td:has(img)").remove();
                doc_grade.select("td:contains(-)").remove();
                doc_grade.select("table").attr("width", "100%").attr("style", "font-size:65%;");
                doc_grade.select("table:has(input)").attr("width", "100%").attr("style", "font-size:57%");

                doc_grade.select("th:contains(학년도)").attr("bgcolor", "#cd1f1f").attr("style", "color:#ffffff;");
                doc_grade.select("th:contains(과목명), th:contains(개설학과)," +
                        " th:contains(이수구분), th:contains(학점)," +
                        " th:contains(성적), th:contains(인증구분)").attr("bgcolor", "#e5e5e5");
                //doc_grade.select("table:contains(학과별)").select("input").attr("href", "http://info.kw.ac.kr/webnote/sungjuk/sungjuk_info.html");

                Element link = doc_grade.select("table:contains(학과별)").select("input").first();

                Element aTag = doc_grade.createElement("a");
                aTag.attr("href", "http://info.kw.ac.kr/webnote/sungjuk/sungjuk_info.html");

                link.replaceWith(aTag);
                aTag.appendChild(link);


                webView = (WebView) findViewById(R.id.webview_grade);

                webView.loadDataWithBaseURL("", /*elements.toString()*/ doc_grade.toString(), "text/html", "utf-8", "");
                webView.setWebViewClient(new WebViewClient() {
                    @Override
                    public boolean shouldOverrideUrlLoading(WebView view, String url) {
                        return false;
                    }
                });

            } catch (Exception e) {
                webView = (WebView) findViewById(R.id.webview_grade);
                webView.loadDataWithBaseURL("", /*elements.toString() +*/ getString(R.string.message_grade_missing), "text/html", "utf-8", "");
            }
            pb.setVisibility(View.GONE);
        }
    }


    public class GetRankHtml extends AsyncTask<Void, Void, String> {
        ProgressBar pb = (ProgressBar) findViewById(R.id.progressbar_downloading);

        @Override
        public void onPreExecute() {
            pb.setVisibility(View.VISIBLE);
        }

        @Override
        public String doInBackground(Void... p) {
            try {
                return User.getHtml("GET", Sites.RANK_URL, "euc-kr");
            } catch (Exception e) {
                return null;
            }
        }

        @Override
        public void onPostExecute(String document) {
            html = null;
            html = document;
            new GetGradeHtml().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
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
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // Check if the key event was the Back button and if there's history

        try {
            if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.getUrl().contains("info")) {
                new GetRankHtml().execute();
                // webView
                return true;
            }
        } catch (Exception e) {
            finish();
        }
        // If it wasn't the Back key or there's no web page history, bubble up to the default
        // system behavior (probably exit the activity)
        return super.onKeyDown(keyCode, event);
    }
}

