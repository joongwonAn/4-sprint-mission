package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class Message extends BaseUpdatableEntity {

  @Column(columnDefinition = "TEXT", nullable = true)
  private String content;
  //
  @ManyToOne
  @JoinColumn(name = "channel_id", nullable = false)
  private Channel channel;

  @ManyToOne
  @JoinColumn(name = "author_id", nullable = false)
  private User author;
}
