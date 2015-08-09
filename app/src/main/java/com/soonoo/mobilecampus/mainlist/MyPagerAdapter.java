package com.soonoo.mobilecampus.mainlist;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.soonoo.mobilecampus.board.BoardHomeView;
import com.soonoo.mobilecampus.timetable.HomeViewTableFrag;

/**
 * Created by soonoo on 2015-02-22.
 */
public class MyPagerAdapter extends FragmentPagerAdapter {
    public MyPagerAdapter(FragmentManager fm) {
        super(fm);
    }
    @Override
    public CharSequence getPageTitle(int position) {
        switch(position){
            case 1: return "유캠퍼스";
            case 2: return "시간표";
            case 0: return "게시판";
        }
        return null;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public Fragment getItem(int position) {
        switch(position){
            case 1: return new HomeViewSubListFrag();
            case 2: return new HomeViewTableFrag();
            case 0: return new BoardHomeView();
        }
        return null;
    }
}
