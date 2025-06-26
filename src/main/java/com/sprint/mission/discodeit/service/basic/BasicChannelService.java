package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.ChannelDto;
import com.sprint.mission.discodeit.dto.PrivateChannelCreateDto;
import com.sprint.mission.discodeit.dto.PublicChannelCreateDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.mapper.ChannelMapper;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicChannelService implements ChannelService {
    private final ChannelRepository channelRepository;
    private final ReadStatusRepository readStatusRepository;
    private final MessageRepository messageRepository;

    private final ChannelMapper channelMapper;

    @Override
    public ChannelDto createPublicChannel(PublicChannelCreateDto dto) {
        Channel channel = channelMapper.toEntity(dto);
        channelRepository.save(channel);

        Instant lastMessage = getLastMessageAtOrNull(channel.getId());

        return channelMapper.toDto(channel, lastMessage, null);
    }

    @Override
    public ChannelDto createPrivateChannel(PrivateChannelCreateDto dto) {
        Channel channel = new Channel(ChannelType.PRIVATE, null, null);
        channelRepository.save(channel);

        for (UUID userId : dto.getUserIds()) {
            ReadStatus readStatus = new ReadStatus(userId, channel.getId());
            readStatusRepository.save(readStatus);
        }

        Instant lastMessage = getLastMessageAtOrNull(channel.getId());

        return channelMapper.toDto(channel, lastMessage, dto.getUserIds());
    }

    @Override
    public ChannelDto find(UUID channelId) {
        Channel channel = getChannelOrThrow(channelId);
        Instant lastMessage = getLastMessageAtOrNull(channelId);

        List<UUID> users = List.of();
        if (channel.getType() == ChannelType.PRIVATE) {
            users = readStatusRepository
                    .findByChannelId(channelId)
                    .stream()
                    .map(ReadStatus::getUserId)
                    .toList();
        }

        return channelMapper.toDto(channel, lastMessage, users);
    }

    @Override
    public List<ChannelDto> findAllByUserId(UUID userId) {
        List<Channel> channels = channelRepository.findAll();
        List<ChannelDto> channelDtos = new ArrayList<>();

        for (Channel channel : channels) {
            Instant lastMessage = getLastMessageAtOrNull(channel.getId());

            if(channel.getType()==ChannelType.PUBLIC){
                channelDtos.add(channelMapper.toDto(channel, lastMessage, null));
                continue;
            }

            List<UUID> userIds = List.of();
            if (channel.getType() == ChannelType.PRIVATE) {
                userIds = readStatusRepository
                        .findByChannelId(channel.getId())
                        .stream()
                        .map(ReadStatus::getUserId)
                        .toList();
            }

            if(userIds.contains(userId)){
                channelDtos.add(channelMapper.toDto(channel, lastMessage, userIds));
            }
        }

        return channelDtos;
    }

    private Channel getChannelOrThrow(UUID channelId) {
        return channelRepository.findById(channelId)
                .orElseThrow(() -> new NoSuchElementException("Channel with id " + channelId + " not found"));
    }

    private Instant getLastMessageAtOrNull(UUID channelId) {
        return messageRepository.findLastUpdatedAtByChannelId(channelId).orElse(null);
    }

//    @Override
//    public Channel update(UUID channelId, String newName, String newDescription) {
//        Channel channel = channelRepository.findById(channelId)
//                .orElseThrow(() -> new NoSuchElementException("Channel with id " + channelId + " not found"));
//        channel.update(newName, newDescription);
//        return channelRepository.save(channel);
//    }
//
//    @Override
//    public void delete(UUID channelId) {
//        if (!channelRepository.existsById(channelId)) {
//            throw new NoSuchElementException("Channel with id " + channelId + " not found");
//        }
//        channelRepository.deleteById(channelId);
//    }
}
