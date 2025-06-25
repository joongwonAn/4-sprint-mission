package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.UserStatus;

import java.util.Optional;
import java.util.UUID;

public interface UserStatusRepository {

    UserStatus save(UserStatus userStatus);

    Optional<UserStatus> findByUserId(UUID userId);

    void deleteById(UUID userId);
}
