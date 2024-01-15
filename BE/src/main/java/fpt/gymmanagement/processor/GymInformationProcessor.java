package fpt.gymmanagement.processor;

import fpt.gymmanagement.common.CommonProcessor;
import fpt.gymmanagement.dto.GymInformationDTO;
import fpt.gymmanagement.dto.search.GymInformationSearch;
import fpt.gymmanagement.entity.GymInformation;
import fpt.gymmanagement.entity.User;
import fpt.gymmanagement.entity.UserCourse;
import fpt.gymmanagement.service.GymInformationService;
import fpt.gymmanagement.service.UserCourseService;
import fpt.gymmanagement.service.UserService;
import fpt.gymmanagement.transform.CourseTransform;
import fpt.gymmanagement.transform.GymInformationTransform;
import fpt.gymmanagement.transform.UserTransform;
import fpt.gymmanagement.utils.CommonAppUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
public class GymInformationProcessor extends CommonProcessor<GymInformationService, GymInformationTransform, GymInformation, GymInformationDTO, String> {

    private final UserService userService;
    private final UserCourseService userCourseService;
    private final GymInformationService service;
    private final UserTransform userTransform;
    private final CourseTransform courseTransform;

    protected GymInformationProcessor(GymInformationService service, GymInformationTransform transform, UserService userService, UserCourseService userCourseService, UserTransform userTransform, CourseTransform courseTransform) {
        super(service, transform);
        this.userService = userService;
        this.service = service;
        this.userCourseService = userCourseService;
        this.userTransform = userTransform;
        this.courseTransform = courseTransform;
    }

    public Page<GymInformationDTO> getPage(GymInformationSearch search) {
        User user = userService.getByUsername(CommonAppUtils.getCurrentAccount());
        search.setUserId(user.getId());
        var page = service.getPage(search);
        List<GymInformationDTO> content = this.processDTO(page);
        return new PageImpl<>(content, PageRequest.of(search.getPage(), search.getSize()),
                page.getTotalElements());
    }

    public Page<GymInformationDTO> getPageAllUserGymInformation(GymInformationSearch search) {
        User user = userService.getByUsername(CommonAppUtils.getCurrentAccount());
        search.setPtId(user.getId());
        var page = service.getPagePtGymInformation(search);
        List<GymInformationDTO> content = this.processDTO(page);
        return new PageImpl<>(content, PageRequest.of(search.getPage(), search.getSize()),
                page.getTotalElements());
    }

    private List<GymInformationDTO> processDTO(Page<GymInformation> page) {
        return page.getContent().stream().map(gi -> {
            GymInformationDTO dto = transform.toDto(gi);
            dto.setMentor(userTransform.toDto(gi.getUserCourse().getPtCourse().getUser()));
            dto.setCourse(courseTransform.toDto(gi.getUserCourse().getPtCourse().getCourse()));
            return dto;
        }).collect(Collectors.toList());
    }

    @Override
    public GymInformationDTO saveOrUpdate(GymInformationDTO gymInformationDTO, String id) {
        User user = userService.getByUsername(CommonAppUtils.getCurrentAccount());
        UserCourse userCourse = userCourseService.getOrElseThrow(gymInformationDTO.getUserCourseId());
        if (!userCourse.getPtCourse().getPtId().equals(user.getId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bạn không phải người hướng dẫn người dùng này!!");
        }
        return transform.toDto(service.saveOrUpdate(gymInformationDTO, id));
    }
}