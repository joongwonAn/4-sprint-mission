package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.request.UserUpdateRequest;
import com.sprint.mission.discodeit.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.entity.User;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

  User toCreateEntity(UserCreateRequest request);

  @Mapping(source = "newUsername", target = "username")
  @Mapping(source = "newEmail", target = "email")
  @Mapping(source = "newPassword", target = "password")
  User toUpdateEntity(UserUpdateRequest request);

  UserDto toDto(User user);

  List<UserDto> toDtoList(List<User> users);
}
