package fpt.gymmanagement.service.impl;

import com.querydsl.jpa.impl.JPAQuery;
import fpt.gymmanagement.common.CommonJPAServiceImpl;
import fpt.gymmanagement.dto.request.usercourse.CourseUserSearch;
import fpt.gymmanagement.dto.search.MentorByUserSearch;
import fpt.gymmanagement.dto.search.UserCourseSearch;
import fpt.gymmanagement.entity.*;
import fpt.gymmanagement.enums.EUserCourseStatus;
import fpt.gymmanagement.repository.UserCourseRepository;
import fpt.gymmanagement.service.UserCourseService;
import fpt.gymmanagement.utils.CommonAppUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import java.util.List;

@Service
@Slf4j
public class UserCourseServiceImpl extends CommonJPAServiceImpl<UserCourse, String, UserCourseRepository, QUserCourse>
        implements UserCourseService {

    private final QUserCourse userCourse = new QUserCourse("userCourse");

    private final QUser user = new QUser("user");

    private final QPTCourse ptCourse = new QPTCourse("ptCourse");


    public UserCourseServiceImpl(EntityManager em, UserCourseRepository repo) {
        super(em, QUserCourse.userCourse, repo);
    }

    @Override
    public void saveByList(List<UserCourse> userCourses) {
        repo.saveAll(userCourses);
    }

    @Override
    public Page<UserCourse> getPage(UserCourseSearch search) {
        this.addFilter(search);
        return (Page<UserCourse>) CommonAppUtils.getDataPage(this.query, search.getPage(), search.getSize());
    }

    @Override
    public Page<User> getMentorByUser(MentorByUserSearch search) {
        this.query = new JPAQuery<>(em);
        this.query.select(user).distinct().from(user)
                .join(ptCourse).on(user.id.eq(ptCourse.ptId))
                .join(userCourse).on(ptCourse.id.eq(userCourse.ptCourse.id))
                .where(userCourse.userId.eq(search.getUserId()));

        if (StringUtils.hasText(search.getKeyword())) {
            this.query.where(ptCourse.user.fullName.containsIgnoreCase(search.getKeyword())
                    .or(ptCourse.user.username.containsIgnoreCase(search.getKeyword())));
        }

        return (Page<User>) CommonAppUtils.getDataPage(this.query, search.getPage(), search.getSize());
    }


    @Override
    public Page<UserCourse> getByMentor(CourseUserSearch search) {
        this.query = new JPAQuery<>(em);
        this.query.select(q).distinct().from(q).where(q.ptCourse.ptId.eq(search.getPtId()));
        if (StringUtils.hasText(search.getKeyword())) {
            this.query.where(q.ptCourse.course.title.containsIgnoreCase(search.getKeyword()));
        }

        if (StringUtils.hasText(search.getCourseId())) {
            this.query.where(q.ptCourse.course.id.eq(search.getCourseId()));
        }

        if (search.getStatus() != null) {
            this.query.where(q.status.eq(search.getStatus()));
        }

        this.query.orderBy(q.createdDate.desc(), q.updatedDate.desc());
        return (Page<UserCourse>) CommonAppUtils.getDataPage(this.query, search.getPage(), search.getSize());
    }

    @Override
    public List<UserCourse> findAllInProgress() {
        return repo.findAllByStatus(EUserCourseStatus.IN_PROGRESS);
    }

    @Override
    public List<UserCourse> findAllInProgressByUserId(String userId) {
        this.query = new JPAQuery<>(em);
        this.query.select(q).from(q).where(q.userId.eq(userId).and(q.status.eq(EUserCourseStatus.IN_PROGRESS)));
        return this.query.fetch();
    }

    private void addFilter(UserCourseSearch search) {
        this.query = new JPAQuery<>(em);
        this.query.select(q).from(q)
                .where(q.userId.eq(search.getUserId()));

        if (StringUtils.hasText(search.getKeyword())) {
            String keyword = search.getKeyword();
            this.query.where(q.ptCourse.user.fullName.containsIgnoreCase(keyword)
                    .or(q.ptCourse.course.title.containsIgnoreCase(keyword)));
        }

        if (search.getStatus() != null) {
            this.query.where(q.status.eq(search.getStatus()));
        }

        this.query.orderBy(q.updatedDate.desc(), q.createdDate.desc());
    }

    @Override
    protected String notFoundMessage() {
        return "Không tìm thấy khóa học của người dùng!!!!";
    }
}
