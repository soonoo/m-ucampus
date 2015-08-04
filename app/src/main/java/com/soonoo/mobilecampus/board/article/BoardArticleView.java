package com.soonoo.mobilecampus.board.article;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.soonoo.mobilecampus.R;
import com.soonoo.mobilecampus.Sites;
import com.soonoo.mobilecampus.User;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class BoardArticleView extends AppCompatActivity {
    String id;
    ListView replyList;
    ArrayList<String> contentList;
    ArrayList<String> dateList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board_article_view);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        id = Integer.toString(getIntent().getIntExtra("id", 0));

        new GetReplys().execute();
    }

    class GetArticle extends AsyncTask<Void, Void, String> {
            View header = getLayoutInflater().inflate(R.layout.board_article_header, null, false);
            TextView tv = (TextView) header.findViewById(R.id.msg_fail);

        public String doInBackground(Void... p){
            return User.getHtml("GET", Sites.BOARD_URL + "/read/article?article_id=" + id, "UTF-8");
        }

        public void onPostExecute(String json){
            try {
                JSONArray jsonArray = new JSONArray(json);
                JSONObject article =jsonArray.getJSONObject(0);
                if(article == null) throw new Exception();

                TextView content = (TextView) header.findViewById(R.id.article_content);
                TextView reply_count = (TextView) header.findViewById(R.id.reply_title);

                content.setText(article.getString("content"));
                reply_count.setText("댓글 [" + contentList.size() + "]");

                setTitle(article.getString("title"));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                    getSupportActionBar().setSubtitle(article.getString("date") + "   |   조회:" + article.getString("view_count"));
                }

                replyList.removeHeaderView(replyList.findViewById(R.id.header));
                replyList.addHeaderView(header);
            }catch(Exception e){
                tv.setVisibility(View.VISIBLE);
                LinearLayout ll = (LinearLayout) header.findViewById(R.id.article_article);
                ll.setVisibility(View.GONE);
                e.printStackTrace();
            }

            final Button submitButton = (Button) findViewById(R.id.submit_button);
            final EditText reply = (EditText) findViewById(R.id.reply_write);

            submitButton.setOnClickListener(new View.OnClickListener(){
               public void onClick(View view){
                   String text = reply.getText().toString();
                   text = text.replaceAll("[\\n|\\r]", " ");

                   if(text.equals("")) return;
                   if(text.replaceAll("\\p{Space}", "").equals("")) return;

                   InputMethodManager imm = (InputMethodManager)getSystemService(
                           Context.INPUT_METHOD_SERVICE);
                   imm.hideSoftInputFromWindow(submitButton.getWindowToken(), 0);
                   new SubmitReply().execute(text);
               }
            });

        }
    }

    class SubmitReply extends AsyncTask<String, Void, String>{
        public String doInBackground(String... p){
            String query = "id_article=" + id + "&content=" + p[0];
            String result =  User.getHtml("POST", Sites.BOARD_URL + "/write/reply?", query, "UTF-8");

            if(result.equals("OK")){
                return User.getHtml("GET", Sites.BOARD_URL + "/read/reply?id_article=" + id, "UTF-8");
            }else{
                return null;
            }
        }

        public void onPostExecute(String response){
            if(response == null) {
                return;
            } else {
                contentList = new ArrayList<>();
                dateList = new ArrayList<>();

                try{
                    JSONArray jsonArray = new JSONArray(response);

                    for(int i = 0 ; i < jsonArray.length(); i++) {
                        JSONObject reply = jsonArray.getJSONObject(i);
                        if (reply == null) throw new Exception();

                        contentList.add(reply.getString("content"));
                        dateList.add(reply.getString("date"));
                    }
                    BoardReplyAdapter adapter = new BoardReplyAdapter(contentList, dateList);
                    replyList.setAdapter(adapter);

                    EditText reply = (EditText) findViewById(R.id.reply_write);
                    reply.setText("");

                    TextView tv = (TextView) replyList.findViewById(R.id.reply_title);
                    tv.setText("댓글 [" + contentList.size() + "]");

                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        }
    }


    class GetReplys extends AsyncTask<Void, Void, String>{
        BoardReplyAdapter adapter;

        public void onPreExecute(){
            contentList = new ArrayList<>();
            dateList = new ArrayList<>();
        }

        public String doInBackground(Void... p){
            return User.getHtml("GET", Sites.BOARD_URL + "/read/reply?id_article=" + id, "UTF-8");
        }

        public void onPostExecute(String json){
            try {
                JSONArray jsonArray = new JSONArray(json);

                for(int i = 0 ; i < jsonArray.length(); i++) {
                    JSONObject reply = jsonArray.getJSONObject(i);
                    if (reply == null) throw new Exception();

                    contentList.add(reply.getString("content"));
                    dateList.add(reply.getString("date"));
                }

                replyList = (ListView) findViewById(R.id.reply_list);
                adapter = new BoardReplyAdapter(contentList, dateList);
                replyList.setAdapter(adapter);
            }catch(Exception e){
                e.printStackTrace();
            }

            new GetArticle().execute();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
