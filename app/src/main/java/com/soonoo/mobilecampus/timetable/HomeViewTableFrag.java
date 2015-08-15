package com.soonoo.mobilecampus.timetable;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
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

    ArrayList<ArrayList<String>> days = new ArrayList<>();
    ArrayList<String> day1 = new ArrayList<>();
    ArrayList<String> day2 = new ArrayList<>();
    ArrayList<String> day3 = new ArrayList<>();
    ArrayList<String> day4 = new ArrayList<>();
    ArrayList<String> day5 = new ArrayList<>();
    ArrayList<String> day6 = new ArrayList<>();

    Document document;
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_main_timetable, container, false);
        String[] colorList = {"#f9f99f;", "#c9f2ea;", "#f8e6d5;", "#dbe1eb;", "#fcdceb;", "#dce7ff;", "#edead1;", "#def9ba;",
                "#ecdcff;"};
        days.add(day1);
        days.add(day2);
        days.add(day3);
        days.add(day4);
        days.add(day5);
        days.add(day6);

        for (String str : colorList) bgList.add(str);

        new GetTimeTable().execute();

        return view;
    }

    public class GetTimeTable extends AsyncTask<Void, Void, Document> {
        ProgressBar pb;

        public void onPreExecute() {
            pb = (ProgressBar) view.findViewById(R.id.pb_table);
            pb.setVisibility(View.VISIBLE);
        }


        @Override
        public Document doInBackground(Void... p) {
            String query = "this_year=" + User.subCode.get(0).substring(0, 4) + "&hakgi=" + User.subCode.get(0).substring(4, 5);
            String timeTableHtml = User.getHtml("POST", Sites.TIMETABLE_URL, query, "euc-kr");
            return Jsoup.parse(timeTableHtml);
        }

        @Override
        public void onPostExecute(Document doc) {
            document = doc;
            Elements elements = doc.select("table:has(colgroup)");
            Element html = Jsoup.parse(Sites.TIMETABLE_TEMPLATE);
            String width = "calc(95%/6);";
            String color;

            int idx1 = 0, idx2 = 0;
            for (Element tr : elements.select("tr:gt(1)")) {
                idx2 = 0;
                for (Element td : tr.select("td:gt(0)")) {
                    String text = td.text();
                    if (!text.equals("")) {
                        days.get(idx2).add(text);
                    } else {
                        days.get(idx2).add("");
                    }
                    idx2++;
                }
                idx1++;
            }


            if (isListEmpty(day6)) {
                html.select(".col7").remove();
                days.remove(5);
                width = "calc(95%/5);";
            }

            idx1 = 0;
            for (ArrayList<String> list : days) {
                idx2 = 0;
                for (String subs : list) {
                    if (!subs.equals("") && !subList.contains(subs)) {
                        subList.add(subs);
                    }
                    if (subs.equals("")) {
                        color = "";
                    } else {
                        color = bgList.get(subList.indexOf(subs));
                        try{
                            if(list.get(idx2).equals(list.get(idx2-1))) subs="";
                        }catch(Exception e){

                        }
                    }

                    String div = "<div style=\"top:" + Integer.toString(15 + 71 * idx2) + "px; width:" + width +
                            " background-color:" + color + ";\" >" + subs + "</div>";
                   // System.out.println(div);
                    html.select(".col" + Integer.toString(idx1 + 2) + " > div.subject").first().append(div);
                    idx2++;
                }
                idx1++;
            }

            WebView myWebView = (WebView) view.findViewById(R.id.webview_timetable);
            myWebView.loadDataWithBaseURL("", html.toString(), "text/html", "utf-8", "");
            //System.out.println(html);
            //myWebView.loadDataWithBaseURL("", elements.toString(), "text/html", "utf-8", "");
            pb.setVisibility(View.GONE);
        }
    }

    private boolean isListEmpty(ArrayList<String> list) {
        for (String string : list) {
            if (!string.equals("")) return false;
        }
        return true;
    }

}
