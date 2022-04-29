package com.example.project.Pedo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project.R;

import java.util.List;

public class SixMonthRecordAdapter extends RecyclerView.Adapter<SixMonthRecordAdapter.ViewHolder> {
    Context context;
    List<SixMonthRecordModel> record_list;

    public SixMonthRecordAdapter(Context context, List<SixMonthRecordModel> record_list) {
        this.context = context;
        this.record_list = record_list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.pedo_six_month_record_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (record_list != null && record_list.size() > 0) {
            SixMonthRecordModel model = record_list.get(position);
            holder.date_record.setText(model.getDate());
            holder.totalTime_record.setText(model.getTotalTime());
            holder.step_record.setText(model.getStep());
        } else {
        }
    }

    @Override
    public int getItemCount() {
        return record_list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView date_record, totalTime_record, step_record;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            date_record = itemView.findViewById(R.id.date_record);
            totalTime_record = itemView.findViewById(R.id.totalTime_record);
            step_record = itemView.findViewById(R.id.step_record);
        }
    }
}
