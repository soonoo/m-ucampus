package com.soonoo.mobilecampus.NewPackage;

import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.soonoo.mobilecampus.AssignViewAdapter;
import com.soonoo.mobilecampus.Controller;
import com.soonoo.mobilecampus.Parser;
import com.soonoo.mobilecampus.R;
import com.soonoo.mobilecampus.Sites;
import com.soonoo.mobilecampus.User;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.ArrayList;


public class AssignmentView extends ActionBarActivity {
    String subCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assignment_view);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Tracker t = ((Controller)getApplication()).getTracker(Controller.TrackerName.APP_TRACKER);
        t.setScreenName("AssignView");
        t.send(new HitBuilders.AppViewBuilder().build());

        subCode = User.subCode.get(getIntent().getIntExtra("subIndex", 1) - 1);
        new GetAssignPage().execute();
    }

    class GetAssignPage extends AsyncTask<Void, Void, String> {
        ProgressBar pb = (ProgressBar)findViewById(R.id.progressbar_downloading);
        @Override
        public void onPreExecute()
        {
            pb.setVisibility(View.VISIBLE);
        }

        @Override
        public String doInBackground(Void... p){
            String html = User.getHtml("POST", Sites.ASSIGNMENT_URL,
                    Parser.getAssignQuery(subCode), "UTF-8");
            return html;
        }
        @Override
        public void onPostExecute(String html){
            Document doc = Jsoup.parse(html);

            ArrayList<String> titleList = new ArrayList<>();
            ArrayList<String> numList = new ArrayList<>();
            ArrayList<Boolean> isSubmit = new ArrayList<>();
            ArrayList<String> due = new ArrayList<>();
            ArrayList<String> due2 = new ArrayList<>();

            for(Element element: doc.select("td.t_c:eq(0)[rowspan=2]")){
                numList.add(element.text());
            }

            for(Element element: doc.select(".link_b2")){
                titleList.add(element.text());
            }

            for(Element element: doc.select("td.t_c:eq(3)")){
                isSubmit.add(element.text().equals("제출"));
            }

            for(Element element: doc.select(".t_l2")){
                due.add(element.nextElementSibling().text());
            }

            for(Element element: doc.select(".t_l2")){
                due2.add(element.parent().nextElementSibling().text());
            }

            pb.setVisibility(View.GONE);

            if(titleList.isEmpty()){
                TextView tv = (TextView)findViewById(R.id.message_no_contents);
                tv.setVisibility(View.VISIBLE);
            }

            AssignViewAdapter adapter = new AssignViewAdapter(titleList, numList, isSubmit, due, due2);
            ListView list = (ListView)findViewById(R.id.assign_list);
            list.setAdapter(adapter);

        }


    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
