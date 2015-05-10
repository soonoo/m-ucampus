package com.soonoo.mobilecampus;

import android.support.v7.internal.widget.AdapterViewCompat;
import android.view.View;
import android.widget.AdapterView;

/**
 * Created by soonoo on 2015-05-02.
 */
public class LecSearchViewItem {
    public String name;
    public String value;

    public LecSearchViewItem(String name, String value)
    {
        this.name = name;
        this.value = value;
    }

    @Override
    public String toString()
    {
        return name;
    }
}
