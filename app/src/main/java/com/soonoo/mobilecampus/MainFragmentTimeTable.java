package com.soonoo.mobilecampus;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ListView;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;

/**
 * Created by soonoo on 2015-02-22.
 */
public class MainFragmentTimeTable extends Fragment {
    ArrayList<String> subList = new ArrayList<String>();
    ArrayList<String> bgList = new ArrayList<String>();
    Document document;
    Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_main_timetable, container, false);
        String[] colorList = {"#f9f99f;", "#c9f2ea;", "#f8e6d5;", "#def9ba;", "#fcdceb;", "#dce7ff;", "#edead1;", "#dbe1eb;",
                "#ecdcff;"};

        for(String str:colorList) bgList.add(str);

        new GetTimeTable(new OnRequestCompleted() {
            @Override
            public void onRequestCompleted(Document doc) {
                document = doc;
                Elements elements = doc.select("table:has(colgroup)");
                elements.select("table").attr("width", "100%").attr("style", "font-size:75%;");
                elements.select("tr:eq(0)").remove();
                elements.select("col").first().attr("width", "5%");
                elements.select("col:gt(0)").attr("width", "15.8%");

                for (Element element : elements.select("tr:gt(1) > td:gt(0)")) {
                    String sub = element.text();
                    if (sub != "") {
                        if (!subList.contains(sub)) {
                            subList.add(sub);
                            element.attr("style", "background-color: " + bgList.get(subList.indexOf(sub)));
                        } else {
                            element.attr("style", "background-color: " + bgList.get(subList.indexOf(sub)));
                        }
                    }
                }

                for (Element element : elements.select("table")) {
                    element.attr("border", "1");
                    element.attr("bordercolor", "gray");
                    //element.attr("style", "border-right:1px solid; border-bottom:1px solid;");
                }
                WebView myWebView = (WebView) view.findViewById(R.id.webview_timetable);
                myWebView.loadDataWithBaseURL("", elements.toString(), "text/html", "utf-8", "");
            }
        }).execute();

        return view;
    }

    public class GetTimeTable extends AsyncTask<Void, Void, Document> {
        OnRequestCompleted delegate;

        GetTimeTable(OnRequestCompleted caller){
            delegate = caller;
        }

        @Override
        public  Document doInBackground(Void... p){
            String query = "this_year=" + User.subCode.get(0).substring(0, 4) + "&hakgi=" + User.subCode.get(0).substring(4, 5);
            String timeTableHtml = User.getHtml("POST", Sites.TIMETABLE_URL, query, "euc-kr");
            return Jsoup.parse(timeTableHtml);
        }

        @Override
        public void onPostExecute(Document doc){
            delegate.onRequestCompleted(doc);
        }
    }

}
