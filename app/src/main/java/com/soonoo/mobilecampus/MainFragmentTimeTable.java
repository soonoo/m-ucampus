package com.soonoo.mobilecampus;

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_timetable, container, false);

        bgList.add("#f9f99f;");
        bgList.add("#c9f2ea;");
        bgList.add("#f8e6d5;");
        bgList.add("#def9ba;");
        bgList.add("#fcdceb;");
        bgList.add("#dce7ff;");
        bgList.add("#edead1;");
        bgList.add("#dbe1eb;");
        bgList.add("#ecdcff;");

        /*bgList.add("#d9896c;");
        bgList.add("#ffd9bf;");
        bgList.add("#72e5be;");
        bgList.add("#9cb866;");
        bgList.add("#40d9ff;");
        bgList.add("#fab11c;");
        bgList.add("#ea8f9b;");*/
        try {
            Document doc = new GetTimeTable().execute().get();
            Elements elements = doc.select("table:has(colgroup)");
            elements.select("table").attr("width", "100%").attr("style", "font-size:75%;");
            elements.select("tr:eq(0)").remove();

            elements.select("col").first().attr("width", "5%");
            elements.select("col:gt(0)").attr("width", "15.8%");

            for(Element element: elements.select("tr:gt(1) > td:gt(0)")){
                String sub = element.text();
                if(sub != ""){
                    if(!subList.contains(sub)) {
                        subList.add(sub);
                        element.attr("style", "background-color: " + bgList.get(subList.indexOf(sub)));
                    }else{
                        element.attr("style", "background-color: " + bgList.get(subList.indexOf(sub)));
                    }
                }
            }

            for(Element element: elements.select("table")){
                element.attr("border", "1");
                element.attr("bordercolor", "gray");
                //element.attr("style", "border-right:1px solid; border-bottom:1px solid;");
            }

            WebView myWebView = (WebView) view.findViewById(R.id.webview_timetable);
            myWebView.loadDataWithBaseURL("", elements.toString(), "text/html", "euc-kr", "");
        }catch (Exception e){
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            String exceptionAsStrting = sw.toString();
            Log.e("INFO", exceptionAsStrting);
            e.printStackTrace();
        }
        return view;
    }

    public class GetTimeTable extends AsyncTask<Void, Void, Document> {
        @Override
        public  Document doInBackground(Void... p){
            String query = "this_year=" + User.subCode.get(0).substring(0, 4) + "&hakgi=" + User.subCode.get(0).substring(4, 5);
            String timeTableHtml = User.getHtml("POST", Sites.TIMETABLE_URL, query, "euc-kr");
            return Jsoup.parse(timeTableHtml);
        }
    }

    public boolean isPeriodEmpty(Elements elements){
        for(Element element: elements.select("td:gt(0)")){
            if(element.text().equals("")) return false;
        }
        return true;
    }

}
