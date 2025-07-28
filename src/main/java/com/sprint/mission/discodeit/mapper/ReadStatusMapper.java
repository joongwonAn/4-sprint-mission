package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.data.ReadStatusDto;
import com.sprint.mission.discodeit.dto.request.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.dto.request.ReadStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.ReadStatus;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ReadStatusMapper {

  ReadStatus mapToCreateEntity(ReadStatusCreateRequest request);

//  @Mapping(source = "newLastReadAt", target = "lastReadAt")
  ReadStatus mapToUpdateEntity(ReadStatusUpdateRequest request);

  ReadStatusDto mapToDto(ReadStatus readStatus);

  List<ReadStatusDto> mapToDtoList(List<ReadStatus> readStatuses);
}
