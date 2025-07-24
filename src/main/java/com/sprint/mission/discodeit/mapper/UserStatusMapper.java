package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.data.UserStatusDto;
import com.sprint.mission.discodeit.dto.request.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import java.time.Instant;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserStatusMapper {

  default UserStatus fromCreateRequest(User user, Instant lastActiveAt) {
    return new UserStatus(user, lastActiveAt);
  }

  @Mapping(source = "newLastActiveAt", target = "lastActiveAt")
  void fromUpdateRequest(UserStatusUpdateRequest request, @MappingTarget UserStatus userStatus);

  UserStatusDto toDto(UserStatus userStatus);

  List<UserStatusDto> toDtoList(List<UserStatus> userStatuses);
}
