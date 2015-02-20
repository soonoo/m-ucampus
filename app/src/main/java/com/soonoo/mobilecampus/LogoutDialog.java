package com.soonoo.mobilecampus;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.Window;

/**
 * Created by soonoo on 2015-02-20.
 */
public class LogoutDialog extends Dialog implements View.OnClickListener{
    Context context;
    LogoutDialog(Context context){
        super(context);
        this.context = context;

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.main_list_diaolg_logout);

        findViewById(R.id.logout_canel).setOnClickListener(this);
        findViewById(R.id.logout_confirm).setOnClickListener(this);
    }

    @Override
    public void onClick(View view){
        switch(view.getId()){
            case R.id.logout_confirm:

                SharedPreferences prefs = PreferenceManager
                        .getDefaultSharedPreferences(context);
                prefs.edit().putBoolean("auto_login", false).apply();
//
//                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//                View loginView = inflater.inflate(R.layout.activity_login, null, false);
                this.dismiss();
                Intent intent = new Intent(context, LoginActivity.class);
                ((Activity)context).finish();
                context.startActivity(intent);

                return;
            case R.id.logout_canel:
                this.dismiss();
                return;
        }
    }
}
