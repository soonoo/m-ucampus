package com.soonoo.mobilecampus;

/**
 * Created by soonoo on 2015-02-15.
 */

//TODO:
public class Sites {
    //public static final String BOARD_URL = "http://localhost:3000";
    //public static final String BOARD_URL = "http://10.0.3.2:4000";
    public static final String BOARD_URL = "http://soonoo.iptime.org:3000";


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

    public static final String STUDENT_REFER_URL = "http://info2.kw.ac.kr/servlet/controller.learn.PdsProfServlet";

    public static final String QNA_URL = "http://info2.kw.ac.kr/servlet/controller.learn.QnAStuServlet";

    public static final String TIMETABLE_TEMPLATE = "<html>\n" +
            "<title></title>\n" +
            "<body><span class=\"col\" id=\"index\">\n" +
            "\t\t<div class=\"day\"><span></span></div>\n" +
            "\t\t<div>0</div>\n" +
            "\t\t<div>1</div>\n" +
            "\t\t<div>2</div>\n" +
            "\t\t<div>3</div>\n" +
            "\t\t<div>4</div>\n" +
            "\t\t<div>5</div>\n" +
            "\t\t<div>6</div>\n" +
            "\t\t<div>7</div>\n" +
            "\t\t<div>8</div>\n" +
            "\t\t<div>9</div>\n" +
            "\t\t<div>10</div>\n" +
            "\t\t<div>11</div>\n" +
            "\t\t<div>12</div>\n" +
            "\t\t<div>13</div>\n" +
            "\t\t<div>14</div>\n" +
            "\t\t<div>15</div>\n" +
            "\t</span><span class=\"col\" id=\"mon\">\n" +
            "\t\t<div class=\"day\"><span>월</span></div>\n" +
            "\t\t<div></div>\n" +
            "\t\t<div></div>\n" +
            "\t\t<div></div>\n" +
            "\t\t<div></div>\n" +
            "\t\t<div></div>\n" +
            "\t\t<div></div>\n" +
            "\t\t<div></div>\n" +
            "\t\t<div></div>\n" +
            "\t\t<div></div>\n" +
            "\t\t<div></div>\n" +
            "\t\t<div></div>\n" +
            "\t\t<div></div>\n" +
            "\t\t<div></div>\n" +
            "\t\t<div></div>\n" +
            "\t\t<div></div>\n" +
            "\t\t<div></div>\n" +
            "\t</span><span class=\"col\" id=\"tue\">\n" +
            "\t\t<div class=\"day\"><span>화</span></div>\n" +
            "\t\t<div></div>\n" +
            "\t\t<div></div>\n" +
            "\t\t<div></div>\n" +
            "\t\t<div></div>\n" +
            "\t\t<div></div>\n" +
            "\t\t<div></div>\n" +
            "\t\t<div></div>\n" +
            "\t\t<div></div>\n" +
            "\t\t<div></div>\n" +
            "\t\t<div></div>\n" +
            "\t\t<div></div>\n" +
            "\t\t<div></div>\n" +
            "\t\t<div></div>\n" +
            "\t\t<div></div>\n" +
            "\t\t<div></div>\n" +
            "\t\t<div></div>\n" +
            "\t</span><span class=\"col\" id=\"wed\">\n" +
            "\t\t<div class=\"day\"><span>수</span></div>\n" +
            "\t\t<div></div>\n" +
            "\t\t<div></div>\n" +
            "\t\t<div></div>\n" +
            "\t\t<div></div>\n" +
            "\t\t<div></div>\n" +
            "\t\t<div></div>\n" +
            "\t\t<div></div>\n" +
            "\t\t<div></div>\n" +
            "\t\t<div></div>\n" +
            "\t\t<div></div>\n" +
            "\t\t<div></div>\n" +
            "\t\t<div></div>\n" +
            "\t\t<div></div>\n" +
            "\t\t<div></div>\n" +
            "\t\t<div></div>\n" +
            "\t\t<div></div>\n" +
            "\t</span><span class=\"col\" id=\"thu\">\n" +
            "\t\t<div class=\"day\"><span>목</span></div>\n" +
            "\t\t<div></div>\n" +
            "\t\t<div></div>\n" +
            "\t\t<div></div>\n" +
            "\t\t<div></div>\n" +
            "\t\t<div></div>\n" +
            "\t\t<div></div>\n" +
            "\t\t<div></div>\n" +
            "\t\t<div></div>\n" +
            "\t\t<div></div>\n" +
            "\t\t<div></div>\n" +
            "\t\t<div></div>\n" +
            "\t\t<div></div>\n" +
            "\t\t<div></div>\n" +
            "\t\t<div></div>\n" +
            "\t\t<div></div>\n" +
            "\t\t<div></div>\n" +
            "\t</span><span class=\"col\" id=\"fri\">\n" +
            "\t\t<div class=\"day\"><span>금</span></div>\n" +
            "\t\t<div></div>\n" +
            "\t\t<div></div>\n" +
            "\t\t<div></div>\n" +
            "\t\t<div></div>\n" +
            "\t\t<div></div>\n" +
            "\t\t<div></div>\n" +
            "\t\t<div></div>\n" +
            "\t\t<div></div>\n" +
            "\t\t<div></div>\n" +
            "\t\t<div></div>\n" +
            "\t\t<div></div>\n" +
            "\t\t<div></div>\n" +
            "\t\t<div></div>\n" +
            "\t\t<div></div>\n" +
            "\t\t<div></div>\n" +
            "\t\t<div></div>\n" +
            "\t</span><span class=\"col\" id=\"sat\">\n" +
            "\t\t<div class=\"day\"><span>토</span></div>\n" +
            "\t\t<div></div>\n" +
            "\t\t<div></div>\n" +
            "\t\t<div></div>\n" +
            "\t\t<div></div>\n" +
            "\t\t<div></div>\n" +
            "\t\t<div></div>\n" +
            "\t\t<div></div>\n" +
            "\t\t<div></div>\n" +
            "\t\t<div></div>\n" +
            "\t\t<div></div>\n" +
            "\t\t<div></div>\n" +
            "\t\t<div></div>\n" +
            "\t\t<div></div>\n" +
            "\t\t<div></div>\n" +
            "\t\t<div></div>\n" +
            "\t\t<div></div>\n" +
            "\t</span>\n" +
            "<style>\n" +
            ".day{\n" +
            "\theight:15px;\n" +
            "\tfont-size:70%;\n" +
            "\ttext-align:center;\n" +
            "}\n" +
            ".day span{\n" +
            "\tpadding-top:2px\n" +
            "}\n" +
            ".day ~ div{\n" +
            "\theight:70px;\t\n" +
            "}\n" +
            "span{\n" +
            "\tvertical-align:top;\n" +
            "\twidth:15.8333%;\n" +
            "\tdisplay:inline-block;\n" +
            "}\n" +
            "div{\n" +
            "\tfont-size:60%;\n" +
            "\tborder-spacing:0px;\n" +
            "\tborder-right:1px solid #dfdfdf;\n" +
            "\tborder-bottom: 1px solid #dfdfdf;\n" +
            "\tdisplay:block;\t\n" +
            "}\n" +
            "body {\n" +
            "\twidth:100%;\n" +
            "\tmargin:0px;\n" +
            "}\n" +
            "#index{\n" +
            "\twidth:5%;\n" +
            "}\n" +
            "#index div{\n" +
            "\ttext-align:center;\n" +
            "}\n" +
            "\n" +
            "*{\n" +
            "\tpadding:0px;\n" +
            "\tmargin:0px;\n" +
            "}\n" +
            "</style>\n" +
            "</body>\n" +
            "</html>";
}
