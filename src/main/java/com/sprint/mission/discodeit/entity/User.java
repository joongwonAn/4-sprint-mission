package com.sprint.mission.discodeit.entity;

public class User extends BaseEntity {
    private String username;

    // 생성자
    public User(String name) {
        super();
        this.username = name;
    }

    // setter && getter
    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

}
