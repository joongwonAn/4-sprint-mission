package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class MessageAttachments extends BaseUpdatableEntity {

  @ManyToOne
  @JoinColumn(name = "message_id", nullable = true)
  private Message message;

  @OneToOne
  @JoinColumn(name = "attachment_id", nullable = true)
  private BinaryContent attachment;
}
