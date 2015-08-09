package com.soonoo.mobilecampus.mainlist.assignment;

import android.content.Context;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.soonoo.mobilecampus.R;
import com.soonoo.mobilecampus.Sites;
import com.soonoo.mobilecampus.util.Parser;
import com.soonoo.mobilecampus.util.User;

import java.util.ArrayList;

/**
 * Created by soonoo on 2015-05-18.
 */
public class AssignViewAdapter extends BaseAdapter {
    ArrayList<String> titleList;
    ArrayList<String> numList;
    ArrayList<String> due;
    ArrayList<String> due2;
    ArrayList<Boolean> isSubmit;
    ArrayList<String> queryList;
    String subCode;

    public AssignViewAdapter(ArrayList<String> titleList, ArrayList<String> numList,
                             ArrayList<Boolean> isSubmit, ArrayList<String> due, ArrayList<String> due2, ArrayList<String> queryList, String subCode) {
        this.titleList = titleList;
        this.numList = numList;
        this.isSubmit = isSubmit;
        this.due = due;
        this.due2 = due2;
        this.queryList = queryList;
        this.subCode = subCode;
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

        Holder holder_item;

        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.assign_view_item, parent, false);

            holder_item = new Holder();
            holder_item.title = (TextView) view.findViewById(R.id.assign_title);
            holder_item.yes = (TextView) view.findViewById(R.id.yess);
            holder_item.no = (TextView) view.findViewById(R.id.nooo);
            holder_item.due = (TextView) view.findViewById(R.id.due);
            holder_item.due2 = (TextView) view.findViewById(R.id.due2);
            view.setTag(holder_item);
        } else {
            holder_item = (Holder) view.getTag();
        }

        if (isSubmit.get(pos)) {
            holder_item.yes.setVisibility(View.VISIBLE);
            holder_item.no.setVisibility(View.GONE);
        } else {
            holder_item.no.setVisibility(View.VISIBLE);
            holder_item.yes.setVisibility(View.GONE);
        }

        holder_item.title.setText(titleList.get(pos));
        holder_item.due.setText("제출기한: " + due.get(pos));
        holder_item.due2.setText("추가제출: " + due2.get(pos));

        return view;
    }

    public void executeAsync(int param){
        System.out.println("aaaaaaaaaaaaaaaaa");
        new GetDetails().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, param);
    }

    class Holder {
        TextView title;
        TextView yes;
        TextView no;
        TextView num;
        TextView due;
        TextView due2;
    }

    class GetDetails extends AsyncTask<Integer, Void, String> {
        public String doInBackground(Integer... p){
            //System.out.println(p[0]);
            String getQuery = Parser.getAssignDetailQuery(subCode) + queryList.get(p[0]);
            return User.getHtml("GET", Sites.ASSIGNMENT_DETAIL_URL + getQuery, "UTF-8");
            //return "WhAT THE";
        }
        public void onPostExecute(String a){
        }
    }
}
