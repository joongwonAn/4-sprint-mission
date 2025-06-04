package com.sprint.mission.discodeit.entity;

import java.util.List;

public class User extends BaseEntity {
    private String username;
    private List<Channel> channels;
    private List<Message> messages;

    public List<Channel> getChannels() {
        return channels;
    }

    public List<Message> getMessages() {
        return messages;
    }

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
