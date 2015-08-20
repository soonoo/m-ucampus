package com.soonoo.mobilecampus.mainlist;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.soonoo.mobilecampus.Controller;
import com.soonoo.mobilecampus.LoginView;
import com.soonoo.mobilecampus.R;
import com.soonoo.mobilecampus.board.create.CreateArticleView;


public class HomeView extends AppCompatActivity {
    int curPos = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Tracker t = ((Controller) getApplication()).getTracker(Controller.TrackerName.APP_TRACKER);
        t.setScreenName("MainActivity");
        t.send(new HitBuilders.AppViewBuilder().build());

        final ViewPager pager = (ViewPager) findViewById(R.id.pager);
        final TabLayout tabs = (TabLayout) findViewById(R.id.tabs);

        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                curPos = position;
                invalidateOptionsMenu();
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }

            @Override
            public void onPageScrolled(int position, float arg1, int arg2) {
            }
        });
        try {
            pager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
        } catch (Exception e) {
            ActivityCompat.finishAffinity(this);
            startActivity(new Intent(this, LoginView.class));
        }
        tabs.setBackgroundColor(getResources().getColor(R.color.r3));
        tabs.setupWithViewPager(pager);

        pager.setOffscreenPageLimit(2);
        pager.setCurrentItem(1);

        // Bind the tabs to the ViewPager
        //tabs.setTabTextColors(Color.parseColor("#b3b3b3"), Color.parseColor("#ffffff"));

    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.clear();
        if (curPos == 0) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.menu_home_view, menu);
        }

        menu.add(0, 0, 0, "로그아웃"); //int groupId, int itemId, int order, CharSequence title
        menu.add(0, 1, 0, "종료"); //int groupId, int itemId, int order, CharSequence title
        //menu.add(0, 2, 0, "정보"); //int groupId, int itemId, int order, CharSequence title
        //menu.add(0, 3, 0, "변경사항"); //int groupId, int itemId, int order, CharSequence title
        //menu.removeItem(4);
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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 0:
                AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.MyAlertDialogStyle);
                builder.setMessage("로그아웃합니다.");
                builder.setPositiveButton("로그아웃", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        SharedPreferences prefs = PreferenceManager
                                .getDefaultSharedPreferences(getApplicationContext());
                        prefs.edit().putBoolean("auto_login", false).apply();
                        Intent intent = new Intent(getApplicationContext(), LoginView.class);
                        finish();
                        startActivity(intent);
                    }
                });
                builder.setNegativeButton("취소", null);
                builder.show();

                return true;
            case 1:
                finish();
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
    public void finish() {
        super.finish();
        this.overridePendingTransition(0, 0);
    }

    @Override
    protected void onStop() {
        super.onStop();
        GoogleAnalytics.getInstance(this).reportActivityStop(this);
    }
}
