package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.data.BinaryContentDto;
import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BinaryContentMapper {

  BinaryContent toCreateEntity(BinaryContentCreateRequest request);

  BinaryContentDto toDto(BinaryContent binaryContent);

  List<BinaryContentDto> toDtoList(List<BinaryContent> binaryContents);
}
