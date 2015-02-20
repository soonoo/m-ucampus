package com.soonoo.mobilecampus;

import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.PrintWriter;
import java.io.StringWriter;


public class GradeActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grade);

        try{
            Document doc_grade = new GetGradeHtml().execute().get();

            doc_grade.select("table:contains(지도교수), table:contains(학위과정)").remove();
            doc_grade.select("th:contains(학정번호), td:eq(0), td:has(img)").remove();

            doc_grade.select("table").attr("width", "100%").attr("style", "font-size:77%;");;
            doc_grade.select("th:contains(학년도)").attr("bgcolor", "#a70500").attr("style", "color:#ffffff;");
            doc_grade.select("th:contains(과목명), th:contains(개설학과)," +
                    " th:contains(이수구분), th:contains(학점)," +
                    " th:contains(성적), th:contains(인증구분)").attr("bgcolor", "#bfbfbf");


            Document doc_rank = new GetRankHtml().execute().get();

            doc_rank.select("table").attr("bgcolor", "#bfbfbf").attr("width", "100%").attr("style", "font-size:80%;");
            Elements elements = doc_rank.select("p table");
            WebView webView = (WebView) findViewById(R.id.webview_grade);
            webView.loadDataWithBaseURL("", elements.toString() + doc_grade.select("table").toString(), "text/html", "euc-kr", "");
        } catch(Exception e){
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            String exceptionAsStrting = sw.toString();
            Log.e("INFO", exceptionAsStrting);
            e.printStackTrace();
        }
    }

    public class GetGradeHtml extends AsyncTask<Void, Void, Document>{
        @Override
        public Document doInBackground(Void... p){
            String gradeHtml = User.getHtml("GET", Sites.GRADE_URL, "euc-kr");

            return Jsoup.parse(gradeHtml);
        }
    }

    public class GetRankHtml extends AsyncTask<Void, Void, Document>{
        @Override
        public Document doInBackground(Void... p){
            String gradeHtml = User.getHtml("GET", Sites.RANK_URL, "euc-kr");

            return Jsoup.parse(gradeHtml);
        }
    }
}

