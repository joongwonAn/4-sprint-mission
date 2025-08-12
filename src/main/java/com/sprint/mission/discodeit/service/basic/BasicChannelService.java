package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.data.ChannelDto;
import com.sprint.mission.discodeit.dto.request.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.exception.channel.ChannelNotFoundException;
import com.sprint.mission.discodeit.exception.channel.ChannelPrivateUpdateNotAllowedException;
import com.sprint.mission.discodeit.mapper.ChannelMapper;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ChannelService;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class BasicChannelService implements ChannelService {

    private final ChannelRepository channelRepository;
    //
    private final ReadStatusRepository readStatusRepository;
    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final ChannelMapper channelMapper;

    @Transactional
    @Override
    public ChannelDto create(PublicChannelCreateRequest request) {
        log.info("[CHANNEL] Public 채널 생성 시작: request = {}", request);
        String name = request.name();
        String description = request.description();
        Channel channel = new Channel(ChannelType.PUBLIC, name, description);

        channelRepository.save(channel);
        log.info("[CHANNEL] Public 채널 생성 완료: channel = {}", channel);
        return channelMapper.toDto(channel);
    }

    @Transactional
    @Override
    public ChannelDto create(PrivateChannelCreateRequest request) {
        log.info("[CHANNEL] Private 채널 생성 시작: request = {}", request);
        Channel channel = new Channel(ChannelType.PRIVATE, null, null);
        channelRepository.save(channel);
        log.info("[CHANNEL] Private 채널 생성 완료: channel = {}", channel);

        log.info("[CHANNEL] ReadStatus 생성 시작");
        List<ReadStatus> readStatuses = userRepository.findAllById(request.participantIds()).stream()
                .map(user -> new ReadStatus(user, channel, channel.getCreatedAt()))
                .toList();
        readStatusRepository.saveAll(readStatuses);
        log.info("[CHANNEL] ReadStatus 생성 완료: readStatus = {}", readStatuses);

        return channelMapper.toDto(channel);
    }

    @Transactional(readOnly = true)
    @Override
    public ChannelDto find(UUID channelId) {
        return channelRepository.findById(channelId)
                .map(channelMapper::toDto)
                .orElseThrow(
                        () -> new ChannelNotFoundException(Map.of("channelId", channelId)));
    }

    @Transactional(readOnly = true)
    @Override
    public List<ChannelDto> findAllByUserId(UUID userId) {
        List<UUID> mySubscribedChannelIds = readStatusRepository.findAllByUserId(userId).stream()
                .map(ReadStatus::getChannel)
                .map(Channel::getId)
                .toList();

        return channelRepository.findAllByTypeOrIdIn(ChannelType.PUBLIC, mySubscribedChannelIds)
                .stream()
                .map(channelMapper::toDto)
                .toList();
    }

    @Transactional
    @Override
    public ChannelDto update(UUID channelId, PublicChannelUpdateRequest request) {
        log.info("[CHANNEL] 채널 업데이트 시작: channelId = {}", channelId);
        String newName = request.newName();
        String newDescription = request.newDescription();
        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(() -> {
                    log.error("[CHANNEL] channelId = {}를 찾을 수 없음", channelId);
                    return new ChannelNotFoundException(Map.of("channelId", channelId));
                });
        if (channel.getType().equals(ChannelType.PRIVATE)) {
            log.error("[CHANNEL] Private 채널은 업데이트 할 수 없음");
            throw new ChannelPrivateUpdateNotAllowedException(Map.of("channelId", channelId));
        }
        channel.update(newName, newDescription);
        log.info("[CHANNEL] 채널 업데이트 성공: channel = {}", channel);
        return channelMapper.toDto(channel);
    }

    @Transactional
    @Override
    public void delete(UUID channelId) {
        log.info("[CHANNEL] 채널 삭제 시작: channelId = {}", channelId);
        if (!channelRepository.existsById(channelId)) {
            log.warn("[CHANNEL] channelId = {}를 찾을 수 없음", channelId);
            throw new ChannelNotFoundException(Map.of("channelId", channelId));
        }

        messageRepository.deleteAllByChannelId(channelId);
        readStatusRepository.deleteAllByChannelId(channelId);

        log.info("[CHANNEL] 채널 삭제 완료: channelId = {}", channelId);
        channelRepository.deleteById(channelId);
    }
}
