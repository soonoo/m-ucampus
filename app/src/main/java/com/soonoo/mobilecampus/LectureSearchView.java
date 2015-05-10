package com.soonoo.mobilecampus;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.BootstrapEditText;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.net.URLDecoder;
import java.security.spec.ECField;
import java.util.ArrayList;
import java.util.List;


public class LectureSearchView extends ActionBarActivity implements View.OnClickListener {
    Spinner[] spinners = new Spinner[4];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lecture_search);

        new GetSearchPage().execute();

        Button button = (Button) findViewById(R.id.button_search);
        button.setOnClickListener(this);
    }

    class GetSearchPage extends AsyncTask<Void, Void, String> {
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
        }
    }

    class GetList extends AsyncTask<String, Void, String> {
        ProgressBar pb = (ProgressBar) findViewById(R.id.pb_search);
        Button button = (Button) findViewById(R.id.button_search);
        TextView tv = (TextView) findViewById(R.id.noData);

        @Override
        public void onPreExecute() {
            tv.setVisibility(View.GONE);
            pb.setVisibility(View.VISIBLE);
            button.setClickable(false);
        }

        @Override
        public String doInBackground(String... p) {
            return User.getHtml("POST", Sites.LECTURE_SEARCH + p[0], "euc-kr");
        }

        @Override
        public void onPostExecute(String html) {
            Element table = Jsoup.parse(html).select("table:contains(과목코드)").first();
            table.attr("width", "100%").attr("style", "font-size:70%;");
            table.select("col:eq(5), col:eq(0), col:eq(1), col:eq(2), col:eq(9)").remove();
            for (Element el : table.select("td:eq(5), td:eq(2), td:eq(1), td:eq(0), td:eq(9)")) {
                el.remove();
            }
            for (Element el : table.select("td")) {
                el.attr("style", "white-space:nowrap;");
            }

            WebView wv = (WebView) findViewById(R.id.wv_search_result);
            wv.loadDataWithBaseURL("", table.toString(), "text/html", "euc-kr", "");
            wv.setWebViewClient(new WebViewClient() {
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    if (url.startsWith("h_lecture")) {
                        System.out.println(url);

                        Intent intent = new Intent(LectureSearchView.this, SyllabusView.class);
                        intent.putExtra("url", url);
                        startActivity(intent);
                    }
                    return true;
                }
            });
            pb.setVisibility(View.GONE);
            button.setClickable(true);

            if (html.contains("없습니다")) tv.setVisibility(View.VISIBLE);
            else tv.setVisibility(View.GONE);

        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_search:
                EditText sub = (EditText) findViewById(R.id.text_sub); //hh
                EditText prof = (EditText) findViewById(R.id.text_prof); //prof_name
                LecSearchViewItem item1 = (LecSearchViewItem) spinners[0].getSelectedItem();
                LecSearchViewItem item2 = (LecSearchViewItem) spinners[1].getSelectedItem();
                LecSearchViewItem item3 = (LecSearchViewItem) spinners[2].getSelectedItem();
                LecSearchViewItem item4 = (LecSearchViewItem) spinners[3].getSelectedItem();

                try {
                    String query = "&mode=view&user_opt=&skin_opt=&show_hakbu=&sugang_opt=all&x=29&y=18" +
                            "&hh=" + URLDecoder.decode(sub.getText().toString(), "EUC-KR") +
                            "&prof_name=" + URLDecoder.decode(prof.getText().toString(), "EUC-KR") +
                            "&fsel1=" + URLDecoder.decode(item3.value, "euc-kr") +
                            "&this_year=" + item1.value +
                            "&hakgi=" + item2.value +
                            "&fsel2=" + URLDecoder.decode(item4.value, "euc-kr") +
                            "&fsel4=00_00";

                    new GetList().execute(query);


                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("please");
                }
        }
    }


}
