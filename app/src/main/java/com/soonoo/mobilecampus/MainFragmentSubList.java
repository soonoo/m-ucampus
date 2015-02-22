package com.soonoo.mobilecampus;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;

/**
 * Created by soonoo on 2015-02-22.
 */
public class MainFragmentSubList extends Fragment {
    Context context;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.context = container.getContext();

        View view = inflater.inflate(R.layout.fragment_main_list, container, false);
        ListView mainList = (ListView) view.findViewById(R.id.fragment_list);

        try {
            ArrayList<String> infoList = new InitMainList().execute().get();
            ArrayList<String> titleList = (ArrayList<String>)User.subName.clone();
            int size = infoList.size();

            titleList.add(0, "과목별 조회");
            titleList.add("성적/장학 조회");
            titleList.add("성적/석차 조회");
            titleList.add("장학 조회");
            titleList.add("시설 이용");
            titleList.add("중앙도서관 좌석 정보");

            infoList.add(0, "");
            infoList.add("");
            infoList.add("학기별 성적/석차를 확인합니다.");
            infoList.add("장학금 수혜 현황을 확인합니다.");
            infoList.add("");
            infoList.add("열람실별 좌석 현황을 확인합니다.");

            MainListAdapter adapter = new MainListAdapter(titleList, infoList, size + 1);
            mainList.setAdapter(adapter);
        }catch (Exception e){
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            String exceptionAsStrting = sw.toString();
            Log.e("INFO", exceptionAsStrting);
            e.printStackTrace();
        }


        return view;
    }

    public class InitMainList extends AsyncTask<Void, Void, ArrayList<String>>{
        @Override
        public ArrayList<String> doInBackground(Void... p){
            ArrayList<String> infoList = new ArrayList<String>();

            SharedPreferences prefs = PreferenceManager
                    .getDefaultSharedPreferences(context);
            SharedPreferences.Editor editor = prefs.edit();

            String mainHtml = User.getHtml("POST", Sites.MAIN_URL, Sites.MAIN_QUERY, "utf-8");
            Document mainDoc = Jsoup.parse(mainHtml);
            User.subCode = Parser.getSubCode(mainDoc);
            User.subName = Parser.getSubName(mainDoc);

            for(String sub: User.subCode){
                String info;
                if((info = prefs.getString(sub, null)) != null) {
                    infoList.add(info);
                    continue;
                }else {
                    String sylHtml = User.getHtml("GET", Sites.SYLLABUS_URL + Parser.getSubQuery(sub), "euc-kr");
                    Document sylDoc = Jsoup.parse(sylHtml);

                    String prof = Parser.getProf(sylDoc);
                    String time = Parser.getTime(sylDoc);

                    info = prof + " | " + time;
                    infoList.add(info);
                    editor.putString(sub, info);
                    editor.commit();
                }
            }
            return infoList;
        }
    }
}
