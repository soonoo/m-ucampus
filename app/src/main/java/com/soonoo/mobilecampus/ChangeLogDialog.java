package com.soonoo.mobilecampus;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;

/**
 * Created by soonoo on 2015-02-23.
 */
public class ChangeLogDialog extends Dialog implements View.OnClickListener {
    ChangeLogDialog(Context context){
        super(context);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.changelog_diaolg);
        findViewById(R.id.changelog_confirm).setOnClickListener(this);
    }
    @Override
    public void onClick(View view){
        switch(view.getId()) {
            case R.id.changelog_confirm:
                this.dismiss();
                return;
        }
    }
}
