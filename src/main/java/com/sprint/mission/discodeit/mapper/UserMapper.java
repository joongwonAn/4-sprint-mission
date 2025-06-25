package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.UserCreateDto;
import com.sprint.mission.discodeit.dto.UserStatusDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class UserMapper {

    public UserMapper() {

    }

    public User toEntity(UserCreateDto dto) {

        return new User(
                dto.getUsername(),
                dto.getEmail(),
                dto.getPassword(),
                null
        );
    }

    public UserStatusDto toDto(User user, UserStatus userStatus) {

        return new UserStatusDto(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getProfileImageId(),
                userStatus.getUpdatedAt()
        );
    }

}
