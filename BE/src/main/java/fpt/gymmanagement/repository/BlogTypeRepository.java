package fpt.gymmanagement.repository;

import fpt.gymmanagement.entity.BlogType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BlogTypeRepository extends JpaRepository<BlogType, String> {
}
