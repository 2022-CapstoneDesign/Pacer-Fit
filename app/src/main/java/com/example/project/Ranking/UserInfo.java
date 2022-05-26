package com.example.project.Ranking;

public class UserInfo {
    private String userID;
    private String userPass;
    private String userName;
    private String userGender;
    private String userHeight;
    private String userWeight;
    private String userAge;
    private int userProfileNum;

    public String getUserID()
    {
        return userID;
    }
    public String getUserPass() { return userPass; }
    public String getUserName(){
        return userName;
    }
    public String getUserGender() {
        return userGender;
    }
    public String getUserHeight() {
        return userHeight;
    }
    public String getUserWeight() {
        return userWeight;
    }
    public String getUserAge() {
        return userAge;
    }
    public int getUserProfileNum() {return userProfileNum; }

    public void setUserID(String userID)
    {
        this.userID = userID;
    }
    public void setUserPass(String userPass) {
        this.userPass = userPass;
    }
    public void setUserName(String userName){
        this.userName = userName;
    }
    public void setUserProfileNum(int userProfileNum) {this.userProfileNum = userProfileNum; }
    public void setUserGender(String userGender) {
        this.userGender = userGender;
    }
    public void setUserHeight(String userHeight) {
        this.userHeight = userHeight;
    }
    public void setUserWeight(String userWeight) {
        this.userWeight = userWeight;
    }
    public void setUserAge(String userAge) {
        this.userAge = userAge;
    }
    private static UserInfo instance = null;

    public static synchronized UserInfo getInstance(){
        if(null == instance){
            instance = new UserInfo();
        }
        return instance;
    }


}
