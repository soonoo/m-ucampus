package com.soonoo.mobilecampus;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.AlphaAnimation;
import android.widget.LinearLayout;
import android.widget.ListView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;


public class MainActivity extends ActionBarActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView mainList = (ListView) findViewById(R.id.main_list);

        try {
            ArrayList<String> infoList = new InitMainList().execute().get();
            ArrayList<String> titleList = (ArrayList<String>)User.subName.clone();
            int size = infoList.size();

            titleList.add(0, "과목별 조회");
            titleList.add("성적/장학 조회");
            titleList.add("성적/석차 조회");
            titleList.add("장학 조회");

            infoList.add(0, "");
            infoList.add("");
            infoList.add("학기별 성적/석차를 확인합니다.");
            infoList.add("장학금 수혜 현황을 확인합니다.");

            MainListAdapter adapter = new MainListAdapter(titleList, infoList, size + 1);
            mainList.setAdapter(adapter);
        }catch (Exception e){
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            String exceptionAsStrting = sw.toString();
            Log.e("INFO", exceptionAsStrting);
            e.printStackTrace();
        }
    }


    public class InitMainList extends AsyncTask<Void, Void, ArrayList<String>>{
        @Override
        public ArrayList<String> doInBackground(Void... p){
            ArrayList<String> infoList = new ArrayList<String>();

            SharedPreferences prefs = getSharedPreferences("main_list", 0);
            SharedPreferences.Editor editor = prefs.edit();

            String mainHtml = User.getHtml("POST", Sites.MAIN_URL, Sites.MAIN_QUERY, "utf-8");
            Document mainDoc = Jsoup.parse(mainHtml);
            User.subCode = Parser.getSubCode(mainDoc);
            User.subName = Parser.getSubName(mainDoc);

            for(String sub: User.subCode){
                String info;
                if((info = prefs.getString(sub, null)) != null) {
                    infoList.add(info);
                    continue;
                }else {
                    String sylHtml = User.getHtml("GET", Sites.SYLLABUS_URL + Parser.getSubQuery(sub), "euc-kr");
                    Document sylDoc = Jsoup.parse(sylHtml);

                    String prof = Parser.getProf(sylDoc);
                    String time = Parser.getTime(sylDoc);

                    info = prof + " | " + time;
                    infoList.add(info);
                    editor.putString(sub, info);
                    editor.commit();
                }
            }
            return infoList;
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        menu.add(0, 1, 0, "로그아웃"); //int groupId, int itemId, int order, CharSequence title
        menu.add(0,2,0,"종료"); //int groupId, int itemId, int order, CharSequence title
        menu.add(0,3,0,"정보"); //int groupId, int itemId, int order, CharSequence title
        return true;
    }


    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case 1:
                LogoutDialog dialog_logout = new LogoutDialog(this);
                dialog_logout.show();
                return true;
            case 2:
                finish();
                return true;
            case 3:
                NoticeDialog dialog_notice = new NoticeDialog(this);
                dialog_notice.show();
                return true;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }
}
