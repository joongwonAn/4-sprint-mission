//package com.sprint.mission.discodeit.controller;
//
//import com.sprint.mission.discodeit.controller.api.MessageApi;
//import com.sprint.mission.discodeit.dto.data.MessageDto;
//import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
//import com.sprint.mission.discodeit.dto.request.MessageCreateRequest;
//import com.sprint.mission.discodeit.dto.request.MessageUpdateRequest;
//import com.sprint.mission.discodeit.entity.Message;
//import com.sprint.mission.discodeit.service.MessageService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Optional;
//import java.util.UUID;
//
//@RequiredArgsConstructor
//@RestController
//@RequestMapping("/api/messages")
//public class MessageController implements MessageApi {
//
//  private final MessageService messageService;
//
//  @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
//  public ResponseEntity<MessageDto> create(
//      @RequestPart("messageCreateRequest") MessageCreateRequest messageCreateRequest,
//      @RequestPart(value = "attachments", required = false) List<MultipartFile> attachments
//  ) {
//    return null;
////    List<BinaryContentCreateRequest> attachmentRequests = Optional.ofNullable(attachments)
////        .map(files -> files.stream()
////            .map(file -> {
////              try {
////                return new BinaryContentCreateRequest(
////                    file.getOriginalFilename(),
////                    file.getContentType(),
////                    file.getBytes()
////                );
////              } catch (IOException e) {
////                throw new RuntimeException(e);
////              }
////            })
////            .toList())
////        .orElse(new ArrayList<>());
////    Message createdMessage = messageService.create(messageCreateRequest, attachmentRequests);
////    return ResponseEntity
////        .status(HttpStatus.CREATED)
////        .body(createdMessage);
//  }
//
//  @PatchMapping(path = "{messageId}")
//  public ResponseEntity<MessageDto> update(@PathVariable("messageId") UUID messageId,
//      @RequestBody MessageUpdateRequest request) {
//    return null;
////    Message updatedMessage = messageService.update(messageId, request);
////    return ResponseEntity
////        .status(HttpStatus.OK)
////        .body(updatedMessage);
//  }
//
//  @DeleteMapping(path = "{messageId}")
//  public ResponseEntity<Void> delete(@PathVariable("messageId") UUID messageId) {
//    return null;
////    messageService.delete(messageId);
////    return ResponseEntity
////        .status(HttpStatus.NO_CONTENT)
////        .build();
//  }
//
//  @GetMapping
//  public ResponseEntity<List<MessageDto>> findAllByChannelId(
//      @RequestParam("channelId") UUID channelId) {
//    return null;
////    List<Message> messages = messageService.findAllByChannelId(channelId);
////    return ResponseEntity
////        .status(HttpStatus.OK)
////        .body(messages);
//  }
//}
