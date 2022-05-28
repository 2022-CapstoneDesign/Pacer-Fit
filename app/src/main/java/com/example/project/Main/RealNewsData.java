package com.example.project.Main;

public class RealNewsData {
    static String[] newstitle = new String[11];
    static String[] newslink = new String[11];
    //setter
    public void setRealTimeNews(int i, String title, String link){
        newstitle[i] = title;
        newslink[i] = link;
    }
    public String getLink(int i){
        return newslink[i];
    }
    public String getTitle(int i){
        return newstitle[i];
    }
}
