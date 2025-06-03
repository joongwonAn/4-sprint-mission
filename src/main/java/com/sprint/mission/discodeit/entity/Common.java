package com.sprint.mission.discodeit.entity;

import java.util.UUID;

abstract class Common {
    public UUID id = UUID.randomUUID();
    public long createdAt, updatedAt = System.currentTimeMillis() / 1000; // 초 단위로 변환

    // 생성자
    public Common(){}

    public Common(UUID id, long createdAt, long updatedAt) {
        this.id = id;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // 메소드 - getter 함수
    public UUID getId() {
        return id;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public long getUpdatedAt() {
        return updatedAt;
    }

    // 메소드 - update?
    public abstract void update();
}
