package com.example.project.Main;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project.R;

import java.util.List;

public class RealNewsFeedRecyclerAdapter extends RecyclerView.Adapter<RealNewsFeedRecyclerAdapter.ViewHoler>{

    private Context mContext;
    private List<RealNewsFeedRecyclerModel> list;
    int layout;

    public RealNewsFeedRecyclerAdapter (Context context, List<RealNewsFeedRecyclerModel> feedList, int layout) {
        this.mContext = context;
        this.list = feedList;
        this.layout = layout;
    }

    @NonNull
    @Override
    public RealNewsFeedRecyclerAdapter.ViewHoler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);

        return new RealNewsFeedRecyclerAdapter.ViewHoler(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RealNewsFeedRecyclerAdapter.ViewHoler holder, int position) {
        String textView = list.get(position).getTextView();
        holder.setData(textView);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHoler extends RecyclerView.ViewHolder{

        private TextView feedTitle;

        public ViewHoler(View itemView) {
            super(itemView);

            feedTitle = itemView.findViewById(R.id.realfeedTitle);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = getAdapterPosition();
                    if(pos!=RecyclerView.NO_POSITION){
                        Intent intent = new Intent(mContext,fitnessActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("TEXT", list.get(pos).getTextView());
                        mContext.startActivity(intent);
                    }
                }
            });
        }

        public void setData(String textView) {
            feedTitle.setText(textView);
        }
    }
}
