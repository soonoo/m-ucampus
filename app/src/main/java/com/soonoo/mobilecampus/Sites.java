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

}
