package com.soonoo.mobilecampus;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;

/**
 * Created by soonoo on 2015-02-17.
 */
public class Parser {

    static ArrayList<String> getSubCode(Document doc){
        Elements elements = doc.select("table.main_box ~ table.main_box").select("a[href]");
        ArrayList<String> list = new ArrayList<String>();

        for(Element element: elements){
            String temp = element.attr("href");
            temp = temp.substring(temp.indexOf("U") + 1, temp.indexOf("U") + 17);
            list.add(temp);
        }
        return list;
    }

    static ArrayList<String> getSubName(Document doc){
        Elements elements = doc.select("table.main_box ~ table.main_box").select(".list_txt:contains( )");
        ArrayList<String> list = new ArrayList<String>();

        for(Element element: elements){
            list.add(element.text());
        }
        return list;
    }

    static String getProf(Document doc){
        Element element = doc.select("td:contains(담당교수) ~ td").first();

        return element.text();
    }

    static String getTime(Document doc){
        Element element = doc.select("td:contains(강의실) ~ td").first();

        return element.text();
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
}
