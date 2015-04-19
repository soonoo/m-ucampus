package com.soonoo.mobilecampus;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.w3c.dom.Text;

import java.net.URLEncoder;
import java.util.ArrayList;


public class LecReferDispActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lec_refer_disp);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Tracker t = ((Controller)getApplication()).getTracker(Controller.TrackerName.APP_TRACKER);
        t.setScreenName("LecReferDispActivity");
        t.send(new HitBuilders.AppViewBuilder().build());

        Intent intent = getIntent();
        TextView title = (TextView) findViewById(R.id.content_title);
        title.setText(intent.getStringExtra("title"));
        TextView info = (TextView) findViewById(R.id.content_info);
        info.setText(intent.getStringExtra("info"));

        new GetContents().execute();
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

    public class GetContents extends AsyncTask<Void, Void, Document> {
        Intent intent = getIntent();
        String code = intent.getStringExtra("subIndex");
        String seq = intent.getStringExtra("bdseq");

        @Override
        public Document doInBackground(Void... p) {
            String html = User.getHtml("POST", Sites.LEC_REFER_URL,
                    Parser.getReferViewQuery(code, seq), "utf-8");
            return Jsoup.parse(html);
        }

        @Override
        public void onPostExecute(Document document) {
            float scale = getResources().getDisplayMetrics().density;
            int dpT = (int) (3 * scale + 0.5f);
            int dpB = (int) (2 * scale + 0.5f);
            TextView contents = (TextView) findViewById(R.id.content_contents);
            contents.setText(Html.fromHtml(document.select("td.tl_l2").toString()));
            LinearLayout ll = (LinearLayout) findViewById(R.id.attatch_ll);

            for (final Element element : document.select(".link_b2 a")) {
                TextView attatch = new TextView(LecReferDispActivity.this);
                attatch.setText(element.text());
                attatch.setTextSize(20);
                attatch.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT));
                attatch.setPadding(0, dpT, 0, dpB);
                attatch.setBackgroundResource(R.drawable.attatch_state);
                attatch.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Tracker t = ((Controller)getApplication()).getTracker(Controller.TrackerName.APP_TRACKER);
                        t.send(new HitBuilders.EventBuilder().setCategory("LecReferDispActiviy").setAction("Download Button").setLabel("refer").build());
                        String link = element.attr("href");
                        String title = link.substring(link.indexOf("(") + 2, link.indexOf(",") - 1);
                        String desc = link.substring(link.indexOf(",") + 2, link.indexOf(")") - 1);

                        try {
                            Toast.makeText(getApplicationContext(), "다운로드를 시작합니다.", Toast.LENGTH_SHORT).show();
                            String url = Sites.LEC_DOWNLOAD_URL +
                                    "?p_savefile=" + URLEncoder.encode(title, "utf-8") +
                                    "&p_realfile=" + URLEncoder.encode(desc, "utf-8");

                            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));

                            request.setDescription("유캠퍼스");
                            request.addRequestHeader("Cookie", User.cookie);

                            request.setTitle(desc);
                            // in order for this if to run, you must use the android 3.2 to compile your app
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                                request.allowScanningByMediaScanner();
                                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                            }
                            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, desc);

                            // get download service and enqueue file
                            DownloadManager manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
                            manager.enqueue(request);
                        } catch (Exception e) {
                            Toast.makeText(getApplicationContext(), "다운로드 실패", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                ll.addView(attatch);
            }
        }
    }
    @Override
    public void onRestart(){
        super.onRestart();
        new LoginActivity.OnBack().execute();
    }
    @Override
    protected void onStart(){
        super.onStart();
        GoogleAnalytics.getInstance(this).reportActivityStart(this);
    }

    @Override
    protected void onStop(){
        super.onStop();
        GoogleAnalytics.getInstance(this).reportActivityStop(this);
    }
}
