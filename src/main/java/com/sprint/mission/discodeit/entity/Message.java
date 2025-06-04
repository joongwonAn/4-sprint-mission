package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.service.ChannelService;

import java.util.UUID;

public class Message extends BaseEntity {
    private User sender;
    private Channel channel;
    private String content;

    // 생성자
    public Message(User sender, Channel channel, String content) {
        super();
        this.sender = sender;
        this.channel = channel;
        this.content = content;
    }

    // setter && getter
    public void setContent(String content) {
        this.content = content;
    }

    public User getSender() {
        return sender;
    }

    public Channel getChannel() {
        return channel;
    }

    public String getContent() {
        return content;
    }
}
