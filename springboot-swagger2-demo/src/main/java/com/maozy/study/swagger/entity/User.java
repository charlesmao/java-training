package com.maozy.study.swagger.entity;


public class User {

    private Integer userId;
    private String username;
    private String password;

    public User() {}

    public User(Integer userId, String username, String password) {
        this.userId = userId;
        this.username = username;
        this.password = password;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof User) {
            User user = (User)obj;
            Integer userId = user.getUserId();
            return  (this.userId.intValue() == userId.intValue());
        }
        return super.equals(obj);
    }
}
