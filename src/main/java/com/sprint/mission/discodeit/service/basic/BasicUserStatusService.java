package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.data.UserStatusDto;
import com.sprint.mission.discodeit.dto.request.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.mapper.UserStatusMapper;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserStatusService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.UUID;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BasicUserStatusService implements UserStatusService {

  private final UserStatusRepository userStatusRepository;
  private final UserRepository userRepository;
  private final UserStatusMapper userStatusMapper;

  @Override
  @Transactional(readOnly = true)
  public UserStatusDto find(UUID userStatusId) {
    return userStatusMapper.toDto(checkUserStatus(userStatusId));
  }

  // PAGING !!!!!!!!!!!!!!!
  @Override
  @Transactional(readOnly = true)
  public List<UserStatusDto> findAll() {
    return userStatusMapper.toDtoList(userStatusRepository.findAll());
  }

  @Override
  @Transactional
  public UserStatus update(UUID userStatusId, UserStatusUpdateRequest request) {
    UserStatus userStatus = checkUserStatus(userStatusId);
    userStatusMapper.fromUpdateRequest(request, userStatus);
    return userStatusRepository.save(userStatus);
  }

  @Override
  @Transactional
  public UserStatus updateByUserId(UUID userId, UserStatusUpdateRequest request) {
    User user = checkUser(userId);
    userStatusMapper.fromUpdateRequest(request, user.getStatus());
    return userStatusRepository.save(user.getStatus());
  }

  @Override
  @Transactional
  public void delete(UUID userStatusId) {
    UserStatus userStatus = checkUserStatus(userStatusId);
    userStatusRepository.delete(userStatus);
  }

  private UserStatus checkUserStatus(UUID userStatusId) {
    return userStatusRepository.findById(userStatusId)
        .orElseThrow(
            () -> new NoSuchElementException("UserStatus with id " + userStatusId + " not found"));
  }

  private User checkUser(UUID userId) {
    return userRepository.findById(userId)
        .orElseThrow(() -> new NoSuchElementException("User with id " + userId + " not found"));
  }

}
