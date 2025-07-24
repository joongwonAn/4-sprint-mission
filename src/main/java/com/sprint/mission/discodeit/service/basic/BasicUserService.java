package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserUpdateRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.mapper.UserStatusMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.UserService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BasicUserService implements UserService {

  private final UserRepository userRepository;
  private final BinaryContentRepository binaryContentRepository;
  private final UserMapper userMapper;
  private final UserStatusMapper userStatusMapper;

  @Override
  @Transactional
  public User create(UserCreateRequest request,
      Optional<BinaryContentCreateRequest> optionalProfileCreateRequest) {
    // username, email 중복 검사
    duplicateCheck(null, request.username(), request.email());

    // MapStruct로 기본 정보 매핑
    User user = userMapper.fromCreateRequest(request);

    // UserStatus
    Instant lastActiveAt = Optional.ofNullable(request.lastActiveAt())
        .orElse(Instant.now());
    UserStatus userStatus = userStatusMapper.fromCreateRequest(user, lastActiveAt);
    // 연관관계 설정
    user.setStatus(userStatus);

    return userRepository.save(user);
  }

  @Override
  @Transactional(readOnly = true)
  public UserDto find(UUID userId) {
    return userMapper.toDto(checkUser(userId));
  }

  // !!!!!!!!!!!!!!!! 페이징 구현 필요, Page<UserDto>
  @Override
  @Transactional(readOnly = true)
  public List<UserDto> findAll() {
    List<User> users = userRepository.findAll();
    return userMapper.toDtoList(users);
  }

  @Override
  @Transactional
  public User update(UUID userId, UserUpdateRequest request,
      Optional<BinaryContentCreateRequest> optionalProfileCreateRequest) {
    // user 확인 && 중복 검사
    User user = checkUser(userId);
    duplicateCheck(userId, request.newUsername(), request.newEmail());

    // update
    userMapper.fromUpdateRequest(request, user);
    // 프로필 이미지
    if (optionalProfileCreateRequest.isPresent()) {
      BinaryContentCreateRequest profileRequest = optionalProfileCreateRequest.get();
      BinaryContent newProfile = new BinaryContent(
          profileRequest.fileName(),
          (long) profileRequest.bytes().length,
          profileRequest.contentType(),
          profileRequest.bytes()
      );

      Optional.ofNullable(user.getProfile().getId())
          .ifPresent(binaryContentRepository::deleteById);

      user.setProfile(binaryContentRepository.save(newProfile));
    }

    return userRepository.save(user);
  }

  @Override
  @Transactional
  public void delete(UUID userId) {
    User user = checkUser(userId);
    userRepository.delete(user);
  }

  // 공통 메서드
  private User checkUser(UUID userId) {
    return userRepository.findById(userId)
        .orElseThrow(() -> new NoSuchElementException("User with id " + userId + " not found"));
  }

  private void duplicateCheck(UUID userId, String username, String email) {
    userRepository.findByUsername(username)
        .filter(user -> !user.getId().equals(userId))
        .ifPresent(user -> {
          throw new IllegalArgumentException("User with username " + username + " already exists");
        });

    userRepository.findByEmail(email)
        .filter(user -> !user.getId().equals(userId))
        .ifPresent(user -> {
          throw new IllegalArgumentException("User with email " + email + " already exists");
        });
  }

}
