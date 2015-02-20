package com.soonoo.mobilecampus;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by soonoo on 2015-02-17.
 */
public class MainListDialog extends Dialog implements View.OnClickListener {
    int position;
    MainListDialog(Context context, int position){
        super(context);
        this.position = position;

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.main_list_dialog);

        TextView dialogTitle = (TextView) findViewById(R.id.dialog_title);
        dialogTitle.setText(User.subName.get(position - 1));

        findViewById(R.id.dialog_menu1).setOnClickListener(this);
        findViewById(R.id.dialog_menu2).setOnClickListener(this);
        findViewById(R.id.dialog_menu3).setOnClickListener(this);
    }

    //TODO:
    @Override
    public void onClick(View view){
        switch(view.getId()) {
            case R.id.dialog_menu1:
                return;
            case R.id.dialog_menu2:
                return;
            case R.id.dialog_menu3:
                Intent intent = new Intent(getContext(), SyllabusActivity.class);
                intent.putExtra("subIndex", position);
                getContext().startActivity(intent);
                return;
        }
    }
}
