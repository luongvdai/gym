package fpt.gymmanagement.service.impl;

import com.querydsl.jpa.impl.JPAQuery;
import fpt.gymmanagement.common.CommonJPAServiceImpl;
import fpt.gymmanagement.dto.FeedbackDTO;
import fpt.gymmanagement.dto.response.PersonalTrainerResponse;
import fpt.gymmanagement.dto.search.FeedbackSearch;
import fpt.gymmanagement.entity.Feedback;
import fpt.gymmanagement.entity.QFeedback;
import fpt.gymmanagement.repository.FeedbackRepository;
import fpt.gymmanagement.service.FeedbackService;
import fpt.gymmanagement.utils.CommonAppUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.EntityManager;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class FeedbackServiceImpl extends CommonJPAServiceImpl<Feedback, String, FeedbackRepository, QFeedback>
        implements FeedbackService {

    public FeedbackServiceImpl(EntityManager em, FeedbackRepository repo) {
        super(em, QFeedback.feedback, repo);
    }

    @Override
    protected String notFoundMessage() {
        return "Không tìm thấy đánh giá!!";
    }


    @Override
    public Feedback saveOrUpdate(FeedbackDTO dto, String id) {
        Feedback entity;
        Long currentTime = new Date().getTime();
        if (id != null) {
            entity = super.getOrElseThrow(id);
            BeanUtils.copyProperties(dto, entity, "id", "createdDate");
            entity.setUpdatedDate(currentTime);
            return repo.save(entity);
        }
        entity = new Feedback();
        BeanUtils.copyProperties(dto, entity, "id");
        entity.setCreatedDate(currentTime);
        entity.setUpdatedDate(currentTime);
        return repo.save(entity);
    }

    @Override
    public Feedback getByUserCourseId(String id) {
        return repo.findByUserCourseId(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, notFoundMessage()));
    }

    @Override
    public Page<Feedback> getByPt(FeedbackSearch search) {
        this.query = new JPAQuery<>(em);
        if (StringUtils.hasText(search.getPtId())) {
            this.query.select(q).from(q).where(q.userCourse.ptCourse.ptId.eq(search.getPtId()));
        } else {
            this.query.select(q).from(q);
        }
        this.query.orderBy(q.updatedDate.desc(), q.createdDate.desc());
        return (Page<Feedback>) CommonAppUtils.getDataPage(this.query, search.getPage(), search.getSize());
    }

    @Override
    public PersonalTrainerResponse.StatsFeedback statsStarByPT(String ptId) {
        this.query = new JPAQuery<>(em);
        this.query.select(q).from(q).where(q.userCourse.ptCourse.ptId.eq(ptId));
        List<Feedback> feedbacks = this.query.fetch();
        double stats = 0;
        int total = 0;
        for (Feedback f : feedbacks) {
            stats += f.getStar();
            total++;
        }
        if (stats != 0 && total != 0) {
            return PersonalTrainerResponse.StatsFeedback.builder().average((stats/total)).total(total).build();
        }
        return PersonalTrainerResponse.StatsFeedback.builder().build();
    }

    @Override
    public void deleteById(String s) {
        repo.deleteById(s);
    }
}
