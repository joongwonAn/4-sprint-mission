package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.test.context.ActiveProfiles;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@EnableJpaAuditing
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserStatusRepository userStatusRepository;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
        userStatusRepository.deleteAll();
    }

    @Test
    @DisplayName("fail: 상태가 없는 사용자는 조회 X")
    void findAllWithProfileAndStatus_whenUserHasNoStatus_thenReturnEmptyList() {
        // given
        User user = new User(
                "usename",
                "email@codeit.com",
                "password",
                null
        );
        userRepository.save(user);

        // when
        List<User> result = userRepository.findAllWithProfileAndStatus();

        // then
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("success: 상태있는 사용자는 조회 O")
    void findAllWithProfileAndStatus_whenUserHasStatus_thenReturnUserList() {
        // given
        User user = new User(
                "usename",
                "email@codeit.com",
                "password",
                null
        );
        UserStatus userStatus = new UserStatus(user, Instant.now());
        userRepository.save(user);
        userStatusRepository.save(userStatus);

        // when
        List<User> result = userRepository.findAllWithProfileAndStatus();

        // then
        assertEquals(1, result.size());
    }
}