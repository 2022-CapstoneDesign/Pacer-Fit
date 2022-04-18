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

public class OneMonthRecordAdapter extends RecyclerView.Adapter<OneMonthRecordAdapter.ViewHolder> {
    Context context;
    List<OneMonthRecordModel> record_list;

    public OneMonthRecordAdapter(Context context, List<OneMonthRecordModel> record_list) {
        this.context = context;
        this.record_list = record_list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.dist_one_month_record_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (record_list != null && record_list.size() > 0) {
            OneMonthRecordModel model = record_list.get(position);
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
        TextView date_record, totalTime_record, km_record;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            date_record = itemView.findViewById(R.id.date_record);
            totalTime_record = itemView.findViewById(R.id.totalTime_record);
            km_record = itemView.findViewById(R.id.km_record);
        }
    }
}
