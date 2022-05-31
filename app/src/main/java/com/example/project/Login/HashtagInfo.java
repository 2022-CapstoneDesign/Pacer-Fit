package com.example.project.Login;

public class HashtagInfo {

    private String title;
    private int selected;
    private int index;

    public HashtagInfo(String title,int selected, int index) {
        this.title = title;
        this.selected = selected;
        this.index = index;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getSelected() {
        return selected;
    }

    public void setSelected(int selected) {
        this.selected = selected;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
