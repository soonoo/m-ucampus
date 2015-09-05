package com.soonoo.mobilecampus;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.soonoo.mobilecampus.util.User;

import java.net.URLEncoder;

public class OpinionView extends AppCompatActivity {
    EditText message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opinion_view);

        message = (EditText) findViewById(R.id.post_content);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_opinion_view, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        Snackbar.make(findViewById(R.id.opinion_view), "내용을 입력해 주세요.", Snackbar.LENGTH_SHORT);
        switch (item.getItemId()) {
            case R.id.actionbar_done:
                if (message.getText().toString().trim().length() > 0) {
                    new SendOpinion(item).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(OpinionView.this, R.style.MyAlertDialogStyle);
                    builder.setMessage("내용을 입력해 주세요.");
                    builder.setPositiveButton("확인", null);
                    builder.setCancelable(false);
                    builder.show();
                }
                return true;
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public class SendOpinion extends AsyncTask<Void, Void, String> {
        MenuItem item;
        String text = "";

        SendOpinion(MenuItem item) {
            this.item = item;

            try {
                text = URLEncoder.encode(message.getText().toString(), "UTF-8");
            } catch (Exception e) {
                text = "";
            }
        }

        public void onPreExecute() {
            item.setEnabled(false);
        }

        @Override
        public String doInBackground(Void... p) {
            try {
                return User.getHtml("GET", Sites.BOARD_URL + "/ucampus/opinion?message=" + text, "UTF-8");
            } catch (Exception e) {
                return "";
            }
        }

        public void onPostExecute(String result) {
            if (result.equals("OK")) {
                AlertDialog.Builder builder = new AlertDialog.Builder(OpinionView.this, R.style.MyAlertDialogStyle);
                builder.setMessage("전송되었습니다.");
                builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                });
                builder.setCancelable(false);
                builder.show();
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(OpinionView.this, R.style.MyAlertDialogStyle);
                builder.setMessage("전송에 실패했습니다. 다시 시도해 주세요");
                builder.setPositiveButton("확인", null);
                builder.setCancelable(false);
                builder.show();
            }
            item.setEnabled(true);
        }
    }
}
