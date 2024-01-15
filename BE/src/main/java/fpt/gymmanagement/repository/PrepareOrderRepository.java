package fpt.gymmanagement.repository;

import fpt.gymmanagement.entity.PrepareOrder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PrepareOrderRepository extends JpaRepository<PrepareOrder, String> {
    Optional<PrepareOrder> findByPtCourseIdAndUserId(String ptCourseId, String userId);
}
