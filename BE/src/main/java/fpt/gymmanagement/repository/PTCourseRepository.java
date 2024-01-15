package fpt.gymmanagement.repository;

import fpt.gymmanagement.entity.PTCourse;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PTCourseRepository extends JpaRepository<PTCourse, String> {
    @Query("SELECT m FROM PTCourse m WHERE m.ptId = :ptId AND m.courseId = :courseId")
    Optional<PTCourse> findByPtIdAndCourseId(@Param("ptId") String ptId, @Param("courseId") String courseId);
}
