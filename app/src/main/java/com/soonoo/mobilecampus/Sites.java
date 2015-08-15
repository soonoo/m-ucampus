package com.soonoo.mobilecampus;

/**
 * Created by soonoo on 2015-02-15.
 */

//TODO:
public class Sites {
    //public static final String BOARD_URL = "http://localhost:3000";
    //public static final String BOARD_URL = "http://10.0.3.2:4000";
    public static final String BOARD_URL = "http://175.125.197.82:4000";


    public static final String LOGIN_URL = "https://info.kw.ac.kr/webnote/login/login_proc.php";
    public static final String LOGIN_QUERY = "login_type=2&redirect_url=http%3A%2F%2Finfo.kw.ac.kr%2F" +
            "&layout_opt=&gubun_code=11&p_language=KOREAN&image.x=19&image.y=18";

    public static final String SESSION_URL = "http://info2.kw.ac.kr/servlet/controller.homepage." +
            "MainServlet?p_gate=univ&p_process=main&p_page=learning" +
            "&p_kwLoginType=cookie&gubun_code=11";

    // 과목 정보 초기화용
    public static final String MAIN_URL = "http://info2.kw.ac.kr/servlet/controller.homepage.KwuMainServlet" +
            "?p_process=openStu&p_grcode=N000003";
    public static final String MAIN_QUERY = "p_process=&p_gate=univ&p_grcode=N000003" +
            "&p_page=&gubun_code=11&p_tutor_name=";

    // 강의 계획서
    public static final String SYLLABUS_URL = "http://info.kw.ac.kr/webnote/lecture/h_lecture01_2.php" +
            "?layout_opt=N&engineer_code=&skin_opt=&fsel1_code=&fsel1_str=&fsel2_code=&fsel2_str=" +
            "&fsel2=00_00&fsel3=&fsel4=00_00&hh=&sugang_opt=all&tmp_key=tmp__stu";

    //                                  http://info.kw.ac.kr/webnote/lecture/h_lecture01_2.php
    //       ?layout_opt=N&this_year=2015&hakgi=1&engineer_code=51&skin_opt=&fsel1_code=&fsel1_str=&fsel2_code=&fsel2_str=&fsel2=00_00&fsel3=&fsel4=00_00&sugang_opt=all&tmp_key=tmp__stu&open_major_code=7220&open_grade=2&open_gwamok_no=0819&bunban_no=01

    //장학
    public static final String SCHOLARSHIP_URL = "http://info.kw.ac.kr/webnote/janghak/janghak.php" +
            "?layout_opt=N&gubun_code=11&p_gate=univ&p_grcode=N000003&p_page=&p_process=&p_tutor_name=";

    //성적
    public static final String GRADE_URL = "http://info.kw.ac.kr/webnote/sungjuk/sungjuk.php" +
            "?layout_opt=N&gubun_code=11&p_gate=univ&p_grcode=N000003&p_page=&p_process=&p_subjseq=1&p_tutor_name=";

    //석차
    public static final String RANK_URL = "http://info.kw.ac.kr/webnote/class_order/order.php" +
            "?layout_opt=N&gubun_code=11&p_gate=univ&p_grcode=N000003&p_page=&p_process=&p_tutor_name=";

    //시간표
    public static final String TIMETABLE_URL = "http://info.kw.ac.kr/webnote/schedule/schedule.php";

    //중도 좌석
    public static final String LIBRARY_SEAT_URL = "http://223.194.18.3/domian/domian5.asp";

    //강의 자료실
    public static final String LEC_REFER_URL = "http://info2.kw.ac.kr/servlet/controller.learn.AssPdsStuServlet";
    public static final String LEC_REFER_QUERY = "p_process=listPage&p_grcode=N000003";
    public static final String LEC_REFER_VIEW_QUERY = "p_process=view&p_grcode=N000003";
    //"&p_subj=U2014129577220011&p_year=2014&p_subjseq=1&p_class=01&p_pageno=1";
    // &p_subj=U2015119947220012 &p_year=2015 &p_subjseq=1 &p_class=01  &p_pageno=1
    public static final String LIBRRY_MAIN_PAGE = "http://mkupis.kw.ac.kr/";

    public static final String LEC_DOWNLOAD_URL = "http://info2.kw.ac.kr/servlet/controller.library.DownloadServlet";

    public static final String NOTICE_URL = "http://info2.kw.ac.kr/servlet/controller.learn.NoticeStuServlet";
    public static final String NOTICE_QUERY = "p_process=listPage&p_process=&p_grcode=N000003";
    public static final String NOTICE_VIEW_QUERY = "p_process=view";
    //&p_subj=U2015104537220022&p_year=2015&p_subjseq=1&p_class=02&p_pageno=1
    //"p_subj=U2015104537220022&p_year=2015&p_subjseq=1&p_class=02&p_userid=2014722023UA&p_page=;

    public static final String DINING_URL = "http://www.kw.ac.kr/sub/life/uniguide18_1CP.do";

    //강의계획서 검색
    public static final String LECTURE_SEARCH = "http://info.kw.ac.kr/webnote/lecture/h_lecture.php?layout_opt=N";

    //과제 조회
    public static final String ASSIGNMENT_URL = "http://info2.kw.ac.kr/servlet/controller.learn.ReportStuServlet?p_process=listPage";

    public static final String ASSIGNMENT_DETAIL_URL = "http://info2.kw.ac.kr/servlet/controller.learn.ReportStuServlet";


    public static final String TIMETABLE_TEMPLATE = "<html>\n" +
            "<title></title>\n" +
            "<body>\n" +
            "<table width=\"100%\">\n" +
            "\t<tbody> \n" +
            "\t\t<td width=\"5%\">\n" +
            "\t\t\t<div class=\"col1\">\n" +
            "\t\t\t\t<div class=\"head\"></div>\n" +
            "\t\t\t\t<div class=\"period\">0</div>\n" +
            "\t\t\t\t<div class=\"period\">1</div>\n" +
            "\t\t\t\t<div class=\"period\">2</div>\n" +
            "\t\t\t\t<div class=\"period\">3</div>\n" +
            "\t\t\t\t<div class=\"period\">4</div>\n" +
            "\t\t\t\t<div class=\"period\">5</div>\n" +
            "\t\t\t\t<div class=\"period\">6</div>\n" +
            "\t\t\t\t<div class=\"period\">7</div>\n" +
            "\t\t\t\t<div class=\"period\">8</div>\n" +
            "\t\t\t\t<div class=\"period\">9</div>\n" +
            "\t\t\t\t<div class=\"period\">10</div>\n" +
            "\t\t\t\t<div class=\"period\">11</div>\n" +
            "\t\t\t\t<div class=\"period\">12</div>\n" +
            "\t\t\t\t<div class=\"period\">13</div>\n" +
            "\t\t\t\t<div class=\"period\">14</div>\n" +
            "\t\t\t\t<div class=\"period\">15</div>\n" +
            "\t\t\t</div>\n" +
            "\t\t</td>\n" +
            "\t\t<td class=\"col2\">\n" +
            "\t\t\t<div class=\"subject\">\n" +
            "\t\t\t</div>\n" +
            "\t\t\t<div>\n" +
            "\t\t\t\t<div class=\"day\">월</div>\n" +
            "\t\t\t\t<div class=\"subs\"></div>\n" +
            "\t\t\t\t<div class=\"subs\"></div>\n" +
            "\t\t\t\t<div class=\"subs\"></div>\n" +
            "\t\t\t\t<div class=\"subs\"></div>\n" +
            "\t\t\t\t<div class=\"subs\"></div>\n" +
            "\t\t\t\t<div class=\"subs\"></div>\n" +
            "\t\t\t\t<div class=\"subs\"></div>\n" +
            "\t\t\t\t<div class=\"subs\"></div>\n" +
            "\t\t\t\t<div class=\"subs\"></div>\n" +
            "\t\t\t\t<div class=\"subs\"></div>\n" +
            "\t\t\t\t<div class=\"subs\"></div>\n" +
            "\t\t\t\t<div class=\"subs\"></div>\n" +
            "\t\t\t\t<div class=\"subs\"></div>\n" +
            "\t\t\t\t<div class=\"subs\"></div>\n" +
            "\t\t\t\t<div class=\"subs\"></div>\n" +
            "\t\t\t\t<div class=\"subs\"></div>\n" +
            "\t\t</div>\n" +
            "\t\t</td>\n" +
            "\t\t<td  class=\"col3\">\n" +
            "\t\t\t<div class=\"subject\">\n" +
            "\t\t\t</div>\n" +
            "\t\t\t<div>\n" +
            "\t\t\t\t<div class=\"day\">화</div>\n" +
            "\t\t\t\t<div class=\"subs\"></div>\n" +
            "\t\t\t\t<div class=\"subs\"></div>\n" +
            "\t\t\t\t<div class=\"subs\"></div>\n" +
            "\t\t\t\t<div class=\"subs\"></div>\n" +
            "\t\t\t\t<div class=\"subs\"></div>\n" +
            "\t\t\t\t<div class=\"subs\"></div>\n" +
            "\t\t\t\t<div class=\"subs\"></div>\n" +
            "\t\t\t\t<div class=\"subs\"></div>\n" +
            "\t\t\t\t<div class=\"subs\"></div>\n" +
            "\t\t\t\t<div class=\"subs\"></div>\n" +
            "\t\t\t\t<div class=\"subs\"></div>\n" +
            "\t\t\t\t<div class=\"subs\"></div>\n" +
            "\t\t\t\t<div class=\"subs\"></div>\n" +
            "\t\t\t\t<div class=\"subs\"></div>\n" +
            "\t\t\t\t<div class=\"subs\"></div>\n" +
            "\t\t\t\t<div class=\"subs\"></div>\n" +
            "\t\t</div>\n" +
            "\t\t</td>\n" +
            "\t\t<td  class=\"col4\">\n" +
            "\t\t\t<div class=\"subject\">\n" +
            "\t\t\t</div>\n" +
            "\t\t\t<div>\n" +
            "\t\t\t\t<div class=\"day\">수</div>\n" +
            "\t\t\t\t<div class=\"subs\"></div>\n" +
            "\t\t\t\t<div class=\"subs\"></div>\n" +
            "\t\t\t\t<div class=\"subs\"></div>\n" +
            "\t\t\t\t<div class=\"subs\"></div>\n" +
            "\t\t\t\t<div class=\"subs\"></div>\n" +
            "\t\t\t\t<div class=\"subs\"></div>\n" +
            "\t\t\t\t<div class=\"subs\"></div>\n" +
            "\t\t\t\t<div class=\"subs\"></div>\n" +
            "\t\t\t\t<div class=\"subs\"></div>\n" +
            "\t\t\t\t<div class=\"subs\"></div>\n" +
            "\t\t\t\t<div class=\"subs\"></div>\n" +
            "\t\t\t\t<div class=\"subs\"></div>\n" +
            "\t\t\t\t<div class=\"subs\"></div>\n" +
            "\t\t\t\t<div class=\"subs\"></div>\n" +
            "\t\t\t\t<div class=\"subs\"></div>\n" +
            "\t\t\t\t<div class=\"subs\"></div>\n" +
            "\t\t</div>\n" +
            "\t\t</td>\n" +
            "\t\t<td  class=\"col5\">\n" +
            "\t\t\t<div class=\"subject\">\n" +
            "\t\t\t</div>\n" +
            "\t\t\t<div>\n" +
            "\t\t\t\t<div class=\"day\">목</div>\n" +
            "\t\t\t\t<div class=\"subs\"></div>\n" +
            "\t\t\t\t<div class=\"subs\"></div>\n" +
            "\t\t\t\t<div class=\"subs\"></div>\n" +
            "\t\t\t\t<div class=\"subs\"></div>\n" +
            "\t\t\t\t<div class=\"subs\"></div>\n" +
            "\t\t\t\t<div class=\"subs\"></div>\n" +
            "\t\t\t\t<div class=\"subs\"></div>\n" +
            "\t\t\t\t<div class=\"subs\"></div>\n" +
            "\t\t\t\t<div class=\"subs\"></div>\n" +
            "\t\t\t\t<div class=\"subs\"></div>\n" +
            "\t\t\t\t<div class=\"subs\"></div>\n" +
            "\t\t\t\t<div class=\"subs\"></div>\n" +
            "\t\t\t\t<div class=\"subs\"></div>\n" +
            "\t\t\t\t<div class=\"subs\"></div>\n" +
            "\t\t\t\t<div class=\"subs\"></div>\n" +
            "\t\t\t\t<div class=\"subs\"></div>\n" +
            "\t\t</div>\n" +
            "\t\t</td>\n" +
            "\t\t<td class=\"col6\">\n" +
            "\t\t\t<div class=\"subject\">\n" +
            "\t\t\t\t<!--<div style=\"\">데이터구조 실습</div>-->\n" +
            "\t\t\t</div>\n" +
            "\t\t\t<div>\n" +
            "\t\t\t\t<div class=\"day\">금</div>\n" +
            "\t\t\t\t<div class=\"subs\"></div>\n" +
            "\t\t\t\t<div class=\"subs\"></div>\n" +
            "\t\t\t\t<div class=\"subs\"></div>\n" +
            "\t\t\t\t<div class=\"subs\"></div>\n" +
            "\t\t\t\t<div class=\"subs\"></div>\n" +
            "\t\t\t\t<div class=\"subs\"></div>\n" +
            "\t\t\t\t<div class=\"subs\"></div>\n" +
            "\t\t\t\t<div class=\"subs\"></div>\n" +
            "\t\t\t\t<div class=\"subs\"></div>\n" +
            "\t\t\t\t<div class=\"subs\"></div>\n" +
            "\t\t\t\t<div class=\"subs\"></div>\n" +
            "\t\t\t\t<div class=\"subs\"></div>\n" +
            "\t\t\t\t<div class=\"subs\"></div>\n" +
            "\t\t\t\t<div class=\"subs\"></div>\n" +
            "\t\t\t\t<div class=\"subs\"></div>\n" +
            "\t\t\t\t<div class=\"subs\"></div>\n" +
            "\t\t</div>\n" +
            "\t\t</td>\n" +
            "\t\t<td class=\"col7\">\n" +
            "\t\t\t<div class=\"subject\">\n" +
            "\t\t\t</div>\n" +
            "\t\t\t<div class=\"saturday\">\n" +
            "\t\t\t\t<div class=\"day\">토</div>\n" +
            "\t\t\t\t<div class=\"subs\"></div>\n" +
            "\t\t\t\t<div class=\"subs\"></div>\n" +
            "\t\t\t\t<div class=\"subs\"></div>\n" +
            "\t\t\t\t<div class=\"subs\"></div>\n" +
            "\t\t\t\t<div class=\"subs\"></div>\n" +
            "\t\t\t\t<div class=\"subs\"></div>\n" +
            "\t\t\t\t<div class=\"subs\"></div>\n" +
            "\t\t\t\t<div class=\"subs\"></div>\n" +
            "\t\t\t\t<div class=\"subs\"></div>\n" +
            "\t\t\t\t<div class=\"subs\"></div>\n" +
            "\t\t\t\t<div class=\"subs\"></div>\n" +
            "\t\t\t\t<div class=\"subs\"></div>\n" +
            "\t\t\t\t<div class=\"subs\"></div>\n" +
            "\t\t\t\t<div class=\"subs\"></div>\n" +
            "\t\t\t\t<div class=\"subs\"></div>\n" +
            "\t\t\t\t<div class=\"subs\"></div>\n" +
            "\t\t\t</div>\n" +
            "\t\t</td>\n" +
            "\t</tbody>\n" +
            "</table>\n" +
            "\n" +
            "\n" +
            "<style>\n" +
            "td{\n" +
            "\tpadding:0px;\n" +
            "}\n" +
            "table{\n" +
            "\tborder-spacing:0px;\n" +
            "\tborder-collapse:collapse;\n" +
            "}\n" +
            "body{\n" +
            "\tmargin:0px;\n" +
            "}\n" +
            ".period{\n" +
            "\tfont-size:70%;\t\n" +
            "}\n" +
            ".period, .subs{\n" +
            "\tborder-right:1px solid #dfdfdf;\n" +
            "\tborder-bottom:1px solid #dfdfdf;\n" +
            "\theight:70px;\n" +
            "}\n" +
            "\n" +
            ".head, .day{\n" +
            "\tborder-right:1px solid #dfdfdf;\n" +
            "\tborder-bottom:1px solid #dfdfdf;\n" +
            "\tfont-size:70%;\n" +
            "\theight:15px;\n" +
            "}\n" +
            "\n" +
            ".day{\n" +
            "\ttext-align:center;\t\n" +
            "}\n" +
            "\n" +
            ".subject div{\n" +
            "\tposition:absolute;\n" +
            "\theight:71px;\n" +
            "\tfont-size:65%;\n" +
            "\tmargin-right:1px;\n" +
            "\tpadding:0px;\n" +
            "}\n" +
            "\n" +
            ".period{\n" +
            "\ttext-align:center;\t\n" +
            "}\n" +
            "</style>\n" +
            "</body>\n" +
            "</html>";
}
