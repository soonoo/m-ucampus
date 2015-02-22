package com.soonoo.mobilecampus;

/**
 * Created by soonoo on 2015-02-15.
 */

//TODO:
public class Sites {

    static final String LOGIN_URL = "https://info.kw.ac.kr/webnote/login/login_proc.php";
    static final String LOGIN_QUERY = "login_type=2&redirect_url=http%3A%2F%2Finfo.kw.ac.kr%2F" +
            "&layout_opt=&gubun_code=11&p_language=KOREAN&image.x=19&image.y=18";

    static final String SESSION_URL = "http://info2.kw.ac.kr/servlet/controller.homepage." +
            "MainServlet?p_gate=univ&p_process=main&p_page=learning" +
            "&p_kwLoginType=cookie&gubun_code=11";

    // 과목 정보 초기화용
    static final String MAIN_URL = "http://info2.kw.ac.kr/servlet/controller.homepage.KwuMainServlet" +
            "?p_process=openStu&p_grcode=N000003";
    static final String MAIN_QUERY = "p_process=&p_gate=univ&p_grcode=N000003" +
            "&p_page=&gubun_code=11&p_tutor_name=";

    // 강의 계획서
    static final String SYLLABUS_URL = "http://info.kw.ac.kr/webnote/lecture/h_lecture01_2.php" +
            "?layout_opt=N&engineer_code=&skin_opt=&fsel1_code=&fsel1_str=&fsel2_code=&fsel2_str=" +
            "&fsel2=00_00&fsel3=&fsel4=00_00&hh=&sugang_opt=all&tmp_key=tmp__stu";

    //                                  http://info.kw.ac.kr/webnote/lecture/h_lecture01_2.php
    //       ?layout_opt=N&this_year=2015&hakgi=1&engineer_code=51&skin_opt=&fsel1_code=&fsel1_str=&fsel2_code=&fsel2_str=&fsel2=00_00&fsel3=&fsel4=00_00&sugang_opt=all&tmp_key=tmp__stu&open_major_code=7220&open_grade=2&open_gwamok_no=0819&bunban_no=01

    //장학
    static final String SCHOLARSHIP_URL = "http://info.kw.ac.kr/webnote/janghak/janghak.php" +
            "?layout_opt=N&gubun_code=11&p_gate=univ&p_grcode=N000003&p_page=&p_process=&p_tutor_name=";

    //성적
    static final String GRADE_URL = "http://info.kw.ac.kr/webnote/sungjuk/sungjuk.php" +
            "?layout_opt=N&gubun_code=11&p_gate=univ&p_grcode=N000003&p_page=&p_process=&p_subjseq=1&p_tutor_name=";

    //석차
    static final String RANK_URL = "http://info.kw.ac.kr/webnote/class_order/order.php" +
            "?layout_opt=N&gubun_code=11&p_gate=univ&p_grcode=N000003&p_page=&p_process=&p_tutor_name=";

    //시간표
    static final String TIMETABLE_URL = "http://info.kw.ac.kr/webnote/schedule/schedule.php";

    //중도 좌석
    static final String LIBRARY_SEAT_URL = "http://223.194.18.3/domian/domian5.asp";
}
