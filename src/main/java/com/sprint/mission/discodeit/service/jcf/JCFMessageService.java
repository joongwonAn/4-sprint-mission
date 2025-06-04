package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.MessageService;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class JCFMessageService implements MessageService {
    private final List<Message> messageList = new ArrayList<>();

    @Override
    public Message sendMessage(User sender, Channel channel, String content) {
        Message message = new Message(sender, channel, content);
        messageList.add(message);
        return message;
    }

    @Override
    public Message findMessage(UUID id) {
        for (Message message : messageList) {
            if (message.getId().equals(id)) {
                return message;
            }
        }
        return null;
    }

    @Override
    public List<Message> findAllMessages() {
        return new ArrayList<>(messageList);
    }

    @Override
    public Message updateMessage(UUID id, String newContent) {
        for (Message message : messageList) {
            if (message.getId().equals(id)) {
                message.setContent(newContent);
                message.setUpdatedAt();
                return message;
            }
        }
        return null;
    }

    @Override
    public Message deleteMessage(UUID id) {
        for (Message message : messageList) {
            if (message.getId().equals(id)) {
                messageList.remove(message);
                return message;
            }
        }
        return null;
    }
}
