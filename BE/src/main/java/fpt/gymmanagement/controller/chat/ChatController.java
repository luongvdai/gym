package fpt.gymmanagement.controller.chat;

import fpt.gymmanagement.dto.ChatDTO;
import fpt.gymmanagement.entity.User;
import fpt.gymmanagement.repository.UserRepository;
import fpt.gymmanagement.service.UserService;
import fpt.gymmanagement.service.impl.ChatServiceImpl;
import fpt.gymmanagement.utils.CommonAppUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chats")
@Slf4j
public class ChatController {
    @Autowired
    private ChatServiceImpl chatService;

    @Autowired
    private UserService userService;

    private final SimpMessagingTemplate messagingTemplate;

    public ChatController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    private String getCurrentUserId() {
        // implement logic to get current user id
        User user = userService.getByUsername(CommonAppUtils.getCurrentAccount());
        return user.getId();
    }


    @PostMapping("/send")
    public ResponseEntity<?> sendChat(@RequestBody ChatDTO chatDTO) {
        // Lưu tin nhắn vào database và nhận lại savedChat
        ResponseEntity<?> savedChat = chatService.sendChat(chatDTO);

        // Gửi tin nhắn WebSocket đến người nhận với savedChat
        messagingTemplate.convertAndSendToUser(chatDTO.getReceiverId(), "/queue/messages", savedChat);

        return ResponseEntity.ok("ReceverId: " + chatDTO.getReceiverId() + ", Content: " + chatDTO.getContent());
    }

    // Trong ChatController
    @GetMapping("/load/{receiverId}")
    public ResponseEntity<List<ChatDTO>> loadChats(@PathVariable String receiverId) {
        // Lấy tin nhắn từ database giữa người đăng nhập và người nhận
        List<ChatDTO> chats = chatService.loadChats(getCurrentUserId(), receiverId);
        return ResponseEntity.ok(chats);
    }



    // API để gửi tin nhắn từ client (Postman)
//    @PostMapping("/send")
//    public ResponseEntity<?> sendChat(@RequestBody ChatDTO chatDTO) {
//        User senderId = userService.getByUsername(CommonAppUtils.getCurrentAccount());
//        // Set senderId từ người đang đăng nhập
//        chatDTO.setSenderId(senderId.getId());
//        log.info("SenderId: " +  senderId.getId());
//
//        // Gọi service để lưu tin nhắn vào database
//        chatService.saveChat(chatDTO);
//
//        return ResponseEntity.ok("Message sent successfully by:" + senderId.getId() + "Content: " + chatDTO.getContent());
//    }
}
