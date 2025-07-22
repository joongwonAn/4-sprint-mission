package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.data.ChannelDto;
import com.sprint.mission.discodeit.dto.request.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ChannelMapper {

  Channel toPrivateEntity(PrivateChannelCreateRequest request);

  Channel toPublicEntity(PublicChannelCreateRequest request);

  @Mapping(source = "newName", target = "name")
  @Mapping(source = "newDescription", target = "description")
  Channel toUpdateEntity(PublicChannelUpdateRequest request);

  ChannelDto toDto(Channel channel);

  List<ChannelDto> toDtoList(List<Channel> channels);
}
