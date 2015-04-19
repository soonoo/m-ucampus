package com.soonoo.mobilecampus;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;

/**
 * Created by soonoo on 2015-02-17.
 */
public class Parser {

    static void getNewNotice(Document doc){
        Elements elements = doc.select("table.main_box ~ table.main_box table tr");

        for(Element element: elements){
            if(!element.select("a[href*=Notice]").isEmpty()) {
                Log.d("ddddddd", "true");
                User.isNew.add(true);
            }
            else User.isNew.add(false);
        }
    }

    static ArrayList<String> getSubCode(Document doc){
        Elements elements = doc.select("table.main_box ~ table.main_box").select("a[href]:eq(0)");
        ArrayList<String> list = new ArrayList<String>();

        for(Element element: elements){
            String temp = element.attr("href");
            temp = temp.substring(temp.indexOf("U") + 1, temp.indexOf("U") + 17);
            list.add(temp);
        }
        return list;
    }

    static void setSubCode(){
        Context context = LoginActivity.getContext();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String code;
        User.subCode = new ArrayList<>();
        for(int i = 0; (code = prefs.getString("code"+ i, null)) != null; i++){
            User.subCode.add(code);
        }
    }

    static ArrayList<String> getSubName(Document doc){
        Elements elements = doc.select("table.main_box ~ table.main_box").select(".list_txt:contains( ):eq(1)");
        //Elements elements = doc.select("td:contains([학부])");

        ArrayList<String> list = new ArrayList<String>();

        for(Element element: elements){
            list.add(element.text());
        }
        return list;
    }

    static String getProf(Document doc){
        Element element = doc.select("td:contains(담당교수) ~ td").first();

        if(element != null) return element.text();
        else return "교수 정보 없음";
    }

    static String getTime(Document doc){
        Element element = doc.select("td:contains(강의실) ~ td").first();

        if(element != null) return element.text();
        else return "강의실 정보 없음";
    }

    static String getSubQuery(String code){
        String query = "&bunban_no=" + code.substring(13, 15) +
                "&hakgi=" + code.substring(4, 5) +
                "&open_grade=" + code.substring(15) +
                "&open_gwamok_no=" + code.substring(5, 9) +
                "&open_major_code=" + code.substring(9, 13) +
                "&this_year=" + code.substring(0, 4);
        return query;
    }

    static String getReferQuery(String code, int page){
        String query = Sites.LEC_REFER_QUERY +
                "&p_subj=U" + code +
                "&p_year=" + code.substring(0, 4) +
                "&p_subjseq=" + code.substring(4, 5) +
                "&p_class=" + code.substring(13, 15) +
                "&p_pageno=" + page;
        // 2015 1 1994 7220 01 2
        return query;
    }

    static String getReferViewQuery(String code, String p_dbseq){
        String query = Sites.LEC_REFER_VIEW_QUERY +
                "&p_subj=U" + code +
                "&p_year=" + code.substring(0, 4) +
                "&p_subjseq=" + code.substring(4, 5) +
                "&p_class=" + code.substring(13, 15) +
                "&p_bdseq=" + p_dbseq;
        // 2015 1 1994 7220 01 2
        return query;
    }

    static String getNoticeQuery(String code, int page){
        String query = Sites.NOTICE_QUERY +
                "&p_subj=U" + code +
                "&p_year=" + code.substring(0, 4) +
                "&p_subjseq=" + code.substring(4, 5) +
                "&p_class=" + code.substring(13, 15) +
                "&p_pageno=" + page;
        return query;
    }

    static String getNoticeViewQuery(String seq){
        return Sites.NOTICE_VIEW_QUERY +
                "&p_bdseq=" + seq;
    }
}
