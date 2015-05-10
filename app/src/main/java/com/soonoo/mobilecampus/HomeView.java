package com.soonoo.mobilecampus;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.astuetz.PagerSlidingTabStrip;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;


public class HomeView extends ActionBarActivity {
    //float density = getApplicationContext().getResources().getDisplayMetrics().density;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Tracker t = ((Controller)getApplication()).getTracker(Controller.TrackerName.APP_TRACKER);
        t.setScreenName("MainActivity");
        t.send(new HitBuilders.AppViewBuilder().build());

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

    @Override
    public void onRestart(){
        super.onRestart();
        new LoginView.OnBack().execute();
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
