package com.soonoo.mobilecampus;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.net.URLEncoder;
import java.util.ArrayList;


public class LectureSearchView extends AppCompatActivity implements View.OnClickListener {
    Spinner[] spinners = new Spinner[4];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lecture_search);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Tracker t = ((Controller)getApplication()).getTracker(Controller.TrackerName.APP_TRACKER);
        t.setScreenName("LectureSearchView");
        t.send(new HitBuilders.AppViewBuilder().build());

        new GetSearchPage().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

        Button button = (Button) findViewById(R.id.button_search);
        button.setOnClickListener(this);
    }

    class GetSearchPage extends AsyncTask<Void, Void, String> {
        Button button = (Button) findViewById(R.id.button_search);
        @Override

        public void onPreExecute() {
            button.setClickable(false);
            button.setEnabled(false);
        }

        @Override
        public String doInBackground(Void... p) {
            return User.getHtml("GET", Sites.LECTURE_SEARCH, "EUC-KR");
        }

        @Override
        public void onPostExecute(String html) {
            Document doc = Jsoup.parse(html);
            String[] names = {"this_year", "hakgi", "fsel1", "fsel2"};
            String[] prompts = {"년도", "학기", "단과 대학", "학과"};
            int[] viewId = {R.id.spinner_year, R.id.spinner_term, R.id.spinner_college, R.id.spinner_major};


            for (int i = 0; i < names.length; i++) {
                ArrayList<LecSearchViewItem> list = new ArrayList();
                spinners[i] = (Spinner) findViewById(viewId[i]);

                ArrayAdapter adapter = new ArrayAdapter(LectureSearchView.this, android.R.layout.simple_spinner_dropdown_item,
                        list);

                for (Element element : doc.select("select[name=" + names[i] + "] option")) {
                    list.add(new LecSearchViewItem(element.text(), element.attr("value")));
                }
                spinners[i].setAdapter(adapter);
                spinners[i].setPrompt(prompts[i]);
            }
            button.setEnabled(true);
            button.setClickable(true);
        }
    }

    class GetList extends AsyncTask<String, Void, Void> {
        ProgressBar pb = (ProgressBar) findViewById(R.id.pb_search);
        Button button = (Button) findViewById(R.id.button_search);
        TextView tv = (TextView) findViewById(R.id.noData);
        ListView list = (ListView)findViewById(R.id.search_list);

        ArrayList<String> titleList = new ArrayList<>();
        ArrayList<String> infoList = new ArrayList<>();
        ArrayList<String> codeList = new ArrayList<>();

        @Override
        public void onPreExecute() {
            list.setVisibility(View.GONE);
            tv.setVisibility(View.GONE);
            pb.setVisibility(View.VISIBLE);
            button.setClickable(false);
        }

        @Override
        public Void doInBackground(String... p) {
            String html =  User.getHtml("POST", Sites.LECTURE_SEARCH + p[0], "euc-kr");
            Document doc = Jsoup.parse(html);
            for(Element element: doc.select("a[href^=h_lecture01_2]")){
                Element parent = element.parent().parent();
                infoList.add(parent.select("td:eq(5)").text() + " | " +
                        parent.select("td:eq(4)").text() + "(학점/시간) | "  +
                        parent.select("td:eq(0)").text().charAt(5) + "(개설학년) | " +
                        parent.select("td:eq(3)").text());
                titleList.add(parent.select("td:eq(1)").text() + " (" + parent.select("td:eq(3)").text() + ")");
                codeList.add(parent.select("td:eq(1) a").attr("href").substring(30, 120));
            }
            return null;
        }

        @Override
        public void onPostExecute(Void p) {

            if(titleList.isEmpty()) {
                tv.setVisibility(View.VISIBLE);
                pb.setVisibility(View.GONE);
                button.setClickable(true);
                return;
            }

            SearchViewAdapter adapter = new SearchViewAdapter(titleList, infoList, codeList);

            list.setAdapter(adapter);
            list.setVisibility(View.VISIBLE);
            pb.setVisibility(View.GONE);
            button.setClickable(true);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_search:
                Button button = (Button) findViewById(R.id.button_search);
                InputMethodManager imm = (InputMethodManager)getSystemService(
                        Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(button.getWindowToken(), 0);

                EditText sub = (EditText) findViewById(R.id.text_sub); //hh
                EditText prof = (EditText) findViewById(R.id.text_prof); //prof_name
                LecSearchViewItem item1 = (LecSearchViewItem) spinners[0].getSelectedItem();
                LecSearchViewItem item2 = (LecSearchViewItem) spinners[1].getSelectedItem();
                LecSearchViewItem item3 = (LecSearchViewItem) spinners[2].getSelectedItem();
                LecSearchViewItem item4 = (LecSearchViewItem) spinners[3].getSelectedItem();

                try {
                    String query = "&mode=view&user_opt=&skin_opt=&show_hakbu=&sugang_opt=all&x=29&y=18" +
                            "&hh=" + URLEncoder.encode(sub.getText().toString(), "EUC-KR") +
                            "&prof_name=" + URLEncoder.encode(prof.getText().toString(), "EUC-KR") +
                            "&fsel1=" + URLEncoder.encode(item3.value, "euc-kr") +
                            "&this_year=" + item1.value +
                            "&hakgi=" + item2.value +
                            "&fsel2=" + URLEncoder.encode(item4.value, "euc-kr") +
                            "&fsel4=00_00";

                    new GetList().execute(query);


                } catch (Exception e) {
                    e.printStackTrace();
                }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}