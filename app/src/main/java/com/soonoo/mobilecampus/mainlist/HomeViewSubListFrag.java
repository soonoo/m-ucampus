package com.soonoo.mobilecampus.mainlist;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.soonoo.mobilecampus.LoginView;
import com.soonoo.mobilecampus.R;
import com.soonoo.mobilecampus.Sites;
import com.soonoo.mobilecampus.util.Parser;
import com.soonoo.mobilecampus.util.User;
import com.urqa.clientinterface.URQAController;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.ArrayList;

/**
 * Created by soonoo on 2015-02-22.
 */
public class HomeViewSubListFrag extends Fragment {
    Context context;
    ArrayList<String> infoList;
    ArrayList<String> titleList;
    HomeViewAdapter adapter;
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.context = container.getContext();

        view = inflater.inflate(R.layout.fragment_main_list, container, false);
        new InitMainList().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

        return view;
    }

    public class InitMainList extends AsyncTask<Void, Void, ArrayList<String>> {
        private ProgressDialog nDialog;

        @Override
        public void onPreExecute() {
            nDialog = new ProgressDialog(getActivity()); //Here I get an error: The constructor ProgressDialog(PFragment) is undefined
            nDialog.setMessage("로그인중...");
            nDialog.setIndeterminate(false);
            nDialog.setCancelable(false);
            nDialog.show();
        }

        @Override
        public ArrayList<String> doInBackground(Void... p){
            ArrayList<String> infoList = new ArrayList<>();

            SharedPreferences prefs = PreferenceManager
                    .getDefaultSharedPreferences(context);
            SharedPreferences.Editor editor = prefs.edit();

            String mainHtml;
            try{
                mainHtml = User.getHtml("POST", Sites.MAIN_URL, Sites.MAIN_QUERY, "utf-8");
            }catch(Exception e){
                mainHtml = "";
            }

            Document mainDoc = Jsoup.parse(mainHtml);

            Parser.setSubCode(mainDoc);
            Parser.setSubName(mainDoc);
            Parser.setNewNotice(mainDoc);

            if(User.subCode == null || User.subCode.isEmpty()){
                getActivity().finish();
                prefs.edit().putBoolean("get_session", true);
                startActivity(new Intent(getActivity(), LoginView.class));
            }

            for (String sub : User.subCode) {
                String info;
                if ((info = prefs.getString(sub, null)) != null) {
                    infoList.add(info);
                    continue;
                } else {
                    String sylHtml = null;

                    try{
                        sylHtml = User.getHtml("POST", Sites.SYLLABUS_URL + Parser.getSubQuery(sub), "euc-kr");
                    }catch (Exception e){
                        getActivity().finish();
                        prefs.edit().putBoolean("get_session", true);
                        startActivity(new Intent(getActivity(), LoginView.class));
                    }

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

        @Override
        public void onPostExecute(ArrayList<String> list) {
            titleList = (ArrayList<String>) User.subName.clone();
            infoList = list;
            int size = titleList.size();

            titleList.add(0, "과목별 조회");
            titleList.add("성적/장학 조회");
            titleList.add("성적/석차 조회");
            titleList.add("장학 조회");
            titleList.add("시설 이용");
            titleList.add("중앙도서관");
            titleList.add("강의계획서 검색");

            infoList.add(0, "");
            infoList.add("");
            infoList.add("학기별 성적/석차를 확인합니다.");
            infoList.add("장학금 수혜 현황을 확인합니다.");
            infoList.add("");
            User.isNew.add(false);
            infoList.add("열람실 좌석 현황/도서 검색");
            infoList.add("강의계획서를 검색합니다.");

            adapter = new HomeViewAdapter(titleList, infoList, User.isNew, size + 1);

            ListView mainList = (ListView) view.findViewById(R.id.fragment_list);
            mainList.setAdapter(adapter);
            nDialog.dismiss();
        }
    }
}
