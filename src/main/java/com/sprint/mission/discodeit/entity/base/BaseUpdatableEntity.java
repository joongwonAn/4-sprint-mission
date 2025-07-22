package com.sprint.mission.discodeit.entity.base;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import java.time.Instant;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.LastModifiedDate;

@Entity
@NoArgsConstructor
@Getter
public abstract class BaseUpdatableEntity extends BaseEntity {

  @LastModifiedDate
  @Column(nullable = true, updatable = true)
  private Instant updatedAt;
}
