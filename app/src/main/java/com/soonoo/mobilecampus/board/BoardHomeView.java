package com.soonoo.mobilecampus.board;

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
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.soonoo.mobilecampus.LoginView;
import com.soonoo.mobilecampus.R;
import com.soonoo.mobilecampus.Sites;
import com.soonoo.mobilecampus.util.User;
import com.urqa.clientinterface.URQAController;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class BoardHomeView extends Fragment {
    Context context;
    View view;
    ArrayList<String> titleList = new ArrayList<>();
    ArrayList<String> infoList = new ArrayList<>();
    ArrayList<Integer> idList = new ArrayList<>();
    BoardHomeViewAdpater adapter;
    int page = 1;
    boolean lock = true;
    boolean isEnd = false;
    ListView listView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.context = container.getContext();
        view = inflater.inflate(R.layout.activity_board_home_view, container, false);

        //try{
            new GetJSON().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        /*}catch(Exception e){
            Intent intent = new Intent(getActivity(), LoginView.class);

            SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getActivity());
            pref.edit().putBoolean("get_session", true).apply();
            getActivity().finish();
            startActivity(intent);
        }*/

        return view;
    }

    public class GetJSON extends AsyncTask<Void, Void, String> {
        View footer = getActivity().getLayoutInflater().inflate(R.layout.board_list_footer, null, false);

        public void onPreExecute() {

        }

        public String doInBackground(Void... p) {
            return User.getHtml("GET", Sites.BOARD_URL + "/read/list?num=25&page=" + Integer.toString(page), "UTF-8");
        }

        public void onPostExecute(String json) {
            try {
                JSONArray data = new JSONArray(json);
                int replyCount;

                for (int i = 0; i < data.length(); i++) {
                    JSONObject jsonObject = data.getJSONObject(i);
                    replyCount = jsonObject.getInt("reply_count");

                    if (replyCount > 0)
                        titleList.add(jsonObject.getString("title") + " <font color=#b30027>[" + Integer.toString(replyCount) + "]</font>");
                    else titleList.add(jsonObject.getString("title"));

                    infoList.add(jsonObject.getString("date") + "  |  조회:" + jsonObject.getString("view_count") + "  |  " + jsonObject.getString("ip") + ".*.*");
                    idList.add(jsonObject.getInt("id"));
                }

                listView = (ListView) view.findViewById(R.id.board_home_list);
                adapter = new BoardHomeViewAdpater(titleList, infoList, idList);

                listView.setOnScrollListener(new AbsListView.OnScrollListener() {
                    @Override
                    public void onScrollStateChanged(AbsListView absListView, int i) {
                    }

                    @Override
                    public void onScroll(AbsListView absListView, int i, int i1, int i2) {
                        if (isEnd) {
                            footer.findViewById(R.id.footer_pb).setVisibility(View.GONE);
                            return;
                        }
                        if (i2 == i + i1) {
                           // lock = !lock;
                            if (lock)
                                new GetNextPage().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                        }
                    }
                });

                listView.addFooterView(footer);
                listView.setAdapter(adapter);

            } catch (Exception e) {
                e.printStackTrace();
            }

           // view.findViewById(R.id.footer_pb).setVisibility(View.GONE);
        }
    }

    public class GetNextPage extends AsyncTask<Void, Void, String> {
        View footer = getActivity().getLayoutInflater().inflate(R.layout.board_list_footer, null, false);
        TextView pb = (TextView) footer.findViewById(R.id.footer_pb);

        public void onPreExecute() {
            lock = !lock;
            pb.setVisibility(View.VISIBLE);
        }


        public String doInBackground(Void... p) {
            return User.getHtml("GET", Sites.BOARD_URL + "/read/list?num=25&page=" + Integer.toString(++page), "UTF-8");
        }

        public void onPostExecute(String json) {
            try {
                JSONArray data = new JSONArray(json);
                int replyCount;

                if (data.length() == 0) {
                    isEnd = true;
                    pb.setVisibility(View.GONE);
                    lock = !lock;
                    listView.removeFooterView(footer);
                    adapter.notifyDataSetChanged();
                    return;
                }

                for (int i = 0; i < data.length(); i++) {
                    JSONObject jsonObject = data.getJSONObject(i);
                    replyCount = jsonObject.getInt("reply_count");

                    if (replyCount > 0)
                        titleList.add(jsonObject.getString("title") + " <font color=#b30027>[" + Integer.toString(replyCount) + "]</font>");
                    else titleList.add(jsonObject.getString("title")+ " ");

                    infoList.add(jsonObject.getString("date") + "  |  조회:" + jsonObject.getString("view_count") + "  |  " + jsonObject.getString("ip") + ".*.*");
                    idList.add(jsonObject.getInt("id"));
                }

                adapter.notifyDataSetChanged();
                lock = !lock;
            } catch (Exception e) {
                e.printStackTrace();
            }
            pb.setVisibility(View.GONE);
            if (PreferenceManager.getDefaultSharedPreferences(getActivity()).getBoolean("new_article_created", false)) {
                PreferenceManager.getDefaultSharedPreferences(getActivity()).edit().putBoolean("new_article_created", false).apply();
            }
            if (PreferenceManager.getDefaultSharedPreferences(getActivity()).getBoolean("new_reply_created", false)) {
                PreferenceManager.getDefaultSharedPreferences(getActivity()).edit().putBoolean("new_reply_created", false).apply();
            }
        }
    }

    public void onStart() {
        super.onStart();

        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getActivity());

        if (pref.getBoolean("new_article_created", false)) {
            pref.edit().putBoolean("new_article_created", false).apply();
            page = 1;
            lock = true;
            isEnd = false;
            titleList = new ArrayList<>();
            infoList = new ArrayList<>();
            idList = new ArrayList<>();
            new GetJSON().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } else if (pref.getBoolean("new_reply_created", false)) {
            try {
                titleList.set(pref.getInt("titlePos", 0), titleList.get(pref.getInt("titlePos", 0)).replaceAll("<font color=#b30027>\\[.+\\]<\\/font>", ""));
                titleList.set(pref.getInt("titlePos", 0), titleList.get(pref.getInt("titlePos", 0)) + "<font color=#b30027>[" + pref.getInt("replyNum", 0) + "]</font>");
                pref.edit().putBoolean("new_reply.created", false).apply();
                adapter.notifyDataSetChanged();
            } catch (Exception e){

            }
        }
    }
}
