package fpt.gymmanagement.processor;


import fpt.gymmanagement.common.CommonProcessor;
import fpt.gymmanagement.dto.OrderDetailDTO;
import fpt.gymmanagement.dto.search.OrderDetailSearch;
import fpt.gymmanagement.entity.Order;
import fpt.gymmanagement.entity.OrderDetail;
import fpt.gymmanagement.entity.PTCourse;
import fpt.gymmanagement.entity.UserCourse;
import fpt.gymmanagement.enums.EOrderStatus;
import fpt.gymmanagement.service.OrderDetailService;
import fpt.gymmanagement.service.OrderService;
import fpt.gymmanagement.service.PTCourseService;
import fpt.gymmanagement.service.UserCourseService;
import fpt.gymmanagement.transform.OrderDetailTransform;
import fpt.gymmanagement.utils.CommonAppUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class OrderDetailProcessor extends CommonProcessor<OrderDetailService, OrderDetailTransform, OrderDetail, OrderDetailDTO, String> {
    private final OrderService orderService;
    private final UserCourseService userCourseService;
    private final PTCourseService ptCourseService;

    protected OrderDetailProcessor(OrderDetailService service, OrderDetailTransform transform, OrderService orderService, UserCourseService userCourseService, PTCourseService ptCourseService) {
        super(service, transform);
        this.orderService = orderService;
        this.userCourseService = userCourseService;
        this.ptCourseService = ptCourseService;
    }

    public Page<OrderDetailDTO> getPage(OrderDetailSearch search) {
        var page = service.getPage(search);
        List<OrderDetailDTO> content = page.getContent().stream().map(transform::toDto).collect(Collectors.toList());
        return new PageImpl<>(content, PageRequest.of(search.getPage(), search.getSize()),
                page.getTotalElements());
    }

    @Override
    public OrderDetailDTO saveOrUpdate(OrderDetailDTO dto, String s) {
        Order order = orderService.getOrElseThrow(dto.getOrderId());
        PTCourse ptCourse = ptCourseService.getOrElseThrow(dto.getPtCourseId());
        this.checkPrivacy(order, dto, ptCourse);
        if (!CommonAppUtils.isAdmin()) {
            if (!order.getStatus().equals(EOrderStatus.PENDING)
                    || !order.getUser().getUsername().equals(CommonAppUtils.getCurrentAccount())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Không thể sửa thông tin gói đăng kí khóa học!!");
            }
        }
        dto.setPrice(ptCourse.getCourse().getPrice());
        return super.saveOrUpdate(dto, s);
    }

    private void checkPrivacy(Order order, OrderDetailDTO dto, PTCourse ptCourse) {
        if(!ptCourse.getCourse().getBranchId().equals(order.getUser().getBranchId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Không thể đăng kí khóa học khác chi nhánh!!");
        }
        List<UserCourse> userCourses = userCourseService.findAllInProgressByUserId(order.getUserId());
        Set<String> ptCoursesExist = userCourses.stream().map(userCourse -> userCourse.getPtCourse().getId()).collect(Collectors.toSet());

        if (ptCoursesExist.contains(ptCourse.getId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, order.getUser().getFullName() + " đã theo học khóa học " + ptCourse.getCourse().getTitle());
        }

    }

    @Override
    public void deleteById(String id) {
        OrderDetail orderDetail = service.getOrElseThrow(id);
        Order order = orderService.getOrElseThrow(orderDetail.getOrderId());
        if (!CommonAppUtils.isAdmin()) {
            if (!order.getStatus().equals(EOrderStatus.PENDING)
                    || !order.getUser().getUsername().equals(CommonAppUtils.getCurrentAccount())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Không thể xóa thông tin gói đăng kí khóa học!!");
            }
        }
        super.deleteById(id);
    }
}
