package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.dto.*;
import com.sprint.mission.discodeit.entity.BinaryContentType;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.mapper.*;
import com.sprint.mission.discodeit.repository.*;
import com.sprint.mission.discodeit.repository.file.*;
import com.sprint.mission.discodeit.service.*;
import com.sprint.mission.discodeit.service.basic.*;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.UUID;

public class JavaApplication {

    public static void main(String[] args) {
        UserMapper userMapper = new UserMapper();
        BinaryContentMapper binaryContentMapper = new BinaryContentMapper();
        ChannelMapper channelMapper = new ChannelMapper();
        ReadStatusMapper rsMapper = new ReadStatusMapper();

        UserRepository userRepository = new FileUserRepository();
        UserStatusRepository userStatusRepository = new FileUserStatusRepository();
        BinaryContentRepository binaryContentRepository = new FileBinaryContentRepository();
        ChannelRepository channelRepository = new FileChannelRepository();
        ReadStatusRepository readStatusRepository = new FileReadStatusRepository();
        MessageRepository messageRepository = new FileMessageRepository();
        ReadStatusRepository readStatusRepo = new FileReadStatusRepository();

        UserService userService = new BasicUserService(
                userRepository,
                userStatusRepository,
                binaryContentRepository,
                userMapper,
                binaryContentMapper
        );
        AuthService authService = new BasicAuthService(
                userRepository,
                userStatusRepository,
                userMapper
        );
        ChannelService channelService = new BasicChannelService(
                channelRepository,
                readStatusRepository,
                messageRepository,
                channelMapper
        );
        ReadStatusService readStatusService = new BasicReadStatusService(
                userRepository,
                channelRepository,
                readStatusRepo,
                rsMapper
        );

        // 회원가입
        byte[] dummyBytes = "hello image file content".getBytes(StandardCharsets.UTF_8);
        BinaryContentCreateDto binaryContentDto = new BinaryContentCreateDto(
                dummyBytes,
                "profile.jpg",
                BinaryContentType.USER_PROFILE_IMAGE
        );
        UserCreateDto userCreateDto = new UserCreateDto(
                "woody",
                "woody@codeit.com",
                "woody1234",
                binaryContentDto
        );
        UserCreateDto userCreateDto2 = new UserCreateDto(
                "jw",
                "jw@codeit.com",
                "jw1234",
                binaryContentDto
        );

        // create TEST
        UserStatusResponseDto createdUser1 = userService.create(userCreateDto);
        UserStatusResponseDto createdUser2 = userService.create(userCreateDto2);
        UUID userId = createdUser1.getId();
        UUID userId2 = createdUser2.getId();
        System.out.println("\n[생성 완료] ID: " + userId + ", Username: " + createdUser1.getUsername());
        System.out.println("\n[생성 완료] ID: " + userId2 + ", Username: " + createdUser2.getUsername());
        System.out.println("[프로필 이미지 ID] " + createdUser1.getProfileImageId());
        System.out.println("[프로필 이미지 ID] " + createdUser2.getProfileImageId());

        // login TEST
        UserLoginDto loginDto = new UserLoginDto("woody", "woody1234");
        UserStatusResponseDto afterLogin = authService.login(loginDto);
        System.out.println("\n[LOGIN] updatedAt=" + afterLogin.getUpdatedAt() +
                ", online? " + userService.isOnline(afterLogin));

        // find TEST
        UserStatusResponseDto foundUser = userService.find(userId);
        System.out.println("\n[find 결과]");
        System.out.println("ID: " + foundUser.getId());
        System.out.println("Username: " + foundUser.getUsername());
        System.out.println("Email: " + foundUser.getEmail());
        System.out.println("ProfileImageId: " + foundUser.getProfileImageId());

        // findAll TEST
        List<UserStatusResponseDto> allUsers = userService.findAll();
        System.out.println("\n[findAll 결과]");
        for (UserStatusResponseDto dto : allUsers) {
            System.out.println("→ ID: " + dto.getId() + ", Username: " + dto.getUsername()
                    + ", Email: " + dto.getEmail()
                    + ", Online: " + userService.isOnline(dto));
        }

        // update TEST
        byte[] newImageBytes = "new image content bytes".getBytes(StandardCharsets.UTF_8);
        BinaryContentCreateDto newProfileImage = new BinaryContentCreateDto(
                newImageBytes,
                "new_profile.jpg",
                BinaryContentType.USER_PROFILE_IMAGE
        );

        UserUpdateDto updateDto = new UserUpdateDto(
                userId, // 어떤 유저를 수정할지 지정
                "buzz", // 새로운 username
                "buzz@codeit.com", // 새로운 email
                "buzz1234", // 새로운 password
                newProfileImage // 기존 이미지 그대로 사용
        );
        User updatedUser = userService.update(updateDto);

        System.out.println("\n[update 결과]");
        System.out.println("ID: " + updatedUser.getId());
        System.out.println("Username: " + updatedUser.getUsername());
        System.out.println("Email: " + updatedUser.getEmail());
        System.out.println("ProfileImageId: " + updatedUser.getProfileImageId());

        // delete TEST
        /*userService.delete(userId);
        List<UserStatusDto> allUsers2 = userService.findAll();
        System.out.println("\n[After delete : findAll 결과]");
        for (UserStatusDto dto : allUsers2) {
            System.out.println("→ ID: " + dto.getId() + ", Username: " + dto.getUsername()
                    + ", Email: " + dto.getEmail()
                    + ", Online: " + userService.isOnline(dto));
        }*/

        // CHANNEL 관련
        // public channel CREATE TEST
        PublicChannelCreateDto pubDto = new PublicChannelCreateDto("공지", "전체 공지사항입니다.");
        ChannelResponseDto pubRes = channelService.createPublicChannel(pubDto);

        System.out.println("\n-- PUBLIC 채널 생성 --");
        System.out.println(pubRes.getId() + " / " + pubRes.getType()
                + " / " + pubRes.getName() + " / " + pubRes.getDescription());

        // private channel CREATE TEST
        PrivateChannelCreateDto priDto = new PrivateChannelCreateDto(
                List.of(createdUser1.getId(), createdUser2.getId()));
        ChannelResponseDto priRes = channelService.createPrivateChannel(priDto);

        System.out.println("\n-- PRIVATE 채널 생성 --");
        System.out.println(priRes.getId() + " / " + priRes.getType()
                + " / name=" + priRes.getName()   // null 예상
                + " / desc=" + priRes.getDescription()); // null 예상

        System.out.println("\n-- CHANNEL 조회(find) 테스트 --");
        ChannelResponseDto foundPub = channelService.find(pubRes.getId());
        ChannelResponseDto foundPri = channelService.find(priRes.getId());

        System.out.printf("[PUBLIC ] id=%s, lastMsg=%s, users=%s%n",
                foundPub.getId(), foundPub.getLastMessageAt(), foundPub.getUserIds());

        System.out.printf("[PRIVATE] id=%s, lastMsg=%s, users=%s%n",
                foundPri.getId(), foundPri.getLastMessageAt(), foundPri.getUserIds());

        List<ChannelResponseDto> allChannels = channelService.findAllByUserId(userId);
        System.out.println("\n[findAll - 채널 전체 조회 결과]");
        for (ChannelResponseDto dto : allChannels) {
            System.out.println("→ ID: " + dto.getId()
                    + ", Type: " + dto.getType()
                    + ", Name: " + dto.getName()
                    + ", Description: " + dto.getDescription()
                    + ", LastMessageAt: " + dto.getLastMessageAt()
                    + ", UserIds: " + dto.getUserIds());
        }

        ChannelUpdateDto channelUpdateDto = new ChannelUpdateDto(
                pubRes.getId(),
                "공지 - 수정됨",
                "수정된 전체 공지사항입니다."
        );
        ChannelResponseDto updatedChannel = channelService.update(channelUpdateDto);

        System.out.println("\n-- CHANNEL 업데이트 테스트 --");
        System.out.println(updatedChannel.getId() + " / " + updatedChannel.getType()
                + " / " + updatedChannel.getName() + " / " + updatedChannel.getDescription());

        /*System.out.println("\n-- CHANNEL 삭제 테스트 --");
        UUID deleteChannelId = pubRes.getId();

        List<ChannelResponseDto> channelsBeforeDelete = channelService.findAllByUserId(userId);
        System.out.println("삭제 전 채널 개수: " + channelsBeforeDelete.size());

        channelService.delete(deleteChannelId);

        List<ChannelResponseDto> channelsAfterDelete = channelService.findAllByUserId(userId);
        System.out.println("삭제 후 채널 개수: " + channelsAfterDelete.size());*/

        BinaryContentMapper bcMapper = new BinaryContentMapper();
        BinaryContentCreateDto attach1 = new BinaryContentCreateDto(
                "file-1".getBytes(StandardCharsets.UTF_8),
                "img01.png",
                BinaryContentType.MESSAGE_IMAGE);

        BinaryContentCreateDto attach2 = new BinaryContentCreateDto(
                "file-2".getBytes(StandardCharsets.UTF_8),
                "img02.png",
                BinaryContentType.MESSAGE_IMAGE);

        UUID attachId1 = binaryContentRepository.save(bcMapper.toEntity(attach1, userId)).getId();
        UUID attachId2 = binaryContentRepository.save(bcMapper.toEntity(attach2, userId)).getId();

        MessageMapper msgMapper = new MessageMapper();
        MessageService messageService = new BasicMessageService(
                messageRepository,
                channelRepository,
                userRepository,
                binaryContentRepository,
                msgMapper
        );

        MessageCreateDto msgCreateDto = new MessageCreateDto(
                "첨부파일 두 개 테스트",
                pubRes.getId(),
                userId,
                List.of(attachId1, attachId2)
        );

        MessageResponseDto msgRes = messageService.create(msgCreateDto);

        System.out.println("\n-- MESSAGE create TEST --");
        System.out.printf("msgId=%s, content=%s, channel=%s, author=%s, attachments=%s, createdAt=%s%n",
                msgRes.getId(), msgRes.getContent(), msgRes.getChannelId(),
                msgRes.getAuthorId(), msgRes.getAttachmentIds(), msgRes.getCreatedAt());

        List<MessageResponseDto> msgsInPub = messageService.findByChannelId(pubRes.getId());
        System.out.println("\n-- MESSAGE findByChannelId TEST --");
        for (MessageResponseDto msg : msgsInPub) {
            System.out.printf("msgId=%s, content=%s, author=%s, createdAt=%s%n",
                    msg.getId(), msg.getContent(), msg.getAuthorId(), msg.getCreatedAt());
        }

        // MESSAGE delete TEST
        /*System.out.println("\n-- MESSAGE delete TEST --");

        messageService.delete(msgRes.getId());
        boolean stillExists = messageRepository.existsById(msgRes.getId());
        System.out.println("메시지 존재 여부(should be false) : " + stillExists);
        for (UUID attId : List.of(attachId1, attachId2)) {
            boolean attachExists = binaryContentRepository.findById(attId).isPresent();
            System.out.println("첨부파일 " + attId + " 존재 여부(should be false) : " + attachExists);
        }*/

        // MESSAGE update TEST
        Message updatedMsg = messageService.update(msgRes.getId(), "내용 수정");

        System.out.println("\n-- MESSAGE update TEST --");
        System.out.printf("msgId=%s, content=%s, updatedAt=%s%n",
                updatedMsg.getId(), updatedMsg.getContent(), updatedMsg.getUpdatedAt());

        // ReadStatus create TEST
        ReadStatusCreateDto rsDto = new ReadStatusCreateDto(userId, pubRes.getId());
        ReadStatusResponseDto rsRes = readStatusService.create(rsDto);

        System.out.println("\n-- READ‑STATUS create TEST --");
        System.out.printf("id=%s, user=%s, channel=%s, readAt=%s%n", rsRes.getId(), rsRes.getUserId(), rsRes.getChannelId(), rsRes.getReadAt());

        // ReadStatus find TEST
        ReadStatusResponseDto foundRead = readStatusService.find(rsRes.getId());

        System.out.println("\n-- READ STATUS 조회 (find) --");
        System.out.println("ID: " + foundRead.getId());
        System.out.println("User ID: " + foundRead.getUserId());
        System.out.println("Channel ID: " + foundRead.getChannelId());
        System.out.println("읽은 시각: " + foundRead.getReadAt());


        System.out.println("\n-- 특정 사용자 ReadStatus 전체 조회 --");
        List<ReadStatusResponseDto> readStatusList = readStatusService.findAllByUserId(userId);
        for (ReadStatusResponseDto dto : readStatusList) {
            System.out.println("→ ID: " + dto.getId()
                    + ", userId: " + dto.getUserId()
                    + ", channelId: " + dto.getChannelId()
                    + ", readAt: " + dto.getReadAt());
        }

        // ReadStatus update TEST
        System.out.println("\n-- READ STATUS 업데이트 테스트 --");
        ReadStatusUpdateDto RsUpdateDto = new ReadStatusUpdateDto(
                rsRes.getId(),
                rsRes.getReadAt().plusSeconds(300) // 기존보다 5분 뒤
        );
        ReadStatus updatedRs = readStatusService.update(RsUpdateDto);
        System.out.printf("업데이트된 readAt=%s%n", updatedRs.getReadAt());
    }
}
