package fpt.gymmanagement.repository;

import fpt.gymmanagement.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, String> {
    @Query("select o from Order o where o.id in :ids")
    List<Order> getByIds(List<String> ids);
}
