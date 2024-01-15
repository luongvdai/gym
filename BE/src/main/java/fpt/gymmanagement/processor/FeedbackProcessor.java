package fpt.gymmanagement.processor;

import fpt.gymmanagement.dto.FeedbackDTO;
import fpt.gymmanagement.dto.request.FeedbackUnlockRequest;
import fpt.gymmanagement.dto.search.FeedbackSearch;
import fpt.gymmanagement.entity.Feedback;
import fpt.gymmanagement.entity.User;
import fpt.gymmanagement.entity.UserCourse;
import fpt.gymmanagement.enums.EUserCourseStatus;
import fpt.gymmanagement.service.FeedbackService;
import fpt.gymmanagement.service.UserCourseService;
import fpt.gymmanagement.service.UserService;
import fpt.gymmanagement.transform.FeedbackTransform;
import fpt.gymmanagement.utils.CommonAppUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Transactional
public class FeedbackProcessor {
    private final FeedbackService service;
    private final UserService userService;
    private final FeedbackTransform transform;
    private final UserCourseService userCourseService;

    public FeedbackProcessor(FeedbackService service, UserService userService, FeedbackTransform transform, UserCourseService userCourseService) {
        this.service = service;
        this.userService = userService;
        this.transform = transform;
        this.userCourseService = userCourseService;
    }

    public FeedbackDTO create(FeedbackDTO dto, String id) {
        UserCourse userCourse = userCourseService.getOrElseThrow(dto.getUserCourseId());
        User user = userService.getByUsername(CommonAppUtils.getCurrentAccount());
        if (!user.getId().equals(userCourse.getUserId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Sai yêu cầu!!");//Case không phải chính chủ khóa học!!
        }
        if (userCourse.getStatus().equals(EUserCourseStatus.COMPLETE_COURSE)) {
            dto.setSenderId(user.getId());
            Feedback feedback = service.saveOrUpdate(dto, id);
            userCourse.setStatus(EUserCourseStatus.ALREADY_FEEDBACK);
            userCourseService.saveOrUpdate(userCourse, userCourse.getId());
            return transform.toDto(feedback);
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Không thể viết đánh giá!!");
    }

    public FeedbackDTO update(FeedbackDTO dto, String id) {
        Feedback feedback = service.getOrElseThrow(id);
        this.checkPrivacyFB(feedback);
        BeanUtils.copyProperties(dto, feedback, "id", "createdDate", "userCourseId", "senderId");
        return transform.toDto(service.saveOrUpdate(feedback, id));
    }

    public FeedbackDTO getById(String id) {
        return transform.toDto(service.getByUserCourseId(id));
    }

    public Page<FeedbackDTO> getByPt(FeedbackSearch search) {
        if (CommonAppUtils.isMentor()) {
            User user = userService.getByUsername(CommonAppUtils.getCurrentAccount());
            search.setPtId(user.getId());
        }
        var page = service.getByPt(search);
        List<FeedbackDTO> content = page.getContent().stream().map(transform::toDto).collect(Collectors.toList());
        return new PageImpl<>(content, PageRequest.of(search.getPage(), search.getSize()),
                page.getTotalElements());
    }

    public void deleteById(String id) {
        Feedback feedback = service.getOrElseThrow(id);
        this.checkPrivacyFB(feedback);
        service.deleteById(id);
        //Sau khi xoá feedback thì userCourse trở về trạng thái COMPLETE COURSE
        UserCourse uc = userCourseService.getOrElseThrow(feedback.getUserCourseId());
        uc.setStatus(EUserCourseStatus.COMPLETE_COURSE);
        userCourseService.saveOrUpdate(uc, uc.getId());

    }

    //ONLY ADMIN, MANAGER
    public void unlockFeedback(FeedbackUnlockRequest request) {
        Feedback feedback = service.getOrElseThrow(request.getFeedbackId());
        String branchId = feedback.getSender().getBranchId();
        if (!CommonAppUtils.isAdmin()) {
            User user = userService.getByUsername(CommonAppUtils.getCurrentAccount());
            if (!user.getBranchId().equals(branchId)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bạn không có quyền thực thi hành động này!!");
            }
        }
        feedback.setIsEnable(false);
        service.saveOrUpdate(feedback, feedback.getId());
    }

    private void checkPrivacyFB(Feedback feedback) {
        if (!CommonAppUtils.isAdmin()) {
            User user = userService.getByUsername(CommonAppUtils.getCurrentAccount());
            if (user.getRole().getPosition().equals("MANAGER")) {
                if (!feedback.getUserCourse().getPtCourse().getCourse().getBranchId().equals(user.getBranchId())) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Yêu cầu không đúng!!");
                }
            } else {
                if (!feedback.getSenderId().equals(user.getId())) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Yêu cầu không đúng!!");
                }
            }
        }
    }
}
