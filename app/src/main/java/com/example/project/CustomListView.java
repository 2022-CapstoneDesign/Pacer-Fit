package com.example.project;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class CustomListView extends BaseAdapter {
    LayoutInflater layoutInflater = null;
    private ArrayList<ListData> listViewData = null;
    private int count = 0;

    public CustomListView(ArrayList<ListData> listData) {
        listViewData = listData;
        count = listViewData.size();
    }

    @Override
    public int getCount() {
        return count;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            final Context context = parent.getContext();
            if (layoutInflater == null) {
                layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            }
            convertView = layoutInflater.inflate(R.layout.custom_listview, parent, false);
        }

        TextView date = convertView.findViewById(R.id.date);
        TextView ste = convertView.findViewById(R.id.start_time_end);
        TextView tt = convertView.findViewById(R.id.total_time);
        TextView td = convertView.findViewById(R.id.total_dist);

        date.setText(listViewData.get(position).date);
        ste.setText(listViewData.get(position).ste);
        tt.setText(listViewData.get(position).tt);
        td.setText(listViewData.get(position).td);

        return convertView;
    }
}
