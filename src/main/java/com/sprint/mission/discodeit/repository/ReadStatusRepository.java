package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.ReadStatus;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ReadStatusRepository {
    ReadStatus save(ReadStatus readStatus);

    List<ReadStatus> findByChannelId(UUID channelId);

    void deleteByChannelId(UUID channelId);

    Optional<ReadStatus> findByUserIdAndChannelId(UUID userId, UUID channelId);

    List<ReadStatus> findAll();
}
