package com.soonoo.mobilecampus.mainlist.library;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.soonoo.mobilecampus.Controller;
import com.soonoo.mobilecampus.R;
import com.soonoo.mobilecampus.Sites;
import com.soonoo.mobilecampus.util.User;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;


public class LibrarySeatInfoView extends AppCompatActivity {
    WebView wv;
    Document document = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //overridePendingTransition(R.anim.slide_up, R.anim.slide_down);
        setContentView(R.layout.activity_library_seat_info);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Tracker t = ((Controller)getApplication()).getTracker(Controller.TrackerName.APP_TRACKER);
        t.setScreenName("LibrarySeatInfoActivity");
        t.send(new HitBuilders.AppViewBuilder().build());

        new GetSeatHtml().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    public class GetSeatHtml extends AsyncTask<Void, Void, Document>  {
        ProgressBar pb = (ProgressBar)findViewById(R.id.progressbar_downloading);

        public void onPreExecute(){
            pb.setVisibility(View.VISIBLE);
        }

        public Document doInBackground(Void... p){
            String seatHtml = User.getHtml("GET", Sites.LIBRARY_SEAT_URL, "euc-kr");
            return Jsoup.parse(seatHtml);
        }

        @Override
        public void onPostExecute(Document doc){
            document = doc;
            try {
                document.select("table").attr("width", "100%").attr("style", "font-size:80%;");
                document.select("tr:eq(1) > td:eq(1)").attr("width", "23%");
                document.select("br + font").first().text("정보 갱신 간격은 1분입니다.");
                //document.select("a").unwrap();


                for(Element element: document.select("a:not(img)")){
                    element.attr("href", "http://223.194.18.3/domian/" + element.attr("href"));
                }
                wv = (WebView) findViewById(R.id.webview_library);
                wv.loadDataWithBaseURL("", document.toString(), "text/html", "utf-8", "");
                wv.getSettings().setDefaultTextEncodingName("euc-kr");
                wv.setWebViewClient(new WebViewClient() {
                    @Override
                    public boolean shouldOverrideUrlLoading(WebView view, String url) {
                        return false;
                    }
                });
                wv.getSettings().setBuiltInZoomControls(true);
                wv.getSettings().setDisplayZoomControls(false);
            }catch(Exception e){
                wv = (WebView) findViewById(R.id.webview_library);
                wv.loadUrl(Sites.LIBRARY_SEAT_URL);
                wv.getSettings().setJavaScriptEnabled(true);
                wv.getSettings().setDefaultTextEncodingName("euc-kr");

                wv.getSettings().setBuiltInZoomControls(true);
                wv.getSettings().setDisplayZoomControls(false);
            }
            pb.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // Check if the key event was the Back button and if there's history
        if ((keyCode == KeyEvent.KEYCODE_BACK) && wv.getUrl().contains("room_no=")) {
            new GetSeatHtml().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            return true;
        }
        // If it wasn't the Back key or there's no web page history, bubble up to the default
        // system behavior (probably exit the activity)
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_seat_info, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.actionbar_refresh:
               // if(wv.getUrl().contains())
                    new GetSeatHtml().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                Toast.makeText(getApplicationContext(), getString(R.string.message_refreshed), Toast.LENGTH_SHORT).show();
                return true;
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void finish() {
        super.finish();
        wv.clearCache(true);
        //overridePendingTransition(R.anim.slide_up, R.anim.slide_down);
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
