package fpt.gymmanagement.repository;

import fpt.gymmanagement.entity.UserCourse;
import fpt.gymmanagement.enums.EUserCourseStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserCourseRepository extends JpaRepository<UserCourse, String> {
    @Query("select uc from UserCourse  uc where uc.status = :status")
    List<UserCourse> findAllByStatus(EUserCourseStatus status);
}
