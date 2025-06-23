package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.UUID;

public interface    UserService {
    User createUser(User user);
    User getUser(UUID id);
    List<User> getUsers();
    void updateUser (UUID id, String username);
    boolean deleteUser (UUID id);
}