package fpt.gymmanagement.repository;

import fpt.gymmanagement.entity.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetail, String> {

    void deleteAllByOrderId(String orderId);

    List<OrderDetail> findAllByOrderId(String orderId);

}
