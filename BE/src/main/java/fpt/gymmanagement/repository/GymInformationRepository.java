package fpt.gymmanagement.repository;

import fpt.gymmanagement.entity.GymInformation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GymInformationRepository extends JpaRepository<GymInformation, String> {
}
