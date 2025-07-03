package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.data.ReadStatusDto;
import com.sprint.mission.discodeit.dto.request.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.dto.request.ReadStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.ReadStatus;

import java.util.List;
import java.util.UUID;

public interface ReadStatusService {
    ReadStatusDto create(ReadStatusCreateRequest request);
    ReadStatus find(UUID readStatusId);
    List<ReadStatusDto> findAllByUserId(UUID userId);
    List<ReadStatusDto> updateByChannelId(UUID channelId, ReadStatusUpdateRequest request);
    void delete(UUID readStatusId);
}
