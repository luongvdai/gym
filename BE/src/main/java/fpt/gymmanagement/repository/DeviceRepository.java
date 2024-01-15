package fpt.gymmanagement.repository;

import fpt.gymmanagement.entity.Device;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeviceRepository extends JpaRepository<Device, String> {
    Device findByName(String name);
}
