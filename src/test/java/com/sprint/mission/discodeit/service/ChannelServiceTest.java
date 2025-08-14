package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.data.ChannelDto;
import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.request.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.mapper.ChannelMapper;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.basic.BasicChannelService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ChannelServiceTest {

    @InjectMocks
    private BasicChannelService channelService;

    @Mock
    private ChannelRepository channelRepository;
    @Mock
    private ChannelMapper channelMapper;

    @Mock
    private UserRepository userRepository;
    @Mock
    private ReadStatusRepository readStatusRepository;
    @Mock
    private MessageRepository messageRepository;

    @Test
    @DisplayName("public channel 생성 성공")
    void public_channel_create_success() {
        // given
        PublicChannelCreateRequest request = new PublicChannelCreateRequest(
                "name",
                "description"
        );
        Channel savedChannel = new Channel(
                ChannelType.PUBLIC,
                request.name(),
                request.description()
        );
        when(channelRepository.save(any(Channel.class))).thenReturn(savedChannel);

        ChannelDto expect = new ChannelDto(
                savedChannel.getId(),
                ChannelType.PUBLIC,
                request.name(),
                request.description(),
                null,
                null
        );
        when(channelMapper.toDto(any(Channel.class))).thenReturn(expect);

        // when
        ChannelDto result = channelService.create(request);

        // then
        assertAll(
                "public channel 생성 검중",
                () -> assertEquals(expect.id(), result.id()),
                () -> assertEquals(expect.type(), result.type()),
                () -> assertEquals(expect.name(), result.name()),
                () -> assertEquals(expect.description(), result.description()),
                () -> assertEquals(expect.participants(), result.participants()),
                () -> assertEquals(expect.lastMessageAt(), result.lastMessageAt())
        );
        verify(channelRepository, times(1)).save(any(Channel.class));
        verify(channelMapper, times(1)).toDto(any(Channel.class));
    }

    @Test
    @DisplayName("private 채널 생성 성공")
    void private_channel_create_success() {
        // given
        PrivateChannelCreateRequest request = new PrivateChannelCreateRequest(
                List.of(UUID.randomUUID(), UUID.randomUUID())
        );
        Channel savedChannel = new Channel(
                ChannelType.PRIVATE,
                null,
                null
        );
        when(channelRepository.save(any(Channel.class))).thenReturn(savedChannel);

        User user1 = new User("user1", "user1@codeit.com", "password", null);
        User user2 = new User("user2", "user2@codeit.com", "password", null);
        when(userRepository.findAllById(request.participantIds())).thenReturn(List.of(user1, user2));
        when(readStatusRepository.saveAll(anyList())).thenReturn(null);

        List<UserDto> expectParticipants = List.of(
                new UserDto(user1.getId(), user1.getUsername(), user1.getEmail(), null, true),
                new UserDto(user2.getId(), user2.getUsername(), user2.getEmail(), null, true)
        );
        ChannelDto expect = new ChannelDto(
                savedChannel.getId(),
                savedChannel.getType(),
                savedChannel.getName(),
                savedChannel.getDescription(),
                expectParticipants,
                null
        );
        when(channelMapper.toDto(any(Channel.class))).thenReturn(expect);

        // when
        ChannelDto result = channelService.create(request);

        // then
        assertAll(
                "private channel 생성 검증",
                () -> assertEquals(expect.id(), result.id()),
                () -> assertEquals(expect.type(), result.type()),
                () -> assertEquals(expect.name(), result.name()),
                () -> assertEquals(expect.description(), result.description()),
                () -> assertEquals(expect.participants(), result.participants()),
                () -> assertEquals(expect.lastMessageAt(), result.lastMessageAt())
        );
        verify(channelRepository, times(1)).save(any(Channel.class));
        verify(readStatusRepository, times(1)).saveAll(anyList());
        verify(userRepository, times(1)).findAllById(request.participantIds());
        verify(channelMapper, times(1)).toDto(any(Channel.class));
    }

    @Test
    @DisplayName("public channel 수정 성공")
    void update() {
        // given
        UUID channelId = UUID.randomUUID();
        PublicChannelUpdateRequest request = new PublicChannelUpdateRequest(
                "newName",
                "newDescription"
        );
        Channel channel = new Channel(
                ChannelType.PUBLIC,
                "name",
                "description"
        );
        when(channelRepository.findById(channelId)).thenReturn(Optional.of(channel));

        ChannelDto expect = new ChannelDto(
                channel.getId(),
                ChannelType.PUBLIC,
                request.newName(),
                request.newDescription(),
                null,
                null
        );
        when(channelMapper.toDto(any(Channel.class))).thenReturn(expect);

        // when
        ChannelDto result = channelService.update(channelId, request);

        // then
        assertAll(
                "public 채널 업데이트 성공",
                () -> assertEquals(expect.id(), result.id()),
                () -> assertEquals(expect.type(), result.type()),
                () -> assertEquals(expect.name(), result.name()),
                () -> assertEquals(expect.description(), result.description()),
                () -> assertEquals(expect.participants(), result.participants()),
                () -> assertEquals(expect.lastMessageAt(), result.lastMessageAt())
        );
        verify(channelRepository, times(1)).findById(channelId);
        verify(channelMapper, times(1)).toDto(any(Channel.class));
    }

    @Test
    @DisplayName("channel 삭제 성공")
    void delete() {
        // given
        UUID channelId = UUID.randomUUID();
        when(channelRepository.existsById(channelId)).thenReturn(true);

        doNothing().when(messageRepository).deleteAllByChannelId(channelId);
        doNothing().when(readStatusRepository).deleteAllByChannelId(channelId);

        // when
        channelService.delete(channelId);

        // then
        verify(channelRepository, times(1)).existsById(channelId);
        verify(channelRepository, times(1)).deleteById(channelId);
        verify(messageRepository, times(1)).deleteAllByChannelId(channelId);
        verify(readStatusRepository, times(1)).deleteAllByChannelId(channelId);
    }
}