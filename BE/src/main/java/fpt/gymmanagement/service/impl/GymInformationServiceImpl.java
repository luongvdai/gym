package fpt.gymmanagement.service.impl;

import com.querydsl.jpa.impl.JPAQuery;
import fpt.gymmanagement.common.CommonJPAServiceImpl;
import fpt.gymmanagement.dto.GymInformationDTO;
import fpt.gymmanagement.dto.search.GymInformationSearch;
import fpt.gymmanagement.entity.GymInformation;
import fpt.gymmanagement.entity.QGymInformation;
import fpt.gymmanagement.repository.GymInformationRepository;
import fpt.gymmanagement.service.GymInformationService;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import java.util.Date;

@Service
public class GymInformationServiceImpl extends CommonJPAServiceImpl<GymInformation, String, GymInformationRepository, QGymInformation>
        implements GymInformationService {
    public GymInformationServiceImpl(EntityManager em, GymInformationRepository repo) {
        super(em, QGymInformation.gymInformation, repo);
    }

    @Override
    protected String notFoundMessage() {
        return "Không tìm thấy thông tin!!";
    }

    @Override
    public Page<GymInformation> getPage(GymInformationSearch search) {
        this.addFilterGymInfo(search);
        long count = this.query.stream().count();
        var data = this.query.offset((long) search.getPage() * search.getSize()).limit(search.getSize()).fetch();
        return new PageImpl<>(data, PageRequest.of(search.getPage(), search.getSize()), count);
    }

    @Override
    public Page<GymInformation> getPagePtGymInformation(GymInformationSearch search) {
        this.addFilterGymInformation(search);
        long count = this.query.stream().count();
        var data = this.query.offset((long) search.getPage() * search.getSize()).limit(search.getSize()).fetch();
        return new PageImpl<>(data, PageRequest.of(search.getPage(), search.getSize()), count);
    }

    private void addFilterGymInformation(GymInformationSearch search) {

        this.query = new JPAQuery<>(em);
        this.query.select(q).from(q);

        if (StringUtils.hasText(search.getPtId())) {
            this.query.where(q.userCourse.ptCourse.ptId.eq(search.getPtId()));
        }

        if (StringUtils.hasText(search.getUserId())) {
            this.query.where(q.userId.eq(search.getUserId()));
        }

        if (StringUtils.hasText(search.getCourseId())) {
            this.query.where(q.userCourse.ptCourse.courseId.eq(search.getCourseId()));
        }

        this.query.orderBy(q.updatedDate.desc(), q.createdDate.desc());
    }

    @Override
    public GymInformation saveOrUpdate(GymInformationDTO dto, String id) {
        GymInformation entity;
        if (id != null) {
            entity = super.getOrElseThrow(id);
            BeanUtils.copyProperties(dto, entity, "id", "createdDate");
            entity.setUpdatedDate(new Date().getTime());
            return repo.save(entity);
        }
        entity = new GymInformation();

        if (dto.getCreatedDate() == null) {
            long currentDate = new Date().getTime();
            dto.setCreatedDate(currentDate);
            dto.setUpdatedDate(currentDate);
        } else {
            dto.setUpdatedDate(dto.getCreatedDate());
        }
        BeanUtils.copyProperties(dto, entity, "id");
        return repo.save(entity);
    }

    private void addFilterGymInfo(GymInformationSearch search) {
        this.query = new JPAQuery<>(em);
        this.query.select(q).from(q);
        if (StringUtils.hasText(search.getUserId())) {
            this.query.where(q.userId.eq(search.getUserId()));
        }

        this.query.orderBy(q.updatedDate.desc(), q.createdDate.desc());
    }
}
