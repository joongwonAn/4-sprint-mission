package com.sprint.mission.discodeit.dto.request;

import java.time.Instant;

public record UserCreateRequest(
    String username,
    String email,
    String password,
    Instant lastActiveAt
) {

}
