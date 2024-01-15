package fpt.gymmanagement.repository;

import fpt.gymmanagement.entity.GuestConsultant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GuestConsultantRepository extends JpaRepository<GuestConsultant, String> {
}
