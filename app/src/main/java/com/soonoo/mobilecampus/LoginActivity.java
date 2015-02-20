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
import android.widget.Toast;
import java.io.PrintWriter;
import java.io.StringWriter;


public class LoginActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

        if(prefs.getBoolean("auto_login", false)){
            try {
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                if (new Login().execute(prefs.getString("user_id", null), prefs.getString("user_pw", null)).get());
                finish();
                startActivity(intent);
            }catch (Exception e){}
        }

        final Button login_button = (Button) findViewById(R.id.login_button);
        final EditText login_id = (EditText) findViewById(R.id.text_id);
        final EditText login_pw = (EditText) findViewById(R.id.text_pw);
        final CheckBox auto_login = (CheckBox) findViewById(R.id.auto_login);

        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = login_id.getText().toString();
                String pw = login_pw.getText().toString();
                try{
                    if(!isConnected()) {
                        Toast.makeText(LoginActivity.this, R.string.error_network_connection, Toast.LENGTH_SHORT).show();
                    }else if(id.equals("")){
                        Toast.makeText(LoginActivity.this, R.string.error_id, Toast.LENGTH_SHORT).show();
                    } else if(pw.equals("")){
                        Toast.makeText(LoginActivity.this, R.string.error_pw, Toast.LENGTH_SHORT).show();
                    } else if(new Login().execute(id, pw).get()){
                        prefs.edit().putBoolean("auto_login", auto_login.isChecked()).apply();

                        if(auto_login.isChecked()) {
                            prefs.edit().putString("user_id", id).apply();
                            prefs.edit().putString("user_pw", pw).apply();
                        }
                        // 로그인, 메인 액티비티 리스트 초기화 진행
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        intent.putExtra("user_id", login_id.getText().toString());
                        intent.putExtra("user_pw", login_pw.getText().toString());
                        finish();
                        startActivity(intent);
                    }else{
                        Toast.makeText(LoginActivity.this, R.string.error_login_info, Toast.LENGTH_SHORT).show();
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

    public class Login extends AsyncTask<String, Void, Boolean> {
        @Override
        public Boolean doInBackground(String... user_info){
            if(User.login(user_info[0], user_info[1])) {
                User.getSession();
                return true;
            }
            return false;
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
            //3G 또는 WiFi 에 연결되어 있을 경우
            return true;
        } else{
            return false;
        }

    }
}
