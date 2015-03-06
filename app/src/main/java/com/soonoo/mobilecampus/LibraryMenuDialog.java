package com.soonoo.mobilecampus;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.Window;

/**
 * Created by soonoo on 2015-02-28.
 */
public class LibraryMenuDialog extends Dialog implements View.OnClickListener {
    LibraryMenuDialog(Context context){
        super(context);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.library_menu_dialog);
        findViewById(R.id.dialog_menu1).setOnClickListener(this);
        findViewById(R.id.dialog_menu2).setOnClickListener(this);
    }

    @Override
    public void onClick(View view){
        switch(view.getId()) {
            case R.id.dialog_menu1:
                getContext().startActivity(new Intent(getContext(), LibrarySeatInfoActivity.class));
                return;
            case R.id.dialog_menu2:
                getContext().startActivity(new Intent(getContext(), LibraryBookSearch.class));
                return;
        }
    }
}
