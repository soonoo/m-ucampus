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
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.PrintWriter;
import java.io.StringWriter;


public class SyllabusActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_syllabus);

        try{
            Intent intent = getIntent();
            int position = intent.getIntExtra("subIndex", 1);
            Document doc = new GetSyllabus().execute(User.subCode.get(position - 1)).get();

            doc.select("td").attr("style", "font-size:80%;");
            //EDF1F3
            doc.select("td.bgtable1").attr("style", "background-color:#edf1f3; font-size:80%;");
            doc.select("table:contains(출력)").remove();
            doc.select("colspan").remove();
            doc.select("table").select("table").attr("width", "100%");

            for(Element element: doc.select("td:contains(연락처), td:contains(이동전화), td:contains(이메일)")){
                element.nextElementSibling().attr("style", "color:#a70500; text-decoration:underline; font-size:80%;");
            }

            WebView myWebView = (WebView)findViewById(R.id.wv);
            myWebView.loadDataWithBaseURL("", doc.toString(), "text/html", "UTF-8", "");

        }catch(Exception e){
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            String exceptionAsStrting = sw.toString();
            Log.e("INFO", exceptionAsStrting);
            e.printStackTrace();
        }

    }

    public class GetSyllabus extends AsyncTask<String, Void, Document>{
        @Override
        public Document doInBackground(String...str){
            String sylHtml = User.getHtml("GET", Sites.SYLLABUS_URL + Parser.getSubQuery(str[0]), "euc-kr");
            Document doc = Jsoup.parse(sylHtml);
            return doc;
        }
    }
}
