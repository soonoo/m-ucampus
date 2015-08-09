package com.soonoo.mobilecampus.board.create;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.soonoo.mobilecampus.R;
import com.soonoo.mobilecampus.Sites;
import com.soonoo.mobilecampus.board.article.BoardArticleView;
import com.soonoo.mobilecampus.util.User;

import org.json.JSONArray;
import org.json.JSONObject;

public class CreateArticleView extends AppCompatActivity {
    EditText title;
    EditText content;
    ProgressBar pb;
    Button postButton;

    public void doFinish(){
        finish();
    }

    public boolean isEmpty(){
        String titleText = title.getText().toString().replaceAll("[ |\\r|\\n]", "");
        String contentText = content.getText().toString().replaceAll("[ |\\r|\\n]", "");

        if(!titleText.equals("") && !contentText.equals("")) return false;
        else return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_article_view);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        title = (EditText) findViewById(R.id.post_title);
        content = (EditText) findViewById(R.id.post_content);
        postButton = (Button)findViewById(R.id.post_submit_button);

        title.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(isEmpty()) postButton.setEnabled(false);
                else postButton.setEnabled(true);
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        content.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(isEmpty()) postButton.setEnabled(false);
                else postButton.setEnabled(true);
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postButton.setVisibility(View.GONE);
                pb  = (ProgressBar) findViewById(R.id.pb_post);
                pb.setVisibility(View.VISIBLE);
                new PostArticle().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            }
        });
    }

    public class PostArticle extends AsyncTask<Void, Void, String>{
        String query;
        String student_id;

        @Override
        public void onPreExecute(){
            SQLiteDatabase db = openOrCreateDatabase("UserInfo.db", Context.MODE_PRIVATE, null);
            Cursor cursor = db.rawQuery("SELECT * FROM login_info", null);
            if(cursor.moveToFirst()){
               student_id = cursor.getString(0);
            }

            query = "title=" + title.getText().toString() + "&content=" + content.getText().toString()
                            + "&student_id=" + student_id;
        }

        @Override
        public String doInBackground(Void... p){
            return User.getHtml("POST", Sites.BOARD_URL + "/write/article", query, "UTF-8");
        }

        @Override
        public void onPostExecute(String response){
            System.out.println(response);
            if(!response.equals("Bad Request")){
                Intent intent = new Intent(getApplicationContext(), BoardArticleView.class);

                try{
                    JSONArray jsonArray = new JSONArray(response);
                    JSONObject info = jsonArray.getJSONObject(0);
                    intent.putExtra("id", Integer.parseInt(info.getString("id")));
                    doFinish();
                    startActivity(intent);
                }catch(Exception e){
                    e.printStackTrace();
                }
                return;
            } else{
                pb.setVisibility(View.GONE);
                postButton.setVisibility(View.VISIBLE);
                return;
            }
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
