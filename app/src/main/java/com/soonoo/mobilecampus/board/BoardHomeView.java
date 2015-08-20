package com.soonoo.mobilecampus.board;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.soonoo.mobilecampus.R;
import com.soonoo.mobilecampus.Sites;
import com.soonoo.mobilecampus.util.User;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class  BoardHomeView extends Fragment implements View.OnClickListener{
    Context context;
    View view;
    ArrayList<String> titleList;
    ArrayList<String> infoList;
    ArrayList<Integer> idList;
    BoardHomeViewAdpater adapter;
    int page = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.context = container.getContext();
        view = inflater.inflate(R.layout.activity_board_home_view, container, false);

       // new GetJSON().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

        return view;
    }

    public class GetJSON extends AsyncTask<Void, Void, String>{
        ProgressBar pb = (ProgressBar) view.findViewById(R.id.pb_board);

        public void onPreExecute(){
            infoList = new ArrayList<>();
            titleList = new ArrayList<>();
            idList = new ArrayList<>();
        }

        public String doInBackground(Void... p){
            return User.getHtml("GET", Sites.BOARD_URL+"/read/list?num=25&page=" + Integer.toString(page), "UTF-8");
        }

        public void onPostExecute(String json){
            try{
                JSONArray data = new JSONArray(json);
                int replyCount;

                for(int i = 0; i < data.length(); i++){
                    JSONObject jsonObject = data.getJSONObject(i);
                    replyCount = jsonObject.getInt("reply_count");

                    if(replyCount > 0 ) titleList.add(jsonObject.getString("title") + " <font color=#b30027>[" + Integer.toString(replyCount) + "]</font>");
                    else titleList.add(jsonObject.getString("title"));

                    infoList.add(jsonObject.getString("date") + "  |  조회:" + jsonObject.getString("view_count") + "  |  " + jsonObject.getString("ip") + ".*.*");
                    idList.add(jsonObject.getInt("id"));
                }

                ListView listView = (ListView) view.findViewById(R.id.board_home_list);
                adapter = new BoardHomeViewAdpater(titleList, infoList, idList);

                View footer = getActivity().getLayoutInflater().
                        inflate(R.layout.board_list_footer, null, false);
                TextView tv = (TextView) footer.findViewById(R.id.board_list_page);
                tv.setText(Integer.toString(page));

                TextView prev = (TextView) footer.findViewById(R.id.prev);
                prev.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View view){
                        if(page==1) return;
                        page--;
                        new GetJSON().execute();
                        return;
                    }
                });

                TextView next = (TextView) footer.findViewById(R.id.next);
                next.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View view) {
                        page++;
                        new GetJSON().execute();
                        return;
                    }
                });

                listView.removeFooterView(listView.findViewById(R.id.board_list_footer));
                listView.addFooterView(footer);
                //adapter.notifyDataSetChanged();
                listView.setAdapter(adapter);
            }catch(Exception e){
                e.printStackTrace();
            }

            pb.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View view){
        switch(view.getId()){
            case R.id.prev:
                page++;
                new GetJSON().execute();
                return;
            case R.id.next:
                page--;
                new GetJSON().execute();
        }
    }

    public void onStart(){
        super.onStart();
        new GetJSON().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }
}
