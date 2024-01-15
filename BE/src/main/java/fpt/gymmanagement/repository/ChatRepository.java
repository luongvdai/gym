package fpt.gymmanagement.repository;

import fpt.gymmanagement.entity.Chat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatRepository extends JpaRepository<Chat, String> {
    List<Chat> findBySenderIdAndReceiverId(String senderId, String receiverId);

    List<Chat> findBySenderIdAndReceiverIdOrSenderIdAndReceiverIdOrderByCreatedDate(
            String senderId1, String receiverId1, String senderId2, String receiverId2
    );
}
