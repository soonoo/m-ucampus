package com.soonoo.mobilecampus.board;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.soonoo.mobilecampus.R;
import com.soonoo.mobilecampus.board.article.BoardArticleView;

import org.w3c.dom.Text;

import java.lang.reflect.GenericArrayType;
import java.util.ArrayList;

/**
 * Created by SOONOOh on 2015-07-30.
 */
public class BoardHomeViewAdpater extends BaseAdapter {
    private ArrayList<String> titleList;
    private ArrayList<String> infoList;
    private ArrayList<Integer> idList;
    private ArrayList<Integer> countList;
    private ArrayList<Integer> viewList;

    BoardHomeViewAdpater(ArrayList<String> titleList, ArrayList<String> infoList, ArrayList<Integer> idList,
                         ArrayList<Integer> countList, ArrayList<Integer> viewList) {
        this.titleList = titleList;
        this.infoList = infoList;
        this.idList = idList;
        this.countList = countList;
        this.viewList = viewList;
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
        String titleText;

        HolderItem holder;

        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.board_list_item, parent, false);

            holder = new HolderItem();
            holder.title = (TextView) view.findViewById(R.id.board_list_title);
            holder.info = (TextView) view.findViewById(R.id.board_list_info);
            holder.reply_count = (TextView) view.findViewById(R.id.reply_count);
            view.setTag(holder);
        } else {
            holder = (HolderItem) view.getTag();
        }

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, BoardArticleView.class);
                intent.putExtra("id", idList.get(pos));
                intent.putExtra("pos", pos);
                context.startActivity(intent);
                return;
            }
        });

        holder.title.setText(titleList.get(pos));
        holder.info.setText(infoList.get(pos));
        if(holder.info.getText().toString().contains("관리자") || holder.info.getText().toString().contains("admin")) {
            view.setBackgroundColor(context.getResources().getColor(R.color.b5));
        }else{
            view.setBackgroundResource(R.drawable.main_list_state);
        }
        holder.reply_count.setText("조회:" + viewList.get(pos) + " 댓글:" + countList.get(pos));

        return view;
    }

    class HolderItem {
        TextView title;
        TextView info;
        TextView reply_count;
    }
}
