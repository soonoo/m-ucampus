package com.soonoo.mobilecampus.mainlist.notice;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.analytics.GoogleAnalytics;
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
import org.jsoup.select.Elements;

import java.net.URLEncoder;


public class NoticeArticleView extends AppCompatActivity {
    private Tracker mTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice_view);

        Intent intent1 = getIntent();


        AnalyticsApplication application = (AnalyticsApplication) getApplication();
        mTracker = application.getDefaultTracker();
        if (intent1.getBooleanExtra("isStuReferView", false)) {
            mTracker.setScreenName("StudentReferArticle");
        } else {
            mTracker.setScreenName("NoticeArticle");
        }
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
        String seq = intent.getStringExtra("bdseq");

        @Override
        public Document doInBackground(Void... p) {
            String html = null;
            try {
                if (intent.getBooleanExtra("isStuReferView", false)) {
                    html = User.getHtml("POST", Sites.STUDENT_REFER_URL,
                            Parser.getNoticeViewQuery(seq), "utf-8");
                } else {
                    html = User.getHtml("POST", Sites.NOTICE_URL,
                            Parser.getNoticeViewQuery(seq), "utf-8");
                }
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
            // TextView contents = (TextView) findViewById(R.id.content_contents);
            //  contents.setText(Html.fromHtml(document.select("td.tl_l2").toString()));

            WebView wv = (WebView) findViewById(R.id.wv_notice_view);
            String baseUrl = "http://info2.kw.ac.kr/servlet/controller.learn.NoticeStuServlet";
            String baseTag = "<base href=\"" + baseUrl + "\">";

            wv.loadDataWithBaseURL("", baseTag + document.select("td.tl_l2").toString(), "text/html", "euc-kr", "");

            LinearLayout ll = (LinearLayout) findViewById(R.id.attatch_ll);

            Elements elements = document.select(".link_b2 a");
            for (final Element element : elements) {
                TextView attatch = new TextView(NoticeArticleView.this);
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
                        String title = link.substring(link.indexOf("(") + 2, link.indexOf(",") - 1);
                        String desc = link.substring(link.indexOf(",") + 2, link.indexOf(");") - 1);

                        try {
                            Snackbar.make(findViewById(R.id.notice_view), "다운로드를 시작합니다.", Snackbar.LENGTH_SHORT).show();
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
                            Snackbar.make(findViewById(R.id.notice_view), "다운로드를 실패.", Snackbar.LENGTH_SHORT).show();
                        }
                    }
                });

                ll.addView(attatch);
            }

            if (elements.size() == 0) {
                TextView attatch = new TextView(NoticeArticleView.this);
                attatch.setText("첨부파일이 없습니다.");
                attatch.setTextSize(20);
                attatch.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT));
                attatch.setPadding(0, dpT, 0, dpB);
                ll.addView(attatch);
            }
        }
    }
}
