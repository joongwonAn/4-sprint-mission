package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class BinaryContent extends BaseEntity {

  @Column(length = 255, nullable = false)
  private String fileName;

  @Column(columnDefinition = "bigint", nullable = false)
  private Long size;

  @Column(length = 100, nullable = false)
  private String contentType;

  @Column(nullable = false)
  private byte[] bytes;
}
