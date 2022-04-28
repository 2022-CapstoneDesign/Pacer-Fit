package com.example.project.Ranking;

public class UserInfo {
    private String userID;
    private String userName;

    public String getUserID()
    {
        return userID;
    }
    public String getUserName(){
        return userName;
    }
    public void setUserID(String userID)
    {
        this.userID = userID;
    }
    public void setUserName(String userName){
        this.userName = userName;
    }
    private static UserInfo instance = null;

    public static synchronized UserInfo getInstance(){
        if(null == instance){
            instance = new UserInfo();
        }
        return instance;
    }


}
