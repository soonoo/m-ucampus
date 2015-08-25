package com.soonoo.mobilecampus.timetable;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ProgressBar;

import com.googlecode.htmlcompressor.compressor.HtmlCompressor;
import com.soonoo.mobilecampus.LoginView;
import com.soonoo.mobilecampus.R;
import com.soonoo.mobilecampus.Sites;
import com.soonoo.mobilecampus.util.User;
import com.urqa.clientinterface.URQAController;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
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
        //String[] colorList = {"#FFCDD2;", "#F8BBD0;", "#E1BEE7;", "#D1C4E9;", "#C5CAE9;", "#BBDEFB;", "#80CBC4;", "#F0F4C3;",
            //    "#FFF59D;"};
        days.add(day1);
        days.add(day2);
        days.add(day3);
        days.add(day4);
        days.add(day5);
        days.add(day6);

        for (String str : colorList) bgList.add(str);

        //try {
            new GetTimeTable().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        //} catch (Exception e){
            /*Intent intent = new Intent(getActivity(), LoginView.class);

            SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getActivity());
            pref.edit().putBoolean("get_session", true).apply();
            getActivity().finish();
            startActivity(intent);*/
        //}
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
            //String query = "this_year=" + User.subCode.get(0).substring(0, 4) + "&hakgi=" + User.subCode.get(0).substring(4, 5);
            String timeTableHtml = User.getHtml("GET", Sites.TIMETABLE_URL, "euc-kr");
            return Jsoup.parse(timeTableHtml);
        }

        @Override
        public void onPostExecute(Document doc) {
            document = doc;
            Elements elements = doc.select("table:has(colgroup)");
            Element html = Jsoup.parse(Sites.TIMETABLE_TEMPLATE);
            String width = "15.83333%;";
            String color;
            Element ele = Jsoup.parse(Sites.TIMETABLE_TEMPLATE, "", Parser.xmlParser());

            // 요일별 과목명 가져오기
            int idx1 = 0, idx2 = 0;
            for (Element tr : elements.select("tr:gt(1)")) {
                idx2 = 0;
                for (Element td : tr.select("td:gt(0)")) {
                    String text = td.text();
                    if (!text.equals("")) {
                        /*if(text.contains("데이터구조실습")) {
                            days.get(idx2).add("");
                            continue;
                        }*/
                        days.get(idx2).add(text);
                    } else {
                        days.get(idx2).add("");
                    }
                    idx2++;
                }
                idx1++;
            }

            // 토요일 수업 없으면 칼럼 삭제
            if (isListEmpty(day6)) {
                ele.select("#sat").remove();
                days.remove(5);
                width = "19%;";
                ele.select("span:gt(0)").attr("style", "width:19%;");
            }

            /*if(isPeriodEmpty(days)){
                ele.select("div:eq(0)").remove();
            }
*/
            HtmlCompressor compressor = new HtmlCompressor();
            compressor.setPreserveLineBreaks(false);        //preserves original line breaks
            compressor.setRemoveSurroundingSpaces("div,span,html,body,head,title,style");

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
                        try {
                            if (list.get(idx2).equals(list.get(idx2 - 1))) subs = "";
                        } catch (Exception e) {

                        }
                    }

                    if(!subs.equals("")){
                        subs = subs.replaceAll("\\(", "<br>\\(");
                        subs = subs.replaceAll("\\(", "<h5 style=\"padding:3 1 0 1; color:#333333l;\">");
                        subs = subs.replaceAll("\\)", "</h5>");
                    }
                    String div = "background-color:" + color ;
                   // if(!(isPeriodEmpty(days) && idx2==0)){
                    ele.select("span:eq(" + Integer.toString(idx1 + 1) + ") div:eq(" + Integer.toString(idx2 + 1) + ")").first().append("<h4 style=\"padding:3 1 0 1; color:#212121; \">"+subs+"</h4>").
                                attr("style", div);
                  //  }
                    idx2++;
                }
                idx1++;
            }

            WebView myWebView = (WebView) view.findViewById(R.id.webview_timetable);
            myWebView.loadDataWithBaseURL("", compressor.compress(ele.toString()), "text/html", "utf-8", "");
            pb.setVisibility(View.GONE);
        }
    }

    private boolean isListEmpty(ArrayList<String> list) {
        for (String string : list) {
            if (!string.equals("")) return false;
        }
        return true;
    }

    private boolean isPeriodEmpty(ArrayList<ArrayList<String>> list){
        for(ArrayList<String> day: list){
            if(!day.get(0).equals("")) return false;
        }
        return true;
    }

}
