package com.soonoo.mobilecampus.board;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;

import com.soonoo.mobilecampus.R;
import com.soonoo.mobilecampus.Sites;
import com.soonoo.mobilecampus.util.User;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class BoardHomeView extends Fragment {
    Context context;
    View view;
    ArrayList<String> titleList;
    ArrayList<String> infoList;
    ArrayList<Integer> idList;
    ArrayList<Integer> countList;
    ArrayList<Integer> viewList;
    BoardHomeViewAdpater adapter;
    int page = 1;
    boolean refreshed = false;
    boolean lock = true;
    boolean isEnd = false;
    ListView listView;
    SwipeRefreshLayout swipe;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.context = container.getContext();
        view = inflater.inflate(R.layout.activity_board_home_view, container, false);

        swipe = (SwipeRefreshLayout) view.findViewById(R.id.swipe);
        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                lock = true;
                isEnd = false;
                page = 1;
                refreshed = true;
                new GetJSON().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            }
        });

        new GetJSON().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

        return view;
    }

    public class GetJSON extends AsyncTask<Void, Void, String> {
        //View footer = getActivity().getLayoutInflater().inflate(R.layout.board_list_footer, null, false);

        public void onPreExecute() {
            titleList = new ArrayList<>();
            infoList = new ArrayList<>();
            idList = new ArrayList<>();
            countList = new ArrayList<>();
            viewList = new ArrayList<>();
        }

        public String doInBackground(Void... p) {
            try {
                return User.getHtml("GET", Sites.BOARD_URL + "/read/list?num=25&page=" + Integer.toString(page), "UTF-8");
            } catch (Exception e) {
                return "";
            }
        }

        public void onPostExecute(String json) {
            try {
                JSONArray data = new JSONArray(json);
                int replyCount;

                for (int i = 0; i < data.length(); i++) {
                    JSONObject jsonObject = data.getJSONObject(i);
                    replyCount = jsonObject.getInt("reply_count");
                    titleList.add(jsonObject.getString("title"));
                    viewList.add(jsonObject.getInt("view_count"));

                    countList.add(replyCount);

                    if (jsonObject.get("ip").equals("관리자") || jsonObject.get("ip").equals("admin"))
                        infoList.add(jsonObject.getString("date") + "  |  " + jsonObject.getString("ip"));
                    else
                        infoList.add(jsonObject.getString("date") + "  |  " + jsonObject.getString("ip") + ".*.*");

                    idList.add(jsonObject.getInt("id"));
                }

                listView = (ListView) view.findViewById(R.id.board_home_list);
                adapter = new BoardHomeViewAdpater(titleList, infoList, idList, countList, viewList);

                listView.setOnScrollListener(new AbsListView.OnScrollListener() {
                    @Override
                    public void onScrollStateChanged(AbsListView absListView, int i) {
                    }

                    @Override
                    public void onScroll(AbsListView absListView, int i, int i1, int i2) {
                        if (isEnd) {
                            return;
                        }
                        if (i2 == i + i1 + 2) {
                            // lock = !lock;
                            if (lock)
                                new GetNextPage().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                        }
                    }
                });
                listView.setAdapter(adapter);

            } catch (Exception e) {
                e.printStackTrace();
            }
            swipe.setRefreshing(false);
        }
    }

    public class GetNextPage extends AsyncTask<Void, Void, String> {
        public void onPreExecute() {
            lock = !lock;
        }


        public String doInBackground(Void... p) {
            try {
                return User.getHtml("GET", Sites.BOARD_URL + "/read/list?num=25&page=" + Integer.toString(++page), "UTF-8");
            } catch (Exception e) {
                return "";
            }
        }

        public void onPostExecute(String json) {
            try {
                JSONArray data = new JSONArray(json);
                int replyCount;

                if (json.equals("[]")) {
                    isEnd = true;
                    lock = !lock;
                    return;
                }

                for (int i = 0; i < data.length(); i++) {
                    JSONObject jsonObject = data.getJSONObject(i);
                    replyCount = jsonObject.getInt("reply_count");
                    titleList.add(jsonObject.getString("title") + " ");
                    viewList.add(jsonObject.getInt("view_count"));

                    countList.add(replyCount);

                    infoList.add(jsonObject.getString("date") + "  |  " + jsonObject.getString("ip") + ".*.*");
                    idList.add(jsonObject.getInt("id"));
                }

                adapter.notifyDataSetChanged();
                lock = !lock;
            } catch (Exception e) {
                e.printStackTrace();
            }

            /*//footer.setVisibility(View.GONE);
            if (PreferenceManager.getDefaultSharedPreferences(getActivity()).getBoolean("new_article_created", false)) {
                PreferenceManager.getDefaultSharedPreferences(getActivity()).edit().putBoolean("new_article_created", false).apply();
            }
            if (PreferenceManager.getDefaultSharedPreferences(getActivity()).getBoolean("new_reply_created", false)) {
                PreferenceManager.getDefaultSharedPreferences(getActivity()).edit().putBoolean("new_reply_created", false).apply();
            }*/
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
            pref.edit().putBoolean("new_reply_created", false).apply();
            try {
                countList.set(pref.getInt("titlePos", 0), pref.getInt("replyNum", 0));
                adapter.notifyDataSetChanged();
            } catch (Exception e) {
            }
        }
    }
}
