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
 * Created by soonoo on 2015-02-15.
 */
public class HomeViewAdapter extends BaseAdapter{
    private ArrayList<String> titleList;
    private ArrayList<String> infoList;
    private ArrayList<Boolean> isNew;

    private final int TYPE_SEPARATOR = 0;
    private final int TYPE_ITEM = 1;

    private int separatorIndex;

    HomeViewAdapter(ArrayList<String> titleList, ArrayList<String> infoList, ArrayList<Boolean> isNew, int index){
        this.titleList = titleList;
        this.infoList = infoList;
        this.isNew = isNew;
        this.separatorIndex = index;
    }

    @Override
    public int getCount(){
        return titleList.size();
    }

    @Override
    public Object getItem(int position){
        return titleList.get(position);
    }

    @Override
    public long getItemId(int position){
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        final int pos = position;
        final Context context = parent.getContext();
        int viewType = getItemViewType(position);
        View view = convertView;

        switch(viewType){
            case TYPE_SEPARATOR:
                HolderSeparator holder_sep;

                if(view == null){
                    LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    view = inflater.inflate(R.layout.main_list_separator, parent, false);

                    holder_sep = new HolderSeparator();
                    holder_sep.separator = (TextView) view.findViewById(R.id.main_separator);
                    view.setTag(holder_sep);
                }else{
                    holder_sep = (HolderSeparator)view.getTag();
                }

                holder_sep.separator.setText(titleList.get(pos));
                return view;


            case TYPE_ITEM:
                HolderItem holder_item;

                if(view == null){
                    LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    view = inflater.inflate(R.layout.main_list_item, parent, false);

                    holder_item = new HolderItem();
                    holder_item.title = (TextView) view.findViewById(R.id.main_title);
                    holder_item.subTitle = (TextView) view.findViewById(R.id.main_info);
                    holder_item.newNotice = (TextView) view.findViewById(R.id.new_notice);
                    view.setTag(holder_item);
                }else{
                    holder_item = (HolderItem) view.getTag();
                }

                if(0 < pos && pos <= User.subName.size() && isNew.get(pos-1)) holder_item.newNotice.setVisibility(View.VISIBLE);
                else holder_item.newNotice.setVisibility(View.GONE);

                holder_item.title.setText(titleList.get(pos));
                holder_item.subTitle.setText(infoList.get(pos));

                //터치 이벤트 - 과목별
                view.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v){
                        if(pos < User.subName.size() + 1) {
                            new HomeViewDialog(context, pos).show();
                        } else{
                            int index = pos - User.subCode.size();
                            Intent intent;
                            switch(index){
                                case 2:
                                    intent = new Intent(context, GradeView.class);
                                    context.startActivity(intent);
                                    break;
                                case 3:
                                    intent = new Intent(context, ScholarshipActivity.class);
                                    v.getContext().startActivity(intent);
                                    break;
                                case 5:
                                    new LibraryMenuDialog(context).show();
                                    //intent = new Intent(context, LibrarySeatInfoActivity.class);
                                    //v.getContext().startActivity(intent);
                                    break;
                                case 6:
                                    //context.startActivity(new Intent(context, DiningActivity.class));
                                    intent = new Intent(context, LectureSearchView.class);
                                    context.startActivity(intent);
                                    break;
                            }
                        }
                    }
                });
                return view;

            default:
                return view;
        }

    }

    public int getViewTypeCount(){
        return 2;
    }

    public int getItemViewType(int position){
        if(position == 0 || position == separatorIndex || position == separatorIndex + 3) return TYPE_SEPARATOR;
        else return TYPE_ITEM;
    }

    class HolderItem{
        TextView title;
        TextView subTitle;
        TextView newNotice;
    }
    class HolderSeparator{
        TextView separator;
    }
}
