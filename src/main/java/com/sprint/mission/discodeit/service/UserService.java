package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.data.UserDto;import com.sprint.mission.discodeit.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserUpdateRequest;
import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.UUID;

public interface UserService {
    User create(UserCreateRequest userCreateRequest);
    UserDto find(UUID userId);
    List<UserDto> findAll();
    User update(UUID userId, UserUpdateRequest userUpdateRequest);
    void delete(UUID userId);
}
