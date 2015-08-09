package com.soonoo.mobilecampus.mainlist;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.soonoo.mobilecampus.R;
import com.soonoo.mobilecampus.mainlist.assignment.AssignmentView;
import com.soonoo.mobilecampus.mainlist.grade.GradeView;
import com.soonoo.mobilecampus.mainlist.library.LibrarySearchView;
import com.soonoo.mobilecampus.mainlist.library.LibrarySeatInfoView;
import com.soonoo.mobilecampus.mainlist.notice.NoticeView;
import com.soonoo.mobilecampus.mainlist.refer.ReferView;
import com.soonoo.mobilecampus.mainlist.scholarship.ScholarshipActivity;
import com.soonoo.mobilecampus.mainlist.search.LectureSearchView;
import com.soonoo.mobilecampus.mainlist.syllabus.SyllabusView;
import com.soonoo.mobilecampus.util.User;

import java.util.ArrayList;

/**
 * Created by soonoo on 2015-02-15.
 */
public class HomeViewAdapter extends BaseAdapter {
    private ArrayList<String> titleList;
    private ArrayList<String> infoList;
    private ArrayList<Boolean> isNew;

    private final int TYPE_SEPARATOR = 0;
    private final int TYPE_ITEM = 1;

    private int separatorIndex;

    HomeViewAdapter(ArrayList<String> titleList, ArrayList<String> infoList, ArrayList<Boolean> isNew, int index) {
        this.titleList = titleList;
        this.infoList = infoList;
        this.isNew = isNew;
        this.separatorIndex = index;
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
        int viewType = getItemViewType(position);
        View view = convertView;

        switch (viewType) {
            case TYPE_SEPARATOR:
                HolderSeparator holder_sep;

                if (view == null) {
                    LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    view = inflater.inflate(R.layout.main_list_separator, parent, false);

                    holder_sep = new HolderSeparator();
                    holder_sep.separator = (TextView) view.findViewById(R.id.main_separator);
                    view.setTag(holder_sep);
                } else {
                    holder_sep = (HolderSeparator) view.getTag();
                }

                holder_sep.separator.setText(titleList.get(pos));
                return view;

            case TYPE_ITEM:
                HolderItem holder_item;

                if (view == null) {
                    LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    view = inflater.inflate(R.layout.main_list_item, parent, false);

                    holder_item = new HolderItem();
                    holder_item.title = (TextView) view.findViewById(R.id.main_title);
                    holder_item.subTitle = (TextView) view.findViewById(R.id.main_info);
                    holder_item.newNotice = (TextView) view.findViewById(R.id.new_notice);
                    view.setTag(holder_item);
                } else {
                    holder_item = (HolderItem) view.getTag();
                }

                if (0 < pos && pos <= User.subName.size() && isNew.get(pos - 1))
                    holder_item.newNotice.setVisibility(View.VISIBLE);
                else holder_item.newNotice.setVisibility(View.GONE);

                holder_item.title.setText(titleList.get(pos));
                holder_item.subTitle.setText(infoList.get(pos));
                final CharSequence[] ss = {"공지사항", "강의 자료실", "강의계획서 조회", "과제 조회"};

                //터치 이벤트 - 과목별
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (pos < User.subName.size() + 1) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.Theme_AppCompat_Light_Dialog_Alert);
                            builder.setTitle(User.subName.get(pos - 1));
                            builder.setItems(ss, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Intent intent;
                                    switch(i) {
                                        case 0:
                                            intent = new Intent(context, NoticeView.class);
                                            intent.putExtra("subIndex", pos);
                                            context.startActivity(intent);
                                            return;
                                        case 1:
                                            intent = new Intent(context, ReferView.class);
                                            intent.putExtra("subIndex", pos);
                                            context.startActivity(intent);
                                            return;
                                        case 2:
                                            intent = new Intent(context, SyllabusView.class);
                                            intent.putExtra("subIndex", pos);
                                            context.startActivity(intent);
                                            return;
                                        case 3:
                                            intent = new Intent(context, AssignmentView.class);
                                            intent.putExtra("subIndex", pos);
                                            context.startActivity(intent);
                                    }
                                } //#########
                            });
                            builder.show();
                            //new HomeViewDialog(context, pos).show();
                        } else {
                            int index = pos - User.subCode.size();
                            Intent intent;
                            switch (index) {
                                case 2:
                                    intent = new Intent(context, GradeView.class);
                                    context.startActivity(intent);
                                    break;
                                case 3:
                                    intent = new Intent(context, ScholarshipActivity.class);
                                    v.getContext().startActivity(intent);
                                    break;
                                case 5:
                                    CharSequence[] ss = {"열람실 좌석 현황", "도서 검색"};
                                    AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.Theme_AppCompat_Light_Dialog_Alert);
                                    builder.setTitle("중앙도서관");
                                    builder.setItems(ss, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            Intent intent;
                                            switch(i) {
                                                case 0:
                                                    intent = new Intent(context, LibrarySeatInfoView.class);
                                                    context.startActivity(intent);
                                                    return;
                                                case 1:
                                                    intent = new Intent(context, LibrarySearchView.class);
                                                    context.startActivity(intent);
                                                    return;
                                            }
                                        } //#########
                                    });
                                    builder.show();
                                    //new LibraryMenuDialog(context).show();
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

    public int getViewTypeCount() {
        return 2;
    }

    public int getItemViewType(int position) {
        if (position == 0 || position == separatorIndex || position == separatorIndex + 3)
            return TYPE_SEPARATOR;
        else return TYPE_ITEM;
    }

    class HolderItem {
        TextView title;
        TextView subTitle;
        TextView newNotice;
    }

    class HolderSeparator {
        TextView separator;
    }
}
