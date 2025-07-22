package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.data.UserStatusDto;
import com.sprint.mission.discodeit.dto.request.UserStatusCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserStatusMapper {

  User toCreateEntity(UserStatusCreateRequest request);

  @Mapping(source = "newLastActiveAt", target = "lastActiveAt")
  User toUpdateEntity(UserStatusUpdateRequest request);

  UserStatusDto toDto(UserStatus userStatus);

  List<UserStatusDto> toDtoList(List<UserStatus> userStatuses);
}
