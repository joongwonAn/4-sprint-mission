package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Channel;

import java.util.List;
import java.util.UUID;

public interface ChannelService {
    Channel createChannel(String name);

    Channel findChannelById(UUID id);
    List<Channel> findAllChannels();

    Channel updateChannelById(UUID id, String newName);

    Channel deleteChannelById(UUID id);
}
