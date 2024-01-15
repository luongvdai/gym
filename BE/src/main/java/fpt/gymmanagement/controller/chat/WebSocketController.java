package fpt.gymmanagement.controller.chat;

import fpt.gymmanagement.dto.ChatDTO;
import fpt.gymmanagement.entity.Chat;
import fpt.gymmanagement.service.impl.ChatServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@Slf4j
public class WebSocketController {
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private ChatServiceImpl chatService;

    @MessageMapping("/chat")
    public void sendMessage(@Payload ChatDTO chatDTO) {
        // Lưu tin nhắn vào database và nhận lại savedChat
        ResponseEntity<?> savedChat = chatService.sendChat(chatDTO);

        log.info("Bạn đã gửi thành công tin nhắn rồi nhé !!!!!");

        // Gửi tin nhắn WebSocket đến người nhận với savedChat
        messagingTemplate.convertAndSendToUser(chatDTO.getReceiverId(), "/queue/messages", savedChat);
    }


//    @MessageMapping("/chat/{receiverId}")
//    public void sendChat(@DestinationVariable String receiverId, @Payload ChatDTO chatDTO) {
//
//        // Lưu tin nhắn vào database
//        ResponseEntity<?> savedChat = chatService.saveChat(chatDTO);
//
//        // Gửi tin nhắn đến người nhận thông qua WebSocket
//        messagingTemplate.convertAndSendToUser(receiverId, "/user/queue/messages", savedChat);
//
//        // Gửi xác nhận về người gửi
//        messagingTemplate.convertAndSendToUser(chatDTO.getSenderId(), "/user/queue/ack", "Received");
//
//        log.info("Message sent to: {} by sender: {}", receiverId, chatDTO.getSenderId());
//    }

}