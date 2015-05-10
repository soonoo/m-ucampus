package com.soonoo.mobilecampus;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

/**
 * Created by soonoo on 2015-02-17.
 */
public class HomeDialog extends Dialog implements View.OnClickListener {
    int position;
    HomeDialog(Context context, int position){
        super(context);
        this.position = position;

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.main_list_dialog);

        TextView dialogTitle = (TextView) findViewById(R.id.dialog_title);
        dialogTitle.setText(User.subName.get(position - 1));

        findViewById(R.id.dialog_menu1_main).setOnClickListener(this);
        findViewById(R.id.dialog_menu2)     .setOnClickListener(this);
        findViewById(R.id.dialog_menu3)     .setOnClickListener(this);
    }

    //TODO:
    @Override
    public void onClick(View view){
        Intent intent;
        switch(view.getId()) {
            case R.id.dialog_menu1_main:
                intent = new Intent(getContext(), NoticeView.class);
                intent.putExtra("subIndex", position);
                getContext().startActivity(intent);
                return;
            case R.id.dialog_menu2:
                intent = new Intent(getContext(), ReferView.class);
                intent.putExtra("subIndex", position);
                getContext().startActivity(intent);
                return;
            case R.id.dialog_menu3:
                intent = new Intent(getContext(), SyllabusView.class);
                intent.putExtra("subIndex", position);
                getContext().startActivity(intent);
                return;
        }
    }
}
