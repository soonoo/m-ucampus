package com.soonoo.mobilecampus.mainlist.qna;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.support.design.widget.Snackbar;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.soonoo.mobilecampus.AnalyticsApplication;
import com.soonoo.mobilecampus.R;
import com.soonoo.mobilecampus.Sites;
import com.soonoo.mobilecampus.util.Parser;
import com.soonoo.mobilecampus.util.User;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.net.URLEncoder;

public class QnaArticleView extends AppCompatActivity {
    private Tracker mTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lec_refer_disp);

        AnalyticsApplication application = (AnalyticsApplication) getApplication();
        mTracker = application.getDefaultTracker();
        mTracker.setScreenName("QnaArticle");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        try {
            Intent intent = getIntent();
            TextView sub = (TextView) findViewById(R.id.sub);
            sub.setText(intent.getStringExtra("info"));
            TextView title = (TextView) findViewById(R.id.title);
            title.setText(intent.getStringExtra("title"));

            toolbar.findViewById(R.id.sub).setSelected(true);

            new GetContents().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } catch (Exception e) {
            finish();
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

    public class GetContents extends AsyncTask<Void, Void, Document> {
        Intent intent = getIntent();
        String code = intent.getStringExtra("subIndex");
        String seq = intent.getStringExtra("bdseq");

        @Override
        public Document doInBackground(Void... p) {
            String html = null;
            try {
                html = User.getHtml("POST", Sites.QNA_URL,
                        Parser.getReferViewQuery(code, seq), "utf-8");
            } catch (Exception e) {
                finish();
            }
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
                TextView attatch = new TextView(QnaArticleView.this);
                attatch.setText(element.text());
                attatch.setTextSize(20);
                attatch.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT));
                attatch.setPadding(0, dpT, 0, dpB);
                attatch.setBackgroundResource(R.drawable.attatch_state);
                attatch.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String link = element.attr("href");
                        String title = link.substring(link.indexOf("('") + 2, link.indexOf("','") - 1);
                        String desc = link.substring(link.indexOf("','") + 3, link.indexOf(");") - 1);

                        try {
                            Snackbar.make(findViewById(R.id.refer_view), "다운로드를 시작합니다.", Snackbar.LENGTH_SHORT).show();
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
                            Snackbar.make(findViewById(R.id.refer_view), "다운로드 실패", Snackbar.LENGTH_SHORT).show();
                        }
                    }
                });

                ll.addView(attatch);
            }
        }
    }
}
