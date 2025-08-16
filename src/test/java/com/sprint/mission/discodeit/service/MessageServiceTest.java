package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.data.MessageDto;
import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageUpdateRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.mapper.MessageMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.basic.BasicMessageService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;


import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MessageServiceTest {
    @InjectMocks
    private BasicMessageService messageService;

    @Mock
    private MessageRepository messageRepository;

    @Mock
    private MessageMapper messageMapper;

    @Mock
    private ChannelRepository channelRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private BinaryContentRepository binaryContentRepository;
    @Mock
    private BinaryContentStorage binaryContentStorage;

    @Test
    @DisplayName("message 생성 성공")
    void message_create_success() {
        // given
        UUID channelId = UUID.randomUUID();
        UUID authorId = UUID.randomUUID();
        MessageCreateRequest messageCreateRequest = new MessageCreateRequest(
                "content",
                channelId,
                authorId
        );
        List<BinaryContentCreateRequest> binaryContentCreateRequests = List.of(
                new BinaryContentCreateRequest("fileName1", "contentType1", new byte[0]),
                new BinaryContentCreateRequest("fileName2", "contentType2", new byte[0])
        );

        Channel channel = mock(Channel.class);
        when(channelRepository.findById(channelId)).thenReturn(Optional.of(channel));

        User author = mock(User.class);
        when(userRepository.findById(authorId)).thenReturn(Optional.of(author));

        when(binaryContentRepository.save(any(BinaryContent.class))).thenAnswer(i -> i.getArguments()[0]);

        MessageDto expect = mock(MessageDto.class);
        when(messageMapper.toDto(any(Message.class))).thenReturn(expect);

        // when
        MessageDto result = messageService.create(messageCreateRequest, binaryContentCreateRequests);

        // then
        assertAll(
                "메시지 생성 검증",
                () -> assertEquals(expect.id(), result.id()),
                () -> assertEquals(expect.createdAt(), result.createdAt()),
                () -> assertEquals(expect.updatedAt(), result.updatedAt()),
                () -> assertEquals(expect.channelId(), result.channelId()),
                () -> assertEquals(expect.author(), result.author()),
                () -> assertEquals(expect.attachments(), result.attachments())
        );
        verify(channelRepository, times(1)).findById(channelId);
        verify(userRepository, times(1)).findById(authorId);
        verify(binaryContentRepository, times(2)).save(any(BinaryContent.class));
        verify(messageRepository, times(1)).save(any(Message.class));
        verify(messageMapper, times(1)).toDto(any(Message.class));
    }

    @Test
    void message_find_all_by_channel_id_success() {
        // given
        UUID channelId = UUID.randomUUID();
        Instant createAt = Instant.now();
        Pageable pageable = PageRequest.of(0, 10);

        // .... 나중에 해보자...

    }

    @Test
    @DisplayName("message 수정 성공")
    void message_update_success() {
        // given
        UUID messageId = UUID.randomUUID();
        MessageUpdateRequest request = new MessageUpdateRequest("newContent");
        when(messageRepository.findById(messageId))
                .thenReturn(Optional.of(mock(Message.class)));

        MessageDto expect = mock(MessageDto.class);
        when(messageMapper.toDto(any(Message.class))).thenReturn(expect);

        // when
        MessageDto result = messageService.update(messageId, request);

        // then
        assertAll(
                "메시지 업데이트 검증",
                () -> assertEquals(expect.id(), result.id()),
                () -> assertEquals(expect.createdAt(), result.createdAt()),
                () -> assertEquals(expect.updatedAt(), result.updatedAt()),
                () -> assertEquals(expect.content(), result.content()),
                () -> assertEquals(expect.channelId(), result.channelId()),
                () -> assertEquals(expect.author(), result.author()),
                () -> assertEquals(expect.attachments(), result.attachments())
        );
        verify(messageRepository, times(1)).findById(messageId);
        verify(messageMapper, times(1)).toDto(any(Message.class));
    }

    @Test
    @DisplayName("message 삭제 성공")
    void message_delete_success() {
        // given
        UUID messageId = UUID.randomUUID();
        when(messageRepository.existsById(messageId)).thenReturn(true);

        // when
        messageService.delete(messageId);

        // then
        verify(messageRepository, times(1)).existsById(messageId);
        verify(messageRepository, times(1)).deleteById(messageId);
    }
}