package com.soonoo.mobilecampus;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.PrintWriter;
import java.io.StringWriter;


public class LoginActivity extends Activity {
    private static Context ctx;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Tracker t = ((Controller)getApplication()).getTracker(Controller.TrackerName.APP_TRACKER);
        t.setScreenName("LoginActivity");
        t.send(new HitBuilders.AppViewBuilder().build());

        ctx = getApplicationContext();

        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        final Button login_button = (Button) findViewById(R.id.login_button);
        final EditText login_id = (EditText) findViewById(R.id.text_id);
        final EditText login_pw = (EditText) findViewById(R.id.text_pw);
        final CheckBox auto_login = (CheckBox) findViewById(R.id.auto_login);

        if(prefs.getBoolean("auto_login", false) && isConnected()){
            try {
                new Login(true, prefs.getString("user_id", null), prefs.getString("user_pw", null)).execute();
            }catch (Exception e){}
        } else if(!isConnected()){
            Toast.makeText(LoginActivity.this, R.string.error_network_connection, Toast.LENGTH_SHORT).show();
        }

        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String id = login_id.getText().toString();
                final String pw = login_pw.getText().toString();

                try{
                    if(!isConnected()) {
                        Toast.makeText(LoginActivity.this, R.string.error_network_connection, Toast.LENGTH_SHORT).show();
                    } else if(id.equals("")){
                        Toast.makeText(LoginActivity.this, R.string.error_id, Toast.LENGTH_SHORT).show();
                    } else if(pw.equals("")){
                        Toast.makeText(LoginActivity.this, R.string.error_pw, Toast.LENGTH_SHORT).show();
                    } else{
                        new Login(false, id, pw).execute();
                    }
                }catch(Exception e){
                    StringWriter sw = new StringWriter();
                    e.printStackTrace(new PrintWriter(sw));
                    String exceptionAsStrting = sw.toString();
                    Log.e("INFO", exceptionAsStrting);
                    e.printStackTrace();
                }
            }
        });
    }

    public class Login extends AsyncTask<Void, Void, Boolean> {
        boolean isAuto;
        String id;
        String pw;

        Login(boolean isAuto, String id, String pw){
            this.isAuto = isAuto;
            this.id = id;
            this.pw = pw;
        }

        @Override
        public void onPreExecute(){
            findViewById(R.id.progress_login).setVisibility(View.VISIBLE);
            findViewById(R.id.login_button).setEnabled(false);
            if(isAuto){
                CheckBox checkBox = (CheckBox)findViewById(R.id.auto_login);
                checkBox.setChecked(true);
                EditText text_id = (EditText)findViewById(R.id.text_id);
                text_id.setText(id);
                EditText text_pw = (EditText)findViewById(R.id.text_pw);
                text_pw.setText(id);
            }
        }

        @Override
        public Boolean doInBackground(Void... p){
            long ddd = System.currentTimeMillis();
            if(User.login(id, pw)) {
                User.getSession();
                //Log.d("time:  ", Long.toString(System.currentTimeMillis()-ddd));
                return true;
            }
            return false;
        }

        @Override
        public void onPostExecute(Boolean isValid){
            final CheckBox auto_login = (CheckBox) findViewById(R.id.auto_login);
            final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
            if(!isValid){
                Toast.makeText(LoginActivity.this, R.string.error_login_info, Toast.LENGTH_SHORT).show();
            }else{
                 prefs.edit().putBoolean("auto_login", auto_login.isChecked()).apply();

                 if(auto_login.isChecked()) {
                     prefs.edit().putString("user_id", id).apply();
                     prefs.edit().putString("user_pw", pw).apply();
                 }
                // 로그인, 메인 액티비티 리스트 초기화 진행
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                finish();
                startActivity(intent);
            }
            findViewById(R.id.login_button).setEnabled(true);
            findViewById(R.id.progress_login).setVisibility(View.GONE);
        }
    }

    public boolean isConnected(){
        ConnectivityManager cManager;
        NetworkInfo mobile;
        NetworkInfo wifi;

        cManager=(ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        mobile = cManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        wifi = cManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        if(mobile.isConnected() || wifi.isConnected()) {
            return true;
        } else{
            return false;
        }

    }

    static Context getContext(){
        return ctx;
    }

    public static class OnBack extends AsyncTask<Void, Void, Void>{
        @Override
        public Void doInBackground(Void... p){
            final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
            long ddd = System.currentTimeMillis();
            if(User.login(prefs.getString("user_id", null), prefs.getString("user_pw", null))) {
                User.getSession();

                String mainHtml = User.getHtml("POST", Sites.MAIN_URL, Sites.MAIN_QUERY, "utf-8");
                Document mainDoc = Jsoup.parse(mainHtml);
                User.subCode = Parser.getSubCode(mainDoc);
                User.subName = Parser.getSubName(mainDoc);
                //Toast.makeText(getContext(), "timeeee:  "+Long.toString(System.currentTimeMillis() - ddd), Toast.LENGTH_SHORT).show();
                Log.d("timeeee:  ", Long.toString(System.currentTimeMillis()-ddd));
            }
            return null;
        }
    }

    @Override
    public void onRestart(){
        super.onRestart();
        new OnBack().execute();
    }

    @Override
    protected void onStart(){
        super.onStart();
        GoogleAnalytics.getInstance(this).reportActivityStart(this);
    }

    @Override
    protected void onStop(){
        super.onStop();
        GoogleAnalytics.getInstance(this).reportActivityStop(this);
    }
}
