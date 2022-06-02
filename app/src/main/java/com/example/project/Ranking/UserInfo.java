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
    private String[] userHashTags;
    private int userLevelLike;



    private int tag_park;
    private int tag_mountain;
    private int tag_forest;
    private int tag_sea;
    private int tag_beach;
    private int tag_trekking;
    private int tag_nature;
    private int tag_sights;
    private int tag_town;
    private int tag_scenery;
    private int tag_history;

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
    public String[] getUserHashTags() {
        return userHashTags;
    }
    public int getUserLevelLike() {
        return userLevelLike;
    }

    public int getTag_park() {
        return tag_park;
    }
    public int getTag_mountain() {
        return tag_mountain;
    }
    public int getTag_forest() {
        return tag_forest;
    }
    public int getTag_sea() {
        return tag_sea;
    }
    public int getTag_beach() {
        return tag_beach;
    }
    public int getTag_trekking() {
        return tag_trekking;
    }
    public int getTag_nature() {
        return tag_nature;
    }
    public int getTag_sights() {
        return tag_sights;
    }
    public int getTag_town() {
        return tag_town;
    }
    public int getTag_scenery() {
        return tag_scenery;
    }
    public int getTag_history() {
        return tag_history;
    }

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
    public void setUserHashTags(String[] userHashTags) {
        this.userHashTags = userHashTags;
    }
    public void setUserLevelLike(int userLevelLike) {
        this.userLevelLike = userLevelLike;
    }

    public void setTag_park(int tag_park) {
        this.tag_park = tag_park;
    }
    public void setTag_mountain(int tag_mountain) {
        this.tag_mountain = tag_mountain;
    }
    public void setTag_forest(int tag_forest) {
        this.tag_forest = tag_forest;
    }
    public void setTag_sea(int tag_sea) {
        this.tag_sea = tag_sea;
    }
    public void setTag_beach(int tag_beach) {
        this.tag_beach = tag_beach;
    }
    public void setTag_trekking(int tag_trekking) {
        this.tag_trekking = tag_trekking;
    }
    public void setTag_nature(int tag_nature) {
        this.tag_nature = tag_nature;
    }
    public void setTag_sights(int tag_sights) {
        this.tag_sights = tag_sights;
    }
    public void setTag_town(int tag_town) {
        this.tag_town = tag_town;
    }
    public void setTag_scenery(int tag_scenery) {
        this.tag_scenery = tag_scenery;
    }
    public void setTag_history(int tag_history) {
        this.tag_history = tag_history;
    }

    private static UserInfo instance = null;

    public static synchronized UserInfo getInstance(){
        if(null == instance){
            instance = new UserInfo();
        }
        return instance;
    }


}
