package com.soonoo.mobilecampus.board.article;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.soonoo.mobilecampus.R;

import java.util.ArrayList;

/**
 * Created by SOONOOh on 2015-08-03.
 */
public class BoardReplyAdapter extends BaseAdapter {
    ArrayList<String> contentList;
    ArrayList<String> dateList;

    BoardReplyAdapter(ArrayList<String> contentList, ArrayList<String> dateList){
        this.contentList = contentList;
        this.dateList = dateList;
    }

    public int getCount(){
        return contentList.size();
    }

    public Object getItem(int position){
        return contentList.get(position);
    }

    public long getItemId(int position){
        return position;
    }

    public View getView(int position, View view, ViewGroup parentView){
        Holder holder;

        if(view == null){
            LayoutInflater inflater = (LayoutInflater) parentView.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.board_article_reply_item, null, false);

            holder = new Holder();
            holder.content = (TextView) view.findViewById(R.id.reply_content);
            holder.date = (TextView) view.findViewById(R.id.reply_date);
            view.setTag(holder);
        }else{
            holder = (Holder) view.getTag();
        }

        holder.content.setText(contentList.get(position));
        holder.date.setText(dateList.get(position));

        return view;
    }


    class Holder{
        TextView content;
        TextView date;
    }
}
