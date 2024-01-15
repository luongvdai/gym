package fpt.gymmanagement.repository;

import fpt.gymmanagement.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, String> {
    @Query("select c from Course c where c.id in :ids")
    List<Course> getByCourseIds(List<String> ids);
}
