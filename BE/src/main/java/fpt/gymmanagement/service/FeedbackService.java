package fpt.gymmanagement.service;

import fpt.gymmanagement.common.CommonService;
import fpt.gymmanagement.dto.FeedbackDTO;
import fpt.gymmanagement.dto.response.PersonalTrainerResponse;
import fpt.gymmanagement.dto.search.FeedbackSearch;
import fpt.gymmanagement.entity.Feedback;
import org.springframework.data.domain.Page;

import java.util.Map;

public interface FeedbackService extends CommonService<Feedback, String> {
    Feedback saveOrUpdate(FeedbackDTO dto, String id);

    Feedback getByUserCourseId(String id);

    Page<Feedback> getByPt(FeedbackSearch search);

    PersonalTrainerResponse.StatsFeedback statsStarByPT(String ptId);
}
