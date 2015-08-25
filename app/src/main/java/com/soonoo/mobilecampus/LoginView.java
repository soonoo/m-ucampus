package com.soonoo.mobilecampus;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.soonoo.mobilecampus.util.Parser;
import com.soonoo.mobilecampus.util.User;
import com.urqa.clientinterface.URQAController;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.PrintWriter;
import java.io.StringWriter;


public class LoginView extends AppCompatActivity {
    private static Context ctx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
//        getSupportActionBar().hide();

        URQAController.InitializeAndStartSession(getApplicationContext(), "4BF35847");

        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

        if(prefs.getBoolean("get_session", false)){
            prefs.edit().putBoolean("get_session", false).apply();
            new LoginOnBack().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }

        Tracker t = ((Controller) getApplication()).getTracker(Controller.TrackerName.APP_TRACKER);
        t.setScreenName("LoginActivity");
        t.send(new HitBuilders.AppViewBuilder().build());

        ctx = getApplicationContext();

        final Button login_button = (Button) findViewById(R.id.login_button);
        final EditText login_id = (EditText) findViewById(R.id.text_id);
        final EditText login_pw = (EditText) findViewById(R.id.text_pw);

        if (prefs.getBoolean("auto_login", false) && isConnected()) {
            try {
                new Login(true, prefs.getString("user_id", null), prefs.getString("user_pw", null)).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            } catch (Exception e) {
            }
        } else if (!isConnected()) {
            Toast.makeText(LoginView.this, R.string.error_network_connection, Toast.LENGTH_SHORT).show();
        }

        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String id = login_id.getText().toString();
                final String pw = login_pw.getText().toString();

                try {
                    if (!isConnected()) {
                        Toast.makeText(LoginView.this, R.string.error_network_connection, Toast.LENGTH_SHORT).show();
                    } else if (id.equals("")) {
                        Toast.makeText(LoginView.this, R.string.error_id, Toast.LENGTH_SHORT).show();
                    } else if (pw.equals("")) {
                        Toast.makeText(LoginView.this, R.string.error_pw, Toast.LENGTH_SHORT).show();
                    } else {
                        new Login(false, id, pw).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                    }
                } catch (Exception e) {
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

        Login(boolean isAuto, String id, String pw) {
            this.isAuto = isAuto;
            this.id = id;
            this.pw = pw;
        }

        @Override
        public void onPreExecute() {
            findViewById(R.id.progress_login).setVisibility(View.VISIBLE);
            findViewById(R.id.login_button).setEnabled(false);
            if (isAuto) {
                CheckBox checkBox = (CheckBox) findViewById(R.id.auto_login);
                checkBox.setChecked(true);
                EditText text_id = (EditText) findViewById(R.id.text_id);
                text_id.setText(id);
                EditText text_pw = (EditText) findViewById(R.id.text_pw);
                text_pw.setText(id);
            }
        }

        @Override
        public Boolean doInBackground(Void... p) {
            if (User.login(id, pw)) {
                SQLiteDatabase db = openOrCreateDatabase("UserInfo.db", Context.MODE_PRIVATE, null);
                db.execSQL("CREATE TABLE IF NOT EXISTS account_info (id VARCHAR, pw VARCHAR)");
                db.execSQL("DELETE FROM account_info");
                db.execSQL("INSERT INTO account_info VALUES('" + id + "', '" + pw + "')");
                Cursor cursor = db.rawQuery("SELECT * FROM account_info", null);

                User.getSession();
                return true;
            }
            return false;
        }

        @Override
        public void onPostExecute(Boolean isValid) {
            final CheckBox auto_login = (CheckBox) findViewById(R.id.auto_login);
            final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(LoginView.this);
            if (!isValid) {
                Toast.makeText(LoginView.this, R.string.error_login_info, Toast.LENGTH_SHORT).show();
            } else {
                prefs.edit().putBoolean("auto_login", auto_login.isChecked()).apply();

                if (auto_login.isChecked()) {
                    prefs.edit().putString("user_id", id).apply();
                    prefs.edit().putString("user_pw", pw).apply();
                }
                // 로그인, 메인 액티비티 리스트 초기화 진행
                Intent intent = new Intent(LoginView.this, HomeView.class);
                finish();
                startActivity(intent);
            }
            findViewById(R.id.login_button).setEnabled(true);
            findViewById(R.id.progress_login).setVisibility(View.GONE);
        }
    }

    public class LoginOnBack extends AsyncTask<Void, Void, Boolean> {
        @Override
        public void onPreExecute() {
            findViewById(R.id.progress_login).setVisibility(View.VISIBLE);
        }

        @Override
        public Boolean doInBackground(Void... p) {
            SQLiteDatabase db = openOrCreateDatabase("UserInfo.db", Context.MODE_PRIVATE, null);
            Cursor cursor = db.rawQuery("SELECT * FROM account_info", null);

            if (cursor.moveToFirst()) {
                if (User.login(cursor.getString(0), cursor.getString(1))) {
                    User.getSession();
                    return true;
                }
                return false;
            }
            return false;
        }

        @Override
        public void onPostExecute(Boolean isValid) {
            if (!isValid) {
                Toast.makeText(LoginView.this, R.string.error_login_info, Toast.LENGTH_SHORT).show();
            } else {
                // 로그인, 메인 액티비티 리스트 초기화 진행
                Intent intent = new Intent(LoginView.this, HomeView.class);
                finish();
                startActivity(intent);
            }
            findViewById(R.id.login_button).setEnabled(true);
            findViewById(R.id.progress_login).setVisibility(View.GONE);
        }
    }

    public boolean isConnected() {
        ConnectivityManager cManager;
        NetworkInfo mobile;
        NetworkInfo wifi;

        cManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        mobile = cManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        wifi = cManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        if (mobile.isConnected() || wifi.isConnected()) {
            return true;
        } else {
            return false;
        }

    }

    public static Context getContext() {
        return ctx;
    }

    @Override
    protected void onStart() {
        super.onStart();
        GoogleAnalytics.getInstance(this).reportActivityStart(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        GoogleAnalytics.getInstance(this).reportActivityStop(this);
    }


}
