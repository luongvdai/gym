package fpt.gymmanagement.service.impl;

import com.querydsl.jpa.impl.JPAQuery;
import fpt.gymmanagement.dto.PTCourseDTO;
import fpt.gymmanagement.dto.search.CoursesOfPTSearch;
import fpt.gymmanagement.dto.search.GymMemberSearch;
import fpt.gymmanagement.dto.search.MentorByUserSearch;
import fpt.gymmanagement.dto.search.PTInCourseSearch;
import fpt.gymmanagement.entity.*;
import fpt.gymmanagement.repository.PTCourseRepository;
import fpt.gymmanagement.service.PTCourseService;
import fpt.gymmanagement.utils.CommonAppUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.EntityManager;
import java.util.Date;
import java.util.Optional;

@Service
public class PTCourseServiceImpl implements PTCourseService {
    private final PTCourseRepository repository;
    private final EntityManager em;
    private static final String message = "Không tìm thấy dữ liệu mentor cho khóa học!!";
    private final QUser user = new QUser("user");
    private final QPTCourse ptCourse = new QPTCourse("ptCourse");
    private final QCourse course = new QCourse("course");
    private final QUserCourse userCourse = new QUserCourse("userCourse");

    public PTCourseServiceImpl(PTCourseRepository repository, EntityManager em) {
        this.repository = repository;
        this.em = em;
    }

    public PTCourse createOrUpdate(PTCourseDTO dto, String id) {
        PTCourse entity;
        if (id != null) {
            entity = repository.findById(id)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, message));
            BeanUtils.copyProperties(dto, entity, "id", "createdDate", "updatedDate");
            return repository.save(entity);
        }
        entity = new PTCourse();
        BeanUtils.copyProperties(dto, entity, "id");
        Long currentTime = new Date().getTime();
        entity.setCreatedDate(currentTime);
        entity.setUpdatedDate(currentTime);
        return repository.save(entity);
    }

    @Override
    public PTCourse saveEntity(PTCourse ptCourse) {
        return repository.save(ptCourse);
    }

    @Override
    public Page<User> getPTsByCourseId(PTInCourseSearch search) {
        JPAQuery<User> query = new JPAQuery<>(em);
        if (search.getIsActive() != null) {
            query.select(user).distinct().from(user)
                    .join(ptCourse).on(user.id.eq(ptCourse.ptId))
                    .join(course).on(ptCourse.courseId.eq(course.id)).where(ptCourse.isActive.eq(search.getIsActive()));
        } else {
            query.select(user).distinct().from(user)
                    .join(ptCourse).on(user.id.eq(ptCourse.ptId))
                    .join(course).on(ptCourse.courseId.eq(course.id));
        }

        if (StringUtils.hasText(search.getBranchId())) {
            query.where(user.branchId.eq(search.getBranchId()));
        }

        if (StringUtils.hasText(search.getCourseId())) {
            query.where(course.id.eq(search.getCourseId()));
        }

        if (StringUtils.hasText(search.getKeyword())) {
            String keyword = search.getKeyword();
            query.where(user.username.containsIgnoreCase(keyword)
                    .or(user.fullName.containsIgnoreCase(keyword)));
        }

        return (Page<User>) CommonAppUtils.getDataPage(query, search.getPage(), search.getSize());
    }

    @Override
    public Page<User> getMemberOfPT(GymMemberSearch search) {
        JPAQuery<User> query = new JPAQuery<>(em);

        if (StringUtils.hasText(search.getPtId())) {
            query.select(user).distinct().from(user)
                    .join(userCourse).on(user.id.eq(userCourse.userId))
                    .join(ptCourse).on(userCourse.ptCourseId.eq(ptCourse.id)).where(ptCourse.ptId.eq(search.getPtId()));
        } else {
            query.select(user).distinct().from(user)
                    .join(userCourse).on(user.id.eq(userCourse.userId))
                    .join(ptCourse).on(userCourse.ptCourseId.eq(ptCourse.id));
        }

        if (StringUtils.hasText(search.getCourseId())) {
            query.where(ptCourse.course.id.eq(search.getCourseId()));
        }

        if (StringUtils.hasText(search.getKeyword())) {
            String keyword = search.getKeyword();
            query.where(ptCourse.course.title.containsIgnoreCase(keyword)
                    .or(user.fullName.containsIgnoreCase(keyword).or(user.username.containsIgnoreCase(keyword))));
        }

        return (Page<User>) CommonAppUtils.getDataPage(query, search.getPage(), search.getSize());
    }

    @Override
    public Page<Course> getCoursesByPT(CoursesOfPTSearch search) {
        JPAQuery<Course> query = new JPAQuery<>(em);
        query.select(course).from(course)
                .join(ptCourse).on(course.id.eq(ptCourse.courseId))
                .join(user).on(ptCourse.ptId.eq(user.id)).where(ptCourse.isActive.eq(Boolean.TRUE));

        if (StringUtils.hasText(search.getPtId())) {
            query.where(user.id.eq(search.getPtId()));
        }

        if (StringUtils.hasText(search.getKeyword())) {
            String keyword = search.getKeyword();
            query.where(course.title.containsIgnoreCase(keyword)
                    .or(course.description.containsIgnoreCase(keyword)));
        }

        return (Page<Course>) CommonAppUtils.getDataPage(query, search.getPage(), search.getSize());
    }

    @Override
    public void removePTByCourseId(String ptId, String courseId) {
        PTCourse entity = repository.findByPtIdAndCourseId(ptId, courseId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, message));
        entity.setIsActive(false);
        repository.save(entity);
    }

    @Override
    public PTCourse getOrElseThrow(String id) {
        return repository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.BAD_REQUEST, "Không tìm thấy gói khóa học!!"));
    }

    @Override
    public Optional<PTCourse> getByPTIdAndCourseId(String ptId, String courseId) {
        return repository.findByPtIdAndCourseId(ptId, courseId);
    }

    @Override
    public PTCourse active(String id) {
        PTCourse entity = this.getOrElseThrow(id);
        entity.setIsActive(true);
        return repository.save(entity);
    }
}
