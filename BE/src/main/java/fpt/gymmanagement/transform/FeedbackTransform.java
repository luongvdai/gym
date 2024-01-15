package fpt.gymmanagement.transform;

import fpt.gymmanagement.common.CommonTransform;
import fpt.gymmanagement.dto.FeedbackDTO;
import fpt.gymmanagement.entity.Feedback;
import org.mapstruct.Mapper;

@Mapper
public interface FeedbackTransform extends CommonTransform<Feedback, FeedbackDTO> {
}
