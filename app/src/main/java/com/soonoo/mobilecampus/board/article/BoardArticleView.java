package com.soonoo.mobilecampus.board.article;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.soonoo.mobilecampus.R;
import com.soonoo.mobilecampus.Sites;
import com.soonoo.mobilecampus.util.User;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;

public class BoardArticleView extends AppCompatActivity {
    String id;
    int pos;
    ListView replyList;
    ArrayList<String> contentList;
    ArrayList<String> dateList;
    Button submitButton;
    View header;
    BoardReplyAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board_article_view);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        id = Integer.toString(getIntent().getIntExtra("id", 0));
        pos = getIntent().getIntExtra("pos", 0);
        new GetReplys().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    class GetArticle extends AsyncTask<Void, Void, String> {
            TextView tv = (TextView) header.findViewById(R.id.msg_fail);

        public void onPreExecute(){
            submitButton = (Button) header.findViewById(R.id.submit_button);
        }

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

                getSupportActionBar().setTitle(article.getString("title"));
                getSupportActionBar().setSubtitle(article.getString("date") + "   |   조회:" + article.getString("view_count")
                        + "  |  " + article.getString("ip") + ".*.*");

                replyList.removeHeaderView(replyList.findViewById(R.id.header));
                replyList.addHeaderView(header);
                replyList.setAdapter(adapter);
            }catch(Exception e){
                tv.setVisibility(View.VISIBLE);
                LinearLayout ll = (LinearLayout) header.findViewById(R.id.article_article);
                ll.setVisibility(View.GONE);
                e.printStackTrace();
             }

            final EditText reply = (EditText) header.findViewById(R.id.reply_write);

            reply.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    String text = charSequence.toString();
                    if(text.equals("") || text.replaceAll("[ |\\n|\\r]", "").equals("")) submitButton.setEnabled(false);
                    else submitButton.setEnabled(true);
                }

                @Override
                public void afterTextChanged(Editable editable) {
                }
            });

            submitButton.setOnClickListener(new View.OnClickListener(){
               public void onClick(View view){
                   String text = reply.getText().toString();

                   if(text.equals("")) return;
                   if(text.replaceAll("[\\n|\\r|\\t| ]", "").equals("")) return;
//                   if(text.replaceAll("\\p{Space}", "").equals("")) return;

                   InputMethodManager imm = (InputMethodManager)getSystemService(
                           Context.INPUT_METHOD_SERVICE);
                   imm.hideSoftInputFromWindow(submitButton.getWindowToken(), 0);
                   new SubmitReply().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, text);
               }
            });

        }
    }

    class SubmitReply extends AsyncTask<String, Void, String>{
        ProgressBar pb = (ProgressBar) header.findViewById(R.id.pb_reply);
        @Override
        protected void onPreExecute() {
            submitButton.setVisibility(View.GONE);
            pb.setVisibility(View.VISIBLE);
        }

        public String doInBackground(String... p){
            String query = null;
            try {
                query = "id_article=" + id + "&content=" + URLEncoder.encode(p[0], "UTF-8");
            }catch(Exception e){
            }
            String result =  User.getHtml("GET", Sites.BOARD_URL + "/write/reply?" + query, "UTF-8");

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
                        dateList.add(reply.getString("date") + "  |  " + reply.getString("ip")  + ".*.*");
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

                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                prefs.edit().putBoolean("new_reply_created", true).apply();
                prefs.edit().putInt("titlePos", pos).apply();
                prefs.edit().putInt("replyNum", contentList.size()).apply();

                submitButton.setVisibility(View.VISIBLE);
                pb.setVisibility(View.GONE);
            }
        }
    }


    class GetReplys extends AsyncTask<Void, Void, String>{

        public void onPreExecute(){
            header = getLayoutInflater().inflate(R.layout.board_article_header, null, false);

            contentList = new ArrayList<>();
            dateList = new ArrayList<>();
        }

        public String doInBackground(Void... p){
            return User.getHtml("GET", Sites.BOARD_URL + "/read/reply?id_article=" + id, "UTF-8");
        }

        public void onPostExecute(String json){
            try {
                JSONArray jsonArray = new JSONArray(json);

                for(int i = jsonArray.length()-1 ; i > -1; i--) {
                    JSONObject reply = jsonArray.getJSONObject(i);
                    if (reply == null) throw new Exception();

                    contentList.add(reply.getString("content"));
                    dateList.add(reply.getString("date") + "  |  " + reply.getString("ip") + ".*.*");
                }

                replyList = (ListView) findViewById(R.id.reply_list);
                adapter = new BoardReplyAdapter(contentList, dateList);
            }catch(Exception e){
                e.printStackTrace();
            }

            new GetArticle().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
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
