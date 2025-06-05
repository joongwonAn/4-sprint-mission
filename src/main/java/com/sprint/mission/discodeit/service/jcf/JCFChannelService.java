package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.ChannelService;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class JCFChannelService implements ChannelService {
    private final List<Channel> data = new ArrayList<>();

    @Override
    public Channel createChannel(String name) {
        Channel channel = new Channel(name);
        data.add(channel);
        return channel;
    }

    @Override
    public Channel findChannelById(UUID id) {
        for (Channel channel : data) {
            if (channel.getId().equals(id)) {
                return channel;
            }
        }
        return null;
    }

    @Override
    public List<Channel> findAllChannels() {
        return new ArrayList<>(data);
    }

    @Override
    public Channel updateChannelById(UUID id, String newName) {
        for (Channel channel : data) {
            if (channel.getId().equals(id)) {
                channel.setChannelName(newName);
                channel.setUpdatedAt();
                return channel;
            }
        }
        return null;
    }

    @Override
    public Channel deleteChannelById(UUID id) {
        for (Channel channel : data) {
            if (channel.getId().equals(id)) {
                for (User member : channel.getMembers()) {
                    member.getChannels().remove(channel);
                }
                data.remove(channel);
                return channel;
            }
        }
        return null;
    }
}
