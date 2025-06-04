package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class JCFUserService implements UserService {
    private final List<User> userList;

    public JCFUserService() {
        userList = new ArrayList<>();
    }

    // 등록
    @Override
    public User createUser(String name) {
        User createUser = new User(name);
        userList.add(createUser);
        return createUser;
    }

    // 단건 조회
    @Override
    public User findUserById(UUID id) {
        for (User user : userList) {
            if (user.getId().equals(id)) {
                return user;
            }
        }
        return null;
    }

    // 다건 조회
    @Override
    public List<User> findAllUsers() {
        return new ArrayList<>(userList);
    }

    // 수정
    @Override
    public User updateUserById(UUID id, String newName) {
        for (User user : userList) {
            if (user.getId().equals(id)) {
                user.setUsername(newName);
                user.setUpdatedAt();
                return user;
            }
        }
        return null;
    }

    // 삭제
    @Override
    public User deleteUserById(UUID id) {
        for (User user : userList) {
            if (user.getId().equals(id)) {
                userList.remove(user);
                return user;
            }
        }
        return null;
    }
}
