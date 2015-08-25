package com.soonoo.mobilecampus.board;

import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.soonoo.mobilecampus.R;
import com.soonoo.mobilecampus.board.article.BoardArticleView;

import java.lang.reflect.GenericArrayType;
import java.util.ArrayList;

/**
 * Created by SOONOOh on 2015-07-30.
 */
public class BoardHomeViewAdpater extends BaseAdapter {
    private ArrayList<String> titleList;
    private ArrayList<String> infoList;
    private ArrayList<Integer> idList;

    BoardHomeViewAdpater(ArrayList<String> titleList, ArrayList<String> infoList, ArrayList<Integer> idList){
        this.titleList = titleList;
        this.infoList = infoList;
        this.idList = idList;
    }

    @Override
    public int getCount(){ return titleList.size(); }

    @Override
    public Object getItem(int position){
        return titleList.get(position);
    }

    @Override
    public long getItemId(int position){
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();
        View view = convertView;


        HolderItem holder;

        if(view == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.board_list_item, parent, false);

            holder = new HolderItem();
            holder.title = (TextView) view.findViewById(R.id.board_list_title);
            holder.info = (TextView) view.findViewById(R.id.board_list_info);
            view.setTag(holder);
        }else{
            holder = (HolderItem)view.getTag();
        }

        view.setOnClickListener( new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(context, BoardArticleView.class);
                intent.putExtra("id", idList.get(pos));
                intent.putExtra("pos", pos);
                context.startActivity(intent);
                return;
            }
        });

        holder.title.setText(Html.fromHtml(titleList.get(pos)));
        holder.info.setText(infoList.get(pos));
        return view;
    }

    class HolderItem{
        TextView title;
        TextView info;
    }
}
