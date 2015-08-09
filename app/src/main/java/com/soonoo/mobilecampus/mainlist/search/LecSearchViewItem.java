package com.soonoo.mobilecampus.mainlist.search;

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
