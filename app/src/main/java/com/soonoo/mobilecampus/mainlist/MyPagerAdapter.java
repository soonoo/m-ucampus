package com.soonoo.mobilecampus.mainlist;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.soonoo.mobilecampus.board.BoardHomeView;
import com.soonoo.mobilecampus.timetable.HomeViewTableFrag;

/**
 * Created by soonoo on 2015-02-22.
 */
public class MyPagerAdapter extends FragmentPagerAdapter {
    Context context;
    SharedPreferences pref;
    boolean board_off;

    public MyPagerAdapter(FragmentManager fm, SharedPreferences pref) {
        super(fm);
        this.pref = pref;
        this.board_off = pref.getBoolean("board_off", false);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (board_off) position++;
        switch (position) {
            case 0:
                return "게시판";
            case 1:
                return "유캠퍼스";
            case 2:
                return "시간표";
        }
        return null;
    }

    @Override
    public int getCount() {
        if (board_off) return 2;
        else return 3;
    }

    @Override
    public Fragment getItem(int position) {
        if (board_off) position++;
        switch (position) {
            case 0:
                return new BoardHomeView();
            case 1:
                return new HomeViewSubListFrag();
            case 2:
                return new HomeViewTableFrag();
        }
        return null;
    }
}
