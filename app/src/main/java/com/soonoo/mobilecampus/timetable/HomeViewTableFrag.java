package com.soonoo.mobilecampus.timetable;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ProgressBar;

import com.soonoo.mobilecampus.R;
import com.soonoo.mobilecampus.Sites;
import com.soonoo.mobilecampus.util.User;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;

/**
 * Created by soonoo on 2015-02-22.
 */
public class HomeViewTableFrag extends Fragment {
    ArrayList<String> subList = new ArrayList<String>();
    ArrayList<String> bgList = new ArrayList<String>();
    Document document;
    View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_main_timetable, container, false);
        String[] colorList = {"#f9f99f;", "#c9f2ea;", "#f8e6d5;", "#def9ba;", "#fcdceb;", "#dce7ff;", "#edead1;", "#dbe1eb;",
                "#ecdcff;"};

        for(String str:colorList) bgList.add(str);

        new GetTimeTable().execute();

        return view;
    }

    public class GetTimeTable extends AsyncTask<Void, Void, Document> {
        ProgressBar pb;
        public void onPreExecute(){
            pb = (ProgressBar)view.findViewById(R.id.pb_table);
            pb.setVisibility(View.VISIBLE);
        }


        @Override
        public  Document doInBackground(Void... p){
            String query = "this_year=" + User.subCode.get(0).substring(0, 4) + "&hakgi=" + User.subCode.get(0).substring(4, 5);
            String timeTableHtml = User.getHtml("POST", Sites.TIMETABLE_URL, query, "euc-kr");
            return Jsoup.parse(timeTableHtml);
        }

        @Override
        public void onPostExecute(Document doc){
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
            pb.setVisibility(View.GONE);
        }
    }

}