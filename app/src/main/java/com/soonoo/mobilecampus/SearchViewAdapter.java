package com.soonoo.mobilecampus;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by soonoo on 2015-05-23.
 */
public class SearchViewAdapter extends BaseAdapter {
    ArrayList<String> titleList;
    ArrayList<String> infoList;
    ArrayList<String> codeList;

    public SearchViewAdapter(ArrayList<String> titleList, ArrayList<String> infoList, ArrayList<String> codeList) {
        this.titleList = titleList;
        this.infoList = infoList;
        this.codeList = codeList;
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
            view = inflater.inflate(R.layout.search_view_item, parent, false);

            holder_item = new Holder();
            holder_item.title = (TextView) view.findViewById(R.id.search_title);
            holder_item.info = (TextView) view.findViewById(R.id.search_info);
            view.setTag(holder_item);
        } else {
            holder_item = (Holder) view.getTag();
        }

        holder_item.title.setText(titleList.get(pos));
        holder_item.info.setText(infoList.get(pos));

        view.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent intent = new Intent(context, SyllabusView.class);
                                        intent.putExtra("query", codeList.get(pos));
                                        context.startActivity(intent);
                                    }
                                }
        );

        return view;
    }

    class Holder {
        TextView title;
        TextView info;
    }

}
