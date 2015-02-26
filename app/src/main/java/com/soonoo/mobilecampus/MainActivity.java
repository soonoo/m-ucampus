package com.soonoo.mobilecampus;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.astuetz.PagerSlidingTabStrip;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;


public class MainActivity extends ActionBarActivity {

    private Context context;
    //float density = getApplicationContext().getResources().getDisplayMetrics().density;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.context = getApplicationContext();

        this.overridePendingTransition(0, 0);

        ViewPager pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));

        // Bind the tabs to the ViewPager
        PagerSlidingTabStrip tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        tabs.setShouldExpand(true);
        tabs.setViewPager(pager);

        //tabs.setTextSize(Math.round((float)14 * density));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        menu.add(0, 1, 0, "로그아웃"); //int groupId, int itemId, int order, CharSequence title
        menu.add(0,2,0,"종료"); //int groupId, int itemId, int order, CharSequence title
        menu.add(0,3,0,"정보"); //int groupId, int itemId, int order, CharSequence title
        menu.add(0,4,0,"변경사항"); //int groupId, int itemId, int order, CharSequence title
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case 1:
                LogoutDialog dialog_logout = new LogoutDialog(this);
                dialog_logout.show();
                return true;
            case 2:
                finish();
                return true;
            case 3:
                NoticeDialog dialog_notice = new NoticeDialog(this);
                dialog_notice.show();
                return true;
            case 4:
                ChangeLogDialog dialog_change = new ChangeLogDialog(this);
                dialog_change.show();
                return true;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    @Override
    public void finish(){
        super.finish();
        this.overridePendingTransition(0, 0);
    }
}
