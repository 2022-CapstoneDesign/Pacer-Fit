package com.example.project.Main;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project.R;

import java.util.List;

public class FeedRecyclerAdapter extends RecyclerView.Adapter<FeedRecyclerAdapter.ViewHoler>{

    private List<FeedRecyclerModel> list;
    int layout;

    public FeedRecyclerAdapter (List<FeedRecyclerModel> feedList, int layout) {
        this.list = feedList;
        this.layout = layout;
    }

    @NonNull
    @Override
    public FeedRecyclerAdapter.ViewHoler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
        return new ViewHoler(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FeedRecyclerAdapter.ViewHoler holder, int position) {
        int imageView = list.get(position).getImageView();
        String textView = list.get(position).getTextView();

        holder.setData(imageView, textView);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHoler extends RecyclerView.ViewHolder{

        private ImageView feedImage;
        private TextView feedTitle;

        public ViewHoler(View itemView) {
            super(itemView);

            feedImage = itemView.findViewById(R.id.feedImage);
            feedTitle = itemView.findViewById(R.id.feedTitle);
        }

        public void setData(int imageView, String textView) {
            feedImage.setImageResource(imageView);
            feedTitle.setText(textView);
        }
    }
}
