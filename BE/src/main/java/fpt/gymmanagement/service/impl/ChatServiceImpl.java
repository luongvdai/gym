package fpt.gymmanagement.service.impl;

import fpt.gymmanagement.dto.ChatDTO;
import fpt.gymmanagement.entity.Chat;
import fpt.gymmanagement.entity.User;
import fpt.gymmanagement.repository.ChatRepository;
import fpt.gymmanagement.repository.UserRepository;
import fpt.gymmanagement.service.UserService;
import fpt.gymmanagement.utils.CommonAppUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;



@Service
public class ChatServiceImpl {
    private ChatRepository chatRepository;

    private UserRepository userRepository;

    private UserService userService;

    private final SimpMessagingTemplate messagingTemplate;

    public ChatServiceImpl(ChatRepository chatRepository,
                           UserRepository userRepository,
                           UserService userService,
                           SimpMessagingTemplate messagingTemplate) {
        this.chatRepository = chatRepository;
        this.userRepository = userRepository;
        this.userService = userService;
        this.messagingTemplate = messagingTemplate;
    }

    public ResponseEntity<?> sendChat(ChatDTO chatDTO) {
        // Chuyển đổi ChatDTO thành Chat entity
        User user = userService.getByUsername(CommonAppUtils.getCurrentAccount());
        Optional<User> optionalReceiver = userRepository.findById(chatDTO.getReceiverId());
        if (optionalReceiver.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Receiver not found");
        }
        User receiver = optionalReceiver.get();
        Chat chat = new Chat();
        chat.setSenderId(user.getId());
        chat.setReceiverId(receiver.getId());
        chat.setContent(chatDTO.getContent());

        // Lưu tin nhắn vào database
        Chat savedChat = chatRepository.save(chat);

        // Gửi tin nhắn WebSocket đến người nhận với savedChat
        messagingTemplate.convertAndSendToUser(chatDTO.getReceiverId(), "/queue/messages", savedChat);

        return ResponseEntity.ok(savedChat);
    }


    public ResponseEntity<?> saveChat(ChatDTO chatDTO) {

      // User user = userService.getByUsername(CommonAppUtils.getCurrentAccount());

        // Lấy thông tin người gửi từ database
        Optional<User> optionalSender = userRepository.findById(chatDTO.getSenderId());
        if (optionalSender.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Sender not found");
        }
        User sender = optionalSender.get();

        // Lấy thông tin người nhận từ database
        Optional<User> optionalReceiver = userRepository.findById(chatDTO.getReceiverId());
        if (optionalReceiver.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Receiver not found");
        }
        User receiver = optionalReceiver.get();

        // Kiểm tra nếu người gửi và người nhận là cùng một người
//        if (sender.getId().equals(receiver.getId())) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Sender and receiver cannot be the same");
//        }

        // Tạo đối tượng Chat từ DTO
        Chat chat = new Chat();
        chat.setSenderId(sender.getId());
        chat.setReceiverId(receiver.getId());
        chat.setContent(chatDTO.getContent());

        // Lưu tin nhắn vào database
        Chat savedChat = chatRepository.save(chat);

        // Gửi tin nhắn đến người nhận thông qua WebSocket
        messagingTemplate.convertAndSendToUser(chatDTO.getReceiverId(), "/queue/messages", chatDTO);
       // messagingTemplate.convertAndSendToUser(receiver.getId(), "/queue/messages", new ChatDTO(savedChat));

        return ResponseEntity.ok(savedChat);
    }


    public List<ChatDTO> loadChats(String senderId, String receiverId) {
        // Truy vấn database để lấy tin nhắn giữa hai người dùng
        List<Chat> chatList = chatRepository.findBySenderIdAndReceiverIdOrSenderIdAndReceiverIdOrderByCreatedDate(
                senderId, receiverId, receiverId, senderId
        );

        // Chuyển đổi danh sách tin nhắn thành danh sách DTO để trả về
        List<ChatDTO> chatDTOList = new ArrayList<>();
        for (Chat chat : chatList) {
            ChatDTO chatDTO = new ChatDTO();
            chatDTO.setId(chat.getId());
            chatDTO.setContent(chat.getContent());
            chatDTO.setSenderId(chat.getSenderId());
            chatDTO.setReceiverId(chat.getReceiverId());
            chatDTO.setCreatedDate(chat.getCreatedDate()); // Đảm bảo trường này có getter trong đối tượng Chat

            // Thêm vào danh sách kết quả
            chatDTOList.add(chatDTO);
        }

        return chatDTOList;
    }



}
