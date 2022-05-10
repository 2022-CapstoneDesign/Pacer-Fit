package com.example.project.Main;

public class FeedRecyclerModel {

    private int imageView;
    private String textView;

    FeedRecyclerModel(int imageView, String textView) {
        this.imageView = imageView;
        this.textView = textView;
    }

    public int getImageView() {
        return imageView;
    }

    public String getTextView() {
        return textView;
    }
}
