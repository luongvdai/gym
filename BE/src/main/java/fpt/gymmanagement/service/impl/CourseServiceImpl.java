package fpt.gymmanagement.service.impl;

import com.querydsl.jpa.impl.JPAQuery;
import fpt.gymmanagement.common.CommonJPAServiceImpl;
import fpt.gymmanagement.dto.response.CourseStatsResponse;
import fpt.gymmanagement.dto.search.CourseSearch;
import fpt.gymmanagement.dto.search.UserCourseSearch;
import fpt.gymmanagement.entity.*;
import fpt.gymmanagement.repository.CourseRepository;
import fpt.gymmanagement.service.CourseService;
import fpt.gymmanagement.utils.CommonAppUtils;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import java.util.*;

@Service
public class CourseServiceImpl extends CommonJPAServiceImpl<Course, String, CourseRepository, QCourse>
        implements CourseService {

    private final QUser user = new QUser("user");
    private final QCourse course = new QCourse("course");
    private final QUserCourse userCourse = new QUserCourse("userCourse");
    private final QPTCourse ptCourse = new QPTCourse("ptCourse");

    public CourseServiceImpl(EntityManager em, CourseRepository repo) {
        super(em, QCourse.course, repo);
    }

    @Override
    protected String notFoundMessage() {
        return "Không tìm thấy khóa học!!";
    }

    @Override
    public Page<Course> getPage(CourseSearch search) {
        this.addFilterCourse(search);
        return (Page<Course>) CommonAppUtils.getDataPage(this.query, search.getPage(), search.getSize());
    }

    @Override
    public Page<Course> getCourseByUser(UserCourseSearch search) {
        this.query = new JPAQuery<>(em);
        this.query.select(course).from(course)
                .join(ptCourse).on(course.id.eq(ptCourse.courseId))
                .join(userCourse).on(ptCourse.id.eq(userCourse.ptCourse.id))
                .where(userCourse.userId.eq(search.getUserId()));

        return (Page<Course>) CommonAppUtils.getDataPage(this.query, search.getPage(), search.getSize());
    }

    @Override
    public List<CourseStatsResponse> getTopCourse(Map<String, Integer> courseCountMap) {
        List<Map.Entry<String, Integer>> sortedEntries = new ArrayList<>(courseCountMap.entrySet());
        sortedEntries.sort(Map.Entry.comparingByValue(Comparator.reverseOrder()));
        Map<String, Integer> sortedMap = new LinkedHashMap<>();
        for (Map.Entry<String, Integer> entry : sortedEntries) {
            sortedMap.put(entry.getKey(), entry.getValue());
        }

        List<String> courseIds = new ArrayList<>();
        if (sortedMap.size() > 5) {
            int count = 0;
            for (Map.Entry<String, Integer> entry : sortedMap.entrySet()) {
                if (count < 5) {
                    courseIds.add(entry.getKey());
                } else {
                    break;
                }
                count++;
            }
        } else {
            for (Map.Entry<String, Integer> entry : sortedMap.entrySet()) {
                courseIds.add(entry.getKey());
            }
        }
        List<Course> courses = repo.getByCourseIds(courseIds);
        List<CourseStatsResponse> responses = new ArrayList<>();
        int count = 0;
        for (Map.Entry<String, Integer> entry : sortedMap.entrySet()) {
            if (count < 5) {
                for (Course course : courses) {
                    if (entry.getKey().equals(course.getId())) {
                        CourseStatsResponse data = new CourseStatsResponse();
                        data.setCourseId(course.getId());
                        data.setCourseName(course.getTitle());
                        data.setBranchId(course.getBranchId());
                        data.setBranchName(course.getBranch().getName());
                        data.setTotal(entry.getValue());
                        responses.add(data);
                    }
                }
            } else {
                break;
            }
            count++;
        }
        return responses;
    }

    private void addFilterCourse(CourseSearch search) {
        this.query = new JPAQuery<>(em);
        this.query.select(q).from(q);

        if (StringUtils.hasText(search.getKeyword())) {
            String keyword = search.getKeyword();
            this.query.where(q.title.containsIgnoreCase(keyword)
                    .or(q.description.containsIgnoreCase(keyword)));
        }

        if (StringUtils.hasText(search.getBranchId())) {
            this.query.where(q.branchId.eq(search.getBranchId()));
        }

        if (StringUtils.hasText(search.getTypeId())) {
            this.query.where(q.type.id.eq(search.getTypeId()));
        }

        if (search.getRangePrice() != null) {
            this.convertQueryByPrice(search.getRangePrice());
        }

        if (search.getRangeProcess() != null) {
            this.convertQueryByProcess(search.getRangeProcess());
        }

        if (search.getIsActive() == null) {
            this.query.where(q.isActive.eq(true));
        }

        if (search.getNewest() != null && Boolean.TRUE.equals(search.getNewest())) {
            this.query.orderBy(q.createdDate.desc(), q.updatedDate.desc());
        } else {
            this.query.orderBy(q.updatedDate.desc(), q.createdDate.desc());
        }
    }

    private void convertQueryByPrice(Integer rangePrice) {
        switch (rangePrice) {
            case 1: {
                this.query.where(q.price.gt(-1).and(q.price.lt(500001))); // [0, 500.000]
                return;
            }
            case 2: {
                this.query.where(q.price.gt(499999).and(q.price.lt(1000001))); // [500.000, 1.000.000]
                return;
            }
            case 3: {
                this.query.where(q.price.in(999999).and(q.price.lt(2000001))); // [1.000.000, 2.000.000]
                return;
            }
            default: {
                this.query.where(q.price.gt(1999999)); // >=2.000.000
            }
        }
    }

    private void convertQueryByProcess(Integer rangeProcess) {
        if (rangeProcess == 13) {
            this.query.where(q.rangeProcess.gt(12)); // >= 12
        } else {
            this.query.where(q.rangeProcess.eq(rangeProcess));
        }
    }
}
