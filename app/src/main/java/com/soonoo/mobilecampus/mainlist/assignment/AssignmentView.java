package com.soonoo.mobilecampus.mainlist.assignment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.soonoo.mobilecampus.Controller;
import com.soonoo.mobilecampus.R;
import com.soonoo.mobilecampus.Sites;
import com.soonoo.mobilecampus.util.Parser;
import com.soonoo.mobilecampus.util.User;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.ArrayList;


public class AssignmentView extends ActionBarActivity {
    String subCode;
    TextView message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assignment_view);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Tracker t = ((Controller) getApplication()).getTracker(Controller.TrackerName.APP_TRACKER);
        t.setScreenName("AssignView");
        t.send(new HitBuilders.AppViewBuilder().build());

        try {
            subCode = User.subCode.get(getIntent().getIntExtra("subIndex", 1) - 1);
            new GetAssignPage().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } catch (Exception e) {
            finish();
        }
    }

    class GetAssignPage extends AsyncTask<Void, Void, String> {
        ProgressBar pb = (ProgressBar) findViewById(R.id.progressbar_downloading);

        @Override
        public void onPreExecute() {
            pb.setVisibility(View.VISIBLE);
        }

        @Override
        public String doInBackground(Void... p) {
            String html = null;
            try{
                html = User.getHtml("POST", Sites.ASSIGNMENT_URL,
                        Parser.getAssignQuery(subCode), "UTF-8");
            }catch (Exception e){
                finish();
            }
            return html;
        }

        @Override
        public void onPostExecute(String html) {
            Document doc = Jsoup.parse(html);

            final ArrayList<String> titleList = new ArrayList<>();
            ArrayList<String> numList = new ArrayList<>();
            ArrayList<Boolean> isSubmit = new ArrayList<>();
            ArrayList<String> due = new ArrayList<>();
            ArrayList<String> due2 = new ArrayList<>();
            final ArrayList<String> queryList = new ArrayList<>();

            for (Element element : doc.select("td.t_c:eq(0)[rowspan=2]")) {
                numList.add(element.text());
            }

            for (Element element : doc.select(".link_b2")) {
                titleList.add(element.text());
            }

            for (Element element : doc.select("td.t_c:eq(3)")) {
                isSubmit.add(element.text().equals("제출"));
            }

            for (Element element : doc.select(".t_l2")) {
                due.add(element.nextElementSibling().text());
                due2.add(element.parent().nextElementSibling().text());
            }

            /*for(Element element: doc.select("td.btn_b012:contains(조회)")){
                String query;
                System.out.println(element);
                ArrayList<String> jsCodeList = new ArrayList(Arrays.asList(element.attr("onclick").toString().split(",")));
                System.out.println(jsCodeList);
                query = "&p_ordseq=" + jsCodeList.get(0).replaceAll("^[0-9]","");
                query = query + "&p_weeklyseq=" + jsCodeList.get(1).replaceAll("^[0-9]","");
                query = query + "&p_weekysubseq=" + jsCodeList.get(2).replaceAll("^[0-9]","");

                queryList.add(query);
            }*/

            pb.setVisibility(View.GONE);

            if (titleList.isEmpty()) {
                TextView tv = (TextView) findViewById(R.id.message_no_contents);
                tv.setVisibility(View.VISIBLE);
            }

            AssignViewAdapter adapter = new AssignViewAdapter(titleList, numList, isSubmit, due, due2, queryList, subCode);
            ListView list = (ListView) findViewById(R.id.assign_list);
            /*list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> av, View view, int i, long l) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(AssignmentView.this, R.style.Theme_AppCompat_Light_Dialog_Alert);
                    AlertDialog dialog = builder.create();
                    dialog.setTitle(titleList.get(i));

                    message = (TextView) dialog.findViewById(android.R.id.message);
                    dialog.show();

                    new GetDetails().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, queryList.get(i));
                    message.setText("PLEASE>>>>");

                }
            });*/

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

    class GetDetails extends AsyncTask<String, Void, String> {
        public void onPreExecute() {
            System.out.println("aaaaaaaaaaaaa");
        }

        public String doInBackground(String... p) {
            //System.out.println(p[0]);
            String getQuery = Parser.getAssignDetailQuery(subCode) + p[0];
            return User.getHtml("GET", Sites.ASSIGNMENT_DETAIL_URL + getQuery, "UTF-8");
            //return "WhAT THE";
        }

        public void onPostExecute(String a) {
            System.out.println(a);
            message.setText(a);
        }
    }
}
