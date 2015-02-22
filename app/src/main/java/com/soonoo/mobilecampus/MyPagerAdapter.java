package com.soonoo.mobilecampus;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by soonoo on 2015-02-22.
 */
public class MyPagerAdapter extends FragmentPagerAdapter {
    public MyPagerAdapter(FragmentManager fm) {
        super(fm);
    }
    @Override
    public CharSequence getPageTitle(int position) {
        return (position == 0) ? "유캠퍼스 기능" : "시간표";
    }
    @Override
    public int getCount() {
        return 2;
    }
    @Override
    public Fragment getItem(int position) {
        return (position == 0)? new MainFragmentSubList() : new MainFragmentTimeTable() ;
        //return new MainFragmentSubList();
    }
}
