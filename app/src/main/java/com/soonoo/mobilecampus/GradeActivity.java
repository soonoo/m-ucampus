package com.soonoo.mobilecampus;

import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.PrintWriter;
import java.io.StringWriter;


public class GradeActivity extends ActionBarActivity {
    Document doc_grade;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grade);
        //overridePendingTransition(R.anim.slide_up, R.anim.slide_down);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        if (User.isConnected(GradeActivity.this)) new GetGradeHtml().execute();
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
                doc_grade.select("th:contains(학정번호), td:eq(0), td:has(img)").remove();
                doc_grade.select("table").attr("width", "100%").attr("style", "font-size:77%;");
                doc_grade.select("th:contains(학년도)").attr("bgcolor", "#a70500").attr("style", "color:#ffffff;");
                doc_grade.select("th:contains(과목명), th:contains(개설학과)," +
                        " th:contains(이수구분), th:contains(학점)," +
                        " th:contains(성적), th:contains(인증구분)").attr("bgcolor", "#bfbfbf");

                WebView webView = (WebView) findViewById(R.id.webview_grade);
                webView.loadDataWithBaseURL("", /*elements.toString() +*/ doc_grade.select("table").toString(), "text/html", "utf-8", "");
            }catch (Exception e){
                WebView webView = (WebView) findViewById(R.id.webview_grade);
                webView.loadDataWithBaseURL("", /*elements.toString() +*/ getString(R.string.message_grade_missing), "text/html", "utf-8", "");
            }
            pb.setVisibility(View.GONE);
        }
    }

    public class GetRankHtml extends AsyncTask<Void, Void, Document>{
        @Override
        public Document doInBackground(Void... p){
            String gradeHtml = User.getHtml("GET", Sites.RANK_URL, "euc-kr");

            return Jsoup.parse(gradeHtml);
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

