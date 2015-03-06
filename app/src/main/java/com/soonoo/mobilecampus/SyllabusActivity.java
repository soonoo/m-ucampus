package com.soonoo.mobilecampus;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.PrintWriter;
import java.io.StringWriter;


public class SyllabusActivity extends ActionBarActivity{
    Document document;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_syllabus);
        //pd.setVisibility(View.VISIBLE);

        //overridePendingTransition(R.anim.slide_up, R.anim.slide_down);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        int position = intent.getIntExtra("subIndex", 1);

        new GetSyllabus().execute(User.subCode.get(position - 1));
    }

    public class GetSyllabus extends AsyncTask<String, Void, Document>{
        ProgressBar pb = (ProgressBar)findViewById(R.id.progressbar_downloading);

        @Override
        public void onPreExecute(){
            pb.setVisibility(View.VISIBLE);
        }

        @Override
        public Document doInBackground(String...str){
            String sylHtml = User.getHtml("GET", Sites.SYLLABUS_URL + Parser.getSubQuery(str[0]), "euc-kr");
            return Jsoup.parse(sylHtml);
        }
        @Override
        protected void onPostExecute(Document result) {
            document = result;
            try{
                document.select("td").attr("style", "font-size:80%;");
                document.select("td.bgtable1").attr("style", "background-color:#edf1f3; font-size:80%;");
                document.select("table:contains(출력)").remove();
                document.select("colspan").remove();
                document.select("table").select("table").attr("width", "100%");

                for (Element element : document.select("td:contains(연락처), td:contains(이동전화), td:contains(이메일)")) {
                    element.nextElementSibling().attr("style", "color:#a70500; text-decoration:underline; font-size:80%;");
                }
                WebView myWebView = (WebView)findViewById(R.id.wv);
                myWebView.loadDataWithBaseURL("", document.toString(), "text/html", "UTF-8", "");
            } catch(Exception e){
                WebView myWebView = (WebView)findViewById(R.id.wv);
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
}
