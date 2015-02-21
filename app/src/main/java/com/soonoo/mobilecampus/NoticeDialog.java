package com.soonoo.mobilecampus;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.Window;

/**
 * Created by soonoo on 2015-02-21.
 */
public class NoticeDialog extends Dialog implements View.OnClickListener{
    NoticeDialog(Context context){
        super(context);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.main_notice_dialog);
        findViewById(R.id.notice_confirm).setOnClickListener(this);
    }
    @Override
    public void onClick(View view){
        switch(view.getId()) {
            case R.id.notice_confirm:
                this.dismiss();
                return;
        }
    }
}
