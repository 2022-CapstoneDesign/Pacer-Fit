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

public class RecordAdapter extends RecyclerView.Adapter<RecordAdapter.ViewHolder> {
    Context context;
    List<RecordModel> record_list;

    public RecordAdapter(Context context, List<RecordModel> record_list) {
        this.context = context;
        this.record_list = record_list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.dist_record_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (record_list != null && record_list.size() > 0) {
            RecordModel model = record_list.get(position);
            holder.day_record.setText(model.getDay());
            holder.startEndTime_record.setText(model.getStartEndTime());
            holder.totalTime_record.setText(model.getTotalTime());
            holder.km_record.setText(model.getStep());
        } else {
        }
    }

    @Override
    public int getItemCount() {
        return record_list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView day_record, startEndTime_record, totalTime_record, km_record;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            day_record = itemView.findViewById(R.id.day_record);
            startEndTime_record = itemView.findViewById(R.id.startEndTime_record);
            totalTime_record = itemView.findViewById(R.id.totalTime_record);
            km_record = itemView.findViewById(R.id.km_record);
        }
    }
}
