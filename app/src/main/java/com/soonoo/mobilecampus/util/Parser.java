package com.soonoo.mobilecampus.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.soonoo.mobilecampus.LoginView;
import com.soonoo.mobilecampus.Sites;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;

/**
 * Created by soonoo on 2015-02-17.
 */
public class Parser {

    public static void setNewNotice(Document doc) {
        User.isNew = new ArrayList<>();
        Elements elements = doc.select("table.main_box ~ table.main_box table tr");

        for (Element element : elements) {
            if (!element.select("a[href*=Notice]").isEmpty()) {
                Log.d("ddddddd", "true");
                User.isNew.add(true);
            } else {
                User.isNew.add(false);
            }
        }
    }

    public static void setSubCode(Document doc) {
        User.subCode = new ArrayList<>();
        Elements elements = doc.select("table.main_box ~ table.main_box").select("a[href]:eq(0)");

        for (Element element : elements) {
            String temp = element.attr("href");
            temp = temp.substring(temp.indexOf("U") + 1, temp.indexOf("U") + 17);
            User.subCode.add(temp);
        }
    }

    public static void resetSubCode() {
        Context context = LoginView.getContext();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String code;
        User.subCode = new ArrayList<>();
        for (int i = 0; (code = prefs.getString("code" + i, null)) != null; i++) {
            User.subCode.add(code);
        }
    }

    public static void setSubName(Document doc) {
        User.subName = new ArrayList<>();
        Elements elements = doc.select("table.main_box ~ table.main_box").select(".list_txt:contains( ):eq(1)");

        for (Element element : elements) {
            User.subName.add(element.text());
        }
    }

    public static String getProf(Document doc) {
        Element element = doc.select("td:contains(담당교수) ~ td").first();

        if (element != null) return element.text();
        else return "교수 정보 없음";
    }

    public static String getTime(Document doc) {
        Element element = doc.select("td:contains(강의실) ~ td").first();

        if (element != null) return element.text();
        else return "강의실 정보 없음";
    }

    public static String getSubQuery(String code) throws Exception{
        String query = "&bunban_no=" + code.substring(13, 15) +
                "&hakgi=" + code.substring(4, 5) +
                "&open_grade=" + code.substring(15) +
                "&open_gwamok_no=" + code.substring(5, 9) +
                "&open_major_code=" + code.substring(9, 13) +
                "&this_year=" + code.substring(0, 4);
        return query;
    }

    public static String getReferQuery(String code, int page) throws Exception{
        String query = Sites.LEC_REFER_QUERY +
                "&p_subj=U" + code +
                "&p_year=" + code.substring(0, 4) +
                "&p_subjseq=" + code.substring(4, 5) +
                "&p_class=" + code.substring(13, 15) +
                "&p_pageno=" + page;
        // 2015 1 1994 7220 01 2
        return query;
    }

    public static String getReferViewQuery(String code, String p_dbseq) throws Exception {
        String query = Sites.LEC_REFER_VIEW_QUERY +
                "&p_subj=U" + code +
                "&p_year=" + code.substring(0, 4) +
                "&p_subjseq=" + code.substring(4, 5) +
                "&p_class=" + code.substring(13, 15) +
                "&p_bdseq=" + p_dbseq;
        // 2015 1 1994 7220 01 2
        return query;
    }

    public static String getNoticeQuery(String code, int page) throws Exception {
        String query = Sites.NOTICE_QUERY +
                "&p_subj=U" + code +
                "&p_year=" + code.substring(0, 4) +
                "&p_subjseq=" + code.substring(4, 5) +
                "&p_class=" + code.substring(13, 15) +
                "&p_pageno=" + page;
        return query;
    }

    public static String getNoticeViewQuery(String seq) throws Exception{
        return Sites.NOTICE_VIEW_QUERY +
                "&p_bdseq=" + seq;
    }

    public static String getAssignQuery(String code) throws Exception {
        return "p_process=" +
                "&p_gate=univ" +
                "&p_grcode=N000003" +
                "&p_subj=U" + code +//:U2015104537220022
                "&p_year=" + code.substring(0, 4) +//2015
                "&p_subjseq=" + code.substring(4, 5) +//1
                "&p_class=" + code.substring(13, 15) +//02
                //"&p_userid=" + //:2014722023UA
                "&p_page=" + //
                "&gubun_code=11" + //
                "&p_tutor_name=";
    }

    public static String getAssignDetailQuery(String code) {
        // &&&&&&p_userid=2014722023UA&&p_orderseq=3&p_weeklyseq=16&p_weekysubseq=1
        return "p_process=viewBasicScore" +
                "p_gate=univ" +
                "p_grcode=N000003" +
                "gubun_code=11" +
                "p_subj=U" + code +
                "&p_year=" + code.substring(0, 4) +//2015
                "&p_subjseq=" + code.substring(4, 5) +//1
                "&p_class=" + code.substring(13, 15);//02
    }

    public static String getStuReferQuery(String code, int page) throws Exception{
        //p_subjseq=1&p_class=04&s_yearsubjseq=2014%2C1&s_subjclass=U2014110777000041%2C04&
        return "p_pageno=" + page +
                "&p_process=listPage" +
                "&p_grcode=N000003" +
                "&p_subj=U" + code +
                "&p_year=" + code.substring(0, 4) +
                "&p_subjseq=" + code.substring(4, 5) +
                "&p_class=" + code.substring(13, 15);
    }

    public static String getStuReferArticleQuery(String p_bdseq) {
        return "&p_process=view" +
                "&p_grcode=N000003" +
                "&p_bdseq=" + p_bdseq;
    }

}
