package fpt.gymmanagement.processor;

import fpt.gymmanagement.dto.PrepareOrderDTO;
import fpt.gymmanagement.dto.search.PrepareOrderSearch;
import fpt.gymmanagement.entity.PTCourse;
import fpt.gymmanagement.entity.User;
import fpt.gymmanagement.service.PTCourseService;
import fpt.gymmanagement.service.PrepareOrderService;
import fpt.gymmanagement.service.UserService;
import fpt.gymmanagement.transform.PrepareOrderTransform;
import fpt.gymmanagement.utils.CommonAppUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class PrepareOrderProcessor {
    private final PTCourseService ptCourseService;
    private final PrepareOrderTransform transform;
    private final PrepareOrderService service;
    private final UserService userService;

    public Page<PrepareOrderDTO> getAll(PrepareOrderSearch search) {
        User user = userService.getByUsername(CommonAppUtils.getCurrentAccount());
        search.setUserId(user.getId());
        var page = service.getAll(search);
        List<PrepareOrderDTO> content = page.getContent().stream().map(transform::toDto).collect(Collectors.toList());
        return new PageImpl<>(content, PageRequest.of(search.getPage(), search.getSize()),
                page.getTotalElements());
    }

    public PrepareOrderDTO getById(String id) {
        return transform.toDto(service.getById(id));
    }

    public PrepareOrderDTO create(PrepareOrderDTO dto) {
        PTCourse ptCourse = ptCourseService.getOrElseThrow(dto.getPtCourseId());
        User user = userService.getByUsername(CommonAppUtils.getCurrentAccount());
        service.checkAlreadyExist(dto.getPtCourseId(), user.getId());
        if(dto.getTitle() == null || dto.getTitle().isEmpty()) {
            dto.setTitle(ptCourse.getCourse().getTitle());
        }
        dto.setUserId(user.getId());
        return transform.toDto(service.create(dto));
    }

    public void remove(String id) {
        service.deleteById(id);
    }
}
