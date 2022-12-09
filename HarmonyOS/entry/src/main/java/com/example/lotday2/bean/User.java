package com.example.lotday2.bean;

import ohos.data.rdb.RdbOpenCallback;
import ohos.data.rdb.RdbStore;

public class User {

    private Integer userId;

    private String userName;

    private String userPhone;

    private String userPassword;

    private String userImage;

    private String userMotto;


    public User() {
    }

    public User(Integer userId, String userName, String userPhone, String userPassword, String userImage, String userMotto) {
        this.userId = userId;
        this.userName = userName;
        this.userPhone = userPhone;
        this.userPassword = userPassword;
        this.userImage = userImage;
        this.userMotto = userMotto;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }

    public String getUserMotto() {
        return userMotto;
    }

    public void setUserMotto(String userMotto) {
        this.userMotto = userMotto;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", userName='" + userName + '\'' +
                ", userPhone='" + userPhone + '\'' +
                ", userPassword='" + userPassword + '\'' +
                ", userImage='" + userImage + '\'' +
                ", userMotto='" + userMotto + '\'' +
                '}';
    }

    //在数据库创建User表
    private static RdbOpenCallback UserCallback = new RdbOpenCallback() {
        @Override
        public void onCreate(RdbStore store) {
            store.executeSql("create table if not exists yz_user (id INTEGER PRIMARY KEY AUTOINCREMENT,name varchar(50),phone varchar(50),password varchar(50))");
        }
        @Override
        public void onUpgrade(RdbStore store, int oldVersion, int newVersion) {
        }
    };
    public static RdbOpenCallback getUserCallback() {
        return UserCallback;
    }
    public static void setUserCallback(RdbOpenCallback userCallback) {
        UserCallback = userCallback;
    }
}
