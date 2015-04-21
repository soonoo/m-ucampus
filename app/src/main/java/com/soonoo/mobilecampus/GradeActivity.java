package com.soonoo.mobilecampus;

import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.PrintWriter;
import java.io.StringWriter;


public class GradeActivity extends ActionBarActivity {
    Document doc_grade;
    WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grade);
        //overridePendingTransition(R.anim.slide_up, R.anim.slide_down);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Tracker t = ((Controller)getApplication()).getTracker(Controller.TrackerName.APP_TRACKER);
        t.setScreenName("GradeActivity");
        t.send(new HitBuilders.AppViewBuilder().build());

        if (User.isConnected(GradeActivity.this)) {
            new GetGradeHtml().execute();
        }
        else Toast.makeText(getApplicationContext(), "nono", Toast.LENGTH_SHORT).show();
        //Document doc_rank = new GetRankHtml().execute().get();

        //doc_rank.select("table").attr("bgcolor", "#bfbfbf").attr("width", "100%").attr("style", "font-size:80%;");
        //Elements elements = doc_rank.select("p table");

    }


    public class GetGradeHtml extends AsyncTask<Void, Void, Document>{
        ProgressBar pb = (ProgressBar)findViewById(R.id.progressbar_downloading);

        @Override
        public void onPreExecute(){
            pb.setVisibility(View.VISIBLE);
        }

        @Override
        public Document doInBackground(Void... p){
            String gradeHtml = User.getHtml("GET", Sites.GRADE_URL, "euc-kr");

            return Jsoup.parse(gradeHtml);
        }

        @Override
        public void onPostExecute(Document doc){
            doc_grade = doc;

            try {
                doc_grade.select("table:contains(지도교수), table:contains(학위과정)").remove();
                doc_grade.select("th:contains(학정번호), td:has(img)").remove();
                doc_grade.select("td:contains(-)").remove();
                doc_grade.select("table").attr("width", "100%").attr("style", "font-size:70%;");
                doc_grade.select("th:contains(학년도)").attr("bgcolor", "#a70500").attr("style", "color:#ffffff;");
                doc_grade.select("th:contains(과목명), th:contains(개설학과)," +
                        " th:contains(이수구분), th:contains(학점)," +
                        " th:contains(성적), th:contains(인증구분)").attr("bgcolor", "#bfbfbf");
                //doc_grade.select("table:contains(학과별)").select("input").attr("href", "http://info.kw.ac.kr/webnote/sungjuk/sungjuk_info.html");

                Element link = doc_grade.select("table:contains(학과별)").select("input").first();

                Element aTag = doc_grade.createElement("a");
                aTag.attr("href", "http://info.kw.ac.kr/webnote/sungjuk/sungjuk_info.html");

                link.replaceWith(aTag);
                aTag.appendChild(link);


                webView = (WebView) findViewById(R.id.webview_grade);
                webView.loadDataWithBaseURL("", /*elements.toString() +*/ doc_grade.select("table").toString(), "text/html", "utf-8", "");
                webView.setWebViewClient(new WebViewClient() {
                    @Override
                    public boolean shouldOverrideUrlLoading(WebView view, String url) {
                        return false;
                    }
                });
            }catch (Exception e){
                webView = (WebView) findViewById(R.id.webview_grade);
                webView.loadDataWithBaseURL("", /*elements.toString() +*/ getString(R.string.message_grade_missing), "text/html", "utf-8", "");
            }
            pb.setVisibility(View.GONE);
        }
    }



    public class GetRankHtml extends AsyncTask<Void, Void, Document>{
        @Override
        public Document doInBackground(Void... p){
            String gradeHtml = User.getHtml("GET", Sites.RANK_URL, "euc-kr");
//table contains(년도)
            return Jsoup.parse(gradeHtml);
        }

        @Override
        public void onPostExecute(Document document){

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
    public void onRestart(){
        super.onRestart();
        new LoginActivity.OnBack().execute();
    }

    @Override
    protected void onStart(){
        super.onStart();
        GoogleAnalytics.getInstance(this).reportActivityStart(this);
    }

    @Override
    protected void onStop(){
        super.onStop();
        GoogleAnalytics.getInstance(this).reportActivityStop(this);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // Check if the key event was the Back button and if there's history
        if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.getUrl().contains("info")) {
            new GetGradeHtml().execute();
           // webView
            return true;
        }
        // If it wasn't the Back key or there's no web page history, bubble up to the default
        // system behavior (probably exit the activity)
        return super.onKeyDown(keyCode, event);
    }
}

