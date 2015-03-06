package com.soonoo.mobilecampus;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;


public class LecReferRoomActivity extends ActionBarActivity {
    ArrayList<String> titleList;
    ArrayList<String> infoList;
    ArrayList<String> codeList;
    String subCode;
    int page = 1;

    int getDp(int px) {
        float scale = getResources().getDisplayMetrics().density;
        int dp = (int) (px * scale + 0.5f);
        return dp;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lec_refer_room);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        subCode = User.subCode.get(getIntent().getIntExtra("subIndex", 1) - 1);
        new GetRefer(subCode, page).execute();

    }

    public class GetRefer extends AsyncTask<Void, Void, Document> {
        String code;
        int page;

        GetRefer(String code, int page) {
            this.code = code;
            this.page = page;
        }

        @Override
        public void onPreExecute(){
            titleList = new ArrayList<>();
            infoList = new ArrayList<>();
            codeList = new ArrayList<>();
        }

        @Override
        public Document doInBackground(Void... p) {
            String html = User.getHtml("POST", Sites.LEC_REFER_URL, Parser.getReferQuery(code, page), "UTF-8");
            return Jsoup.parse(html);
        }

        @Override
        public void onPostExecute(Document document) {

            Elements elements = document.select("samp");
            for(Element element: elements){
                Element parent = element.parent().parent();
                titleList.add(parent.select("a").text());

                String info = "no." + parent.select("td:eq(1)").text() + "  |  " +
                        "등록일: " + parent.select("td:eq(3)").text() + "  |  " +
                        "조회수: " + parent.select("td:eq(5)").text();
                infoList.add(info);

                String code = element.select("a").attr("href");
                codeList.add(code.substring( code.indexOf("(")+2, code.indexOf(",") - 1 ));
            }

            if(elements.size() == 0) findViewById(R.id.message_no_contents).setVisibility(View.VISIBLE);

            ListView listView = (ListView)findViewById(R.id.refer_list);
            MainListAdapter adapter = new MainListAdapter(titleList, infoList);

            findViewById(R.id.progressbar_downloading).setVisibility(View.GONE);

            View footer = getLayoutInflater().inflate(R.layout.footer, null, false);
            LinearLayout ll = (LinearLayout) footer.findViewById(R.id.footer);
            for(final Element numbers: document.select("b:matches(\\[[1-9]\\]), a:matches(\\[[1-9]\\])")){
                final TextView tv = new TextView(LecReferRoomActivity.this);
                tv.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT));
                tv.setText(numbers.text());
                tv.setTextSize(25);
                tv.setPadding(getDp(7), 0, getDp(7), 0);
                tv.setBackgroundResource(R.drawable.attatch_state);

                final String pageFrom = Integer.toString(page);
                final String pageTo = tv.getText().toString().replaceAll("[^1-9]", "");
                if(pageFrom.equals(pageTo)) tv.setEnabled(false);

                tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(!pageFrom.equals(pageTo)) {
                            page = Integer.parseInt(pageTo);
                            new GetRefer(code, Integer.parseInt(pageTo)).execute();
                        }
                    }
                });

                ll.addView(tv);
            }
            listView.removeFooterView(listView.findViewById(R.id.footer));
            listView.addFooterView(footer);
            listView.setAdapter(adapter);
        }


    }

    public class MainListAdapter extends BaseAdapter {
        private ArrayList<String> titleList;
        private ArrayList<String> infoList;

            MainListAdapter(ArrayList<String> titleList, ArrayList<String> infoList) {
                this.titleList = titleList;
                this.infoList = infoList;
        }

        @Override
        public int getCount() {
            return titleList.size();
        }

        @Override
        public Object getItem(int position) {
            return titleList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final int pos = position;
            final Context context = parent.getContext();
            View view = convertView;

            HolderItem holder_item;
            if (view == null) {
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.main_list_item, parent, false);

                holder_item = new HolderItem();
                holder_item.title = (TextView) view.findViewById(R.id.main_title);
                holder_item.subTitle = (TextView) view.findViewById(R.id.main_info);
                view.setTag(holder_item);
            } else {
                holder_item = (HolderItem) view.getTag();
            }

            holder_item.title.setText(titleList.get(pos));
            holder_item.subTitle.setText(infoList.get(pos));

            //터치 이벤트 - 과목별
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(LecReferRoomActivity.this, LecReferDispActivity.class);
                    intent.putExtra("subIndex", subCode)
                          .putExtra("bdseq", codeList.get(pos))
                          .putExtra("title", titleList.get(pos))
                          .putExtra("info", infoList.get(pos));
                    startActivity(intent);
                }
            });
            return view;
        }

        class HolderItem {
            TextView title;
            TextView subTitle;
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