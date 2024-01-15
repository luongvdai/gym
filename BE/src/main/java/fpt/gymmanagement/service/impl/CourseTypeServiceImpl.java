package fpt.gymmanagement.service.impl;

import com.querydsl.jpa.impl.JPAQuery;
import fpt.gymmanagement.dto.CourseTypeDTO;
import fpt.gymmanagement.dto.search.CourseTypeSearch;
import fpt.gymmanagement.entity.CourseType;
import fpt.gymmanagement.entity.QCourseType;
import fpt.gymmanagement.repository.CourseTypeRepository;
import fpt.gymmanagement.service.CourseTypeService;
import fpt.gymmanagement.utils.CommonAppUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.EntityManager;
import java.util.Date;

@Service
@Slf4j
@RequiredArgsConstructor
public class CourseTypeServiceImpl implements CourseTypeService {
    private final CourseTypeRepository repository;
    private final EntityManager em;

    @Override
    public Page<CourseType> getAll(CourseTypeSearch search) {
        QCourseType type = QCourseType.courseType;
        JPAQuery<CourseType> query = new JPAQuery<>(em);
        query.select(type).from(type);
        if (StringUtils.hasText(search.getKeyword())) {
            query.where(type.name.containsIgnoreCase(search.getKeyword()));
        }
        return (Page<CourseType>) CommonAppUtils.getDataPage(query, search.getPage(), search.getSize());
    }

    @Override
    public CourseType getOrElseThrow(String id) {
        return repository.findById(id)
                .orElseThrow(()
                        -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy kiểu khóa học!!"));
    }

    @Override
    public CourseType create(CourseTypeDTO dto) {
        CourseType type = new CourseType();
        type.setName(dto.getName());
        Long currentTime = new Date().getTime();
        type.setCreatedDate(currentTime);
        type.setUpdatedDate(currentTime);
        return repository.save(type);
    }

    @Override
    public CourseType update(CourseTypeDTO dto, String id) {
        CourseType type = getOrElseThrow(id);
        type.setName(dto.getName());
        Long currentTime = new Date().getTime();
        type.setUpdatedDate(currentTime);
        return repository.save(type);
    }

    @Override
    public void delete(String id) {
        CourseType type = getOrElseThrow(id);
        repository.deleteById(type.getId());
    }
}
