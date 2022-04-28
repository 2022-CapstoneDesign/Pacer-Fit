package com.example.project.Dist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project.R;

import java.util.List;

public class OneWeekRecordAdapter extends RecyclerView.Adapter<OneWeekRecordAdapter.ViewHolder> {
    Context context;
    List<OneWeekRecordModel> record_list;

    public OneWeekRecordAdapter(Context context, List<OneWeekRecordModel> record_list) {
        this.context = context;
        this.record_list = record_list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.dist_one_week_record_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (record_list != null && record_list.size() > 0) {
            OneWeekRecordModel model = record_list.get(position);
            holder.day_record.setText(model.getDay());
            holder.date_record.setText(model.getDate());
            holder.totalTime_record.setText(model.getTotalTime());
            holder.km_record.setText(model.getKm());
        } else {
        }
    }

    @Override
    public int getItemCount() {
        return record_list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView day_record, date_record, totalTime_record, km_record;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            day_record = itemView.findViewById(R.id.day_record);
            date_record = itemView.findViewById(R.id.date_record);
            totalTime_record = itemView.findViewById(R.id.totalTime_record);
            km_record = itemView.findViewById(R.id.km_record);
        }
    }
}
