package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.request.UserUpdateRequest;
import com.sprint.mission.discodeit.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.entity.User;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = {BinaryContentMapper.class, UserStatusMapper.class})
public interface UserMapper {

  @Mapping(source = "profile", target = "profile", qualifiedByName = "toEntity")
  User toCreateEntity(UserCreateRequest request);

  @Mapping(source = "newUsername", target = "username")
  @Mapping(source = "newEmail", target = "email")
  @Mapping(source = "newPassword", target = "password")
  void toUpdateEntity(UserUpdateRequest request, @MappingTarget User user);

  UserDto toDto(User user);

  List<UserDto> toDtoList(List<User> users);
}
