package com.soonoo.mobilecampus.board.create;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
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

import java.net.URLEncoder;

public class CreateArticleView extends AppCompatActivity {
    EditText title;
    EditText content;
    ProgressBar pb;
    Button postButton;

    public void doFinish() {
        finish();
    }

    public boolean isEmpty() {
        title = (EditText) findViewById(R.id.post_title);
        content = (EditText) findViewById(R.id.post_content);

        String titleText = title.getText().toString();
        String contentText = content.getText().toString();

        if (titleText.trim().length() > 0 && contentText.trim().length() > 0 ) return false;
        else return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_article_view);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public class PostArticle extends AsyncTask<Void, Void, String> {
        String query;
        String student_id;

        @Override
        public void onPreExecute() {
            SQLiteDatabase db = openOrCreateDatabase("UserInfo.db", Context.MODE_PRIVATE, null);
            Cursor cursor = db.rawQuery("SELECT * FROM account_info", null);
            if (cursor.moveToFirst()) {
                student_id = cursor.getString(0);
            }

            try {
                query = "title=" + URLEncoder.encode(title.getText().toString().replaceAll("\\s+", " "), "UTF-8") + "&content=" + URLEncoder.encode(content.getText().toString(), "UTF-8")
                        + "&nickname=" + URLEncoder.encode(student_id, "UTF-8");
            } catch (Exception e) {
            }
        }

        @Override
        public String doInBackground(Void... p) {
            try {
                return User.getHtml("GET", Sites.BOARD_URL + "/write/article?" + query, "UTF-8");
            }catch (Exception e){
                return "";
            }
        }

        @Override
        public void onPostExecute(String response) {
            System.out.println(response);
            if (!response.equals("Bad Request")) {
                Intent intent = new Intent(getApplicationContext(), BoardArticleView.class);
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    JSONObject info = jsonArray.getJSONObject(0);
                    intent.putExtra("id", Integer.parseInt(info.getString("id")));
                    finish();
                    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    prefs.edit().putBoolean("new_article_created", true).apply();

                    startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return;
            } else {
                pb.setVisibility(View.GONE);
                postButton.setVisibility(View.VISIBLE);
                return;
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_create_article_view, menu);

        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.actionbar_done:
                item.setEnabled(false);
                if(!isEmpty()) new PostArticle().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                else Snackbar.make(findViewById(R.id.create_view), "제목, 내용을 작성해 주세요.", Snackbar.LENGTH_SHORT).show();
                    return true;
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
