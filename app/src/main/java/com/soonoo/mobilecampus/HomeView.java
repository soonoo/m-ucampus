package com.soonoo.mobilecampus;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.astuetz.PagerSlidingTabStrip;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.soonoo.mobilecampus.board.create.CreateArticleView;


public class HomeView extends AppCompatActivity {
    //float density = getApplicationContext().getResources().getDisplayMetrics().density;
    int curPos = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Tracker t = ((Controller)getApplication()).getTracker(Controller.TrackerName.APP_TRACKER);
        t.setScreenName("MainActivity");
        t.send(new HitBuilders.AppViewBuilder().build());

        this.overridePendingTransition(0, 0);

        ViewPager pager = (ViewPager) findViewById(R.id.pager);
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                curPos = position;
                invalidateOptionsMenu();
            }

            @Override
            public void onPageScrollStateChanged(int state) {}

            @Override
            public void onPageScrolled(int position, float arg1, int arg2) {}
        });
        pager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
        pager.setOffscreenPageLimit(2);
        pager.setCurrentItem(1);

        // Bind the tabs to the ViewPager
        PagerSlidingTabStrip tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        tabs.setShouldExpand(true);
        tabs.setViewPager(pager);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu){
        menu.clear();
        if(curPos == 0){
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.menu_home_view, menu);
        }
        else{
        }

        menu.add(0, 0, 0, "로그아웃"); //int groupId, int itemId, int order, CharSequence title
        menu.add(0, 1, 0, "종료"); //int groupId, int itemId, int order, CharSequence title
        menu.add(0, 2, 0, "정보"); //int groupId, int itemId, int order, CharSequence title
        menu.add(0, 3, 0, "변경사항"); //int groupId, int itemId, int order, CharSequence title
        menu.removeItem(4);
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
       // MenuInflater inflater = getMenuInflater();
       // inflater.inflate(R.menu.menu_home_view, menu);
        super.onCreateOptionsMenu(menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case 0:
                LogoutDialog dialog_logout = new LogoutDialog(this);
                dialog_logout.show();
                return true;
            case 1:
                finish();
                return true;
            case 2:
                NoticeDialog dialog_notice = new NoticeDialog(this);
                dialog_notice.show();
                return true;
            case 3:
                ChangeLogDialog dialog_change = new ChangeLogDialog(this);
                dialog_change.show();
                return true;
            case R.id.actionbar_write:
                Intent intent = new Intent(HomeView.this, CreateArticleView.class);
                startActivity(intent);
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

   // @Override
   // protected void onStart(){
       // super.onStart();
       // GoogleAnalytics.getInstance(this).reportActivityStart(this);
  //  }

    @Override
    protected void onStop(){
        super.onStop();
        GoogleAnalytics.getInstance(this).reportActivityStop(this);
    }
}
