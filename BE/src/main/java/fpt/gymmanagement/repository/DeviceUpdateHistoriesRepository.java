package fpt.gymmanagement.repository;

import fpt.gymmanagement.entity.DeviceUpdateHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeviceUpdateHistoriesRepository extends JpaRepository<DeviceUpdateHistory, String> {
}
