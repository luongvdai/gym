package fpt.gymmanagement.processor;

import fpt.gymmanagement.common.CommonProcessor;
import fpt.gymmanagement.config.VNPAYConfig;
import fpt.gymmanagement.dto.ManageOrderDTO;
import fpt.gymmanagement.dto.OrderDTO;
import fpt.gymmanagement.dto.UserDTO;
import fpt.gymmanagement.dto.request.order.*;
import fpt.gymmanagement.dto.search.OrderSearch;
import fpt.gymmanagement.entity.*;
import fpt.gymmanagement.enums.EOrderStatus;
import fpt.gymmanagement.enums.EUserCourseSlot;
import fpt.gymmanagement.enums.EUserCourseStatus;
import fpt.gymmanagement.service.*;
import fpt.gymmanagement.transform.ManageOrderTransform;
import fpt.gymmanagement.transform.OrderTransform;
import fpt.gymmanagement.transform.UserTransform;
import fpt.gymmanagement.utils.CommonAppUtils;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@Component
@Transactional
@Slf4j
public class OrderProcessor extends CommonProcessor<OrderService, OrderTransform, Order, OrderDTO, String> {

    private final UserService userService;
    private final OrderDetailService orderDetailService;
    private final CourseService courseService;
    private final PTCourseService ptCourseService;
    private final UserCourseService userCoursesService;
    private final ManageOrderTransform manageOrderTransform;
    private final UserTransform userTransform;

    protected OrderProcessor(OrderService service, OrderTransform transform,
                             UserService userService, OrderDetailService orderDetailService,
                             CourseService courseService, PTCourseService ptCourseService,
                             UserCourseService userCoursesService, ManageOrderTransform manageOrderTransform, UserTransform userTransform) {
        super(service, transform);
        this.userService = userService;
        this.orderDetailService = orderDetailService;
        this.courseService = courseService;
        this.ptCourseService = ptCourseService;
        this.userCoursesService = userCoursesService;
        this.manageOrderTransform = manageOrderTransform;
        this.userTransform = userTransform;
    }

    public Page<ManageOrderDTO> getPage(OrderSearch search) {
        if (!CommonAppUtils.isAdmin()) {
            User currentUser = userService.getByUsername(CommonAppUtils.getCurrentAccount());
            search.setBranchId(currentUser.getBranchId());
//            if (currentUser.getRole().getPosition().equals("STAFF")) {
//                search.setAssignId(currentUser.getId());
//            }
        }
        var page = service.getPage(search);
        List<ManageOrderDTO> content = page.getContent().stream().map(manageOrderTransform::toDto).collect(Collectors.toList());
        return new PageImpl<>(content, PageRequest.of(search.getPage(), search.getSize()),
                page.getTotalElements());
    }

    public OrderDTO getById(String id) {
        return transform.toDto(service.getOrElseThrow(id));
    }

    public OrderDTO getDetailByClient(String orderId) {
        Order order = service.getOrElseThrow(orderId);
        if (!order.getUser().getUsername().equals(CommonAppUtils.getCurrentAccount())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Không tìm thấy đơn hàng!!");
        }
        return transform.toDto(order);
    }

    public Page<OrderDTO> getMyOrder(OrderSearch search) {
        User user = userService.getByUsername(CommonAppUtils.getCurrentAccount());
        search.setUserId(user.getId());
        var page = service.getMyOrder(search);
        List<OrderDTO> content = page.getContent().stream().map(transform::toDto).collect(Collectors.toList());
        return new PageImpl<>(content, PageRequest.of(search.getPage(), search.getSize()),
                page.getTotalElements());
    }

    public OrderDTO createByUser(OrderRequest request) {
        //Bắt đầu với trạng thái PENDING - chưa thanh toán
        return this.buildOrder(request, CommonAppUtils.getCurrentAccount(), null);
    }

    public OrderDTO updateByUser(OrderRequest request, String orderId) {
        Order order = service.getOrElseThrow(orderId);
        if (!order.getStatus().equals(EOrderStatus.PENDING)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Không thể sửa đơn hàng khác đang xử lí!!");
        }
        List<OrderDetail> oldOrderDetails = orderDetailService.getAllByOrderId(order.getId());
        OrderDTO response = this.buildOrder(request, CommonAppUtils.getCurrentAccount(), null);
        orderDetailService.deleteAll(oldOrderDetails);
        return response;
    }

    public OrderDTO createByStaff(OrderByStaffRequest request) {
        this.checkPermission(request.getItems());
        User approve = userService.getByUsername(CommonAppUtils.getCurrentAccount());
        return this.buildOrder(request, request.getBuyer(), approve.getId());
    }

    public void updateOrderDetail(OrderItem request, String id) {
        OrderDetail od = orderDetailService.getOrElseThrow(id);
        Order order = od.getOrder();
        this.checkUDOrder(order);
        orderDetailService.deleteById(id);
        OrderDetail orderDetail = new OrderDetail();
        PTCourse ptCourse = ptCourseService.getByPTIdAndCourseId(request.getPtId(), request.getCourseId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Không tồn tại gói khóa học!!"));
        List<UserCourse> userCourses = userCoursesService.findAllInProgressByUserId(order.getUserId());
        Set<String> ptCoursesExist = userCourses.stream().map(userCourse -> userCourse.getPtCourse().getId()).collect(Collectors.toSet());
        if (ptCoursesExist.contains(ptCourse.getId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Người dùng đã tham gia khóa học " + ptCourse.getCourse().getTitle());
        }
        orderDetail.setOrderId(order.getId());
        orderDetail.setSlot(getSpecificSlot(request.getSlot()));
        orderDetail.setPrice(ptCourse.getCourse().getPrice());
        orderDetail.setPtCourseId(ptCourse.getId());
        OrderDetail data = orderDetailService.saveOrUpdate(orderDetail, null);
        UserCourse userCourse = new UserCourse();
        userCourse.setOrderDetailId(data.getId());
        userCourse.setUserId(order.getUserId());
        userCourse.setPtCourseId(ptCourse.getId());
        Long currentTime = new Date().getTime();
        userCourse.setStartDate(currentTime);
        userCourse.setEndDate(this.getEndDate(currentTime, ptCourse.getCourse().getRangeProcess()));
        userCourse.setStatus(EUserCourseStatus.IN_PROGRESS);
        userCoursesService.saveOrUpdate(userCourse, null);
    }

    public void deleteOrderDetail(String id) {
        OrderDetail orderDetail = orderDetailService.getOrElseThrow(id);
        this.checkUDOrder(orderDetail.getOrder());
        String orderId = orderDetail.getOrderId();
        Order order = orderDetail.getOrder();
        if(order.getOrderDetails().size() == 1) {
            service.deleteById(orderId);
        } else {
            orderDetailService.deleteById(id);
        }
    }

    private void checkUDOrder(Order order) {
        User user = userService.getByUsername(CommonAppUtils.getCurrentAccount());
        if (CommonAppUtils.isUser()) {
            if (!order.getStatus().equals(EOrderStatus.PENDING)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Không thể sửa gói khóa học đang trong trạng thái chuẩn bị!!");
            }
            if (!order.getUserId().equals(user.getId())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Sai yêu cầu!!");
            }
        } else {
            if (order.getStatus().equals(EOrderStatus.PENDING) || order.getStatus().equals(EOrderStatus.IN_PROGRESS)) {
                if(CommonAppUtils.isAdmin()) return;
                if(!user.getBranchId().equals(order.getUser().getBranchId())) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bạn không có quyền thực thi ở chi nhánh khác");
                }
            } else {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Sai yêu cầu!!");
            }
        }
    }

    public OrderDTO approveOrReject(ApproveOrderRequest request) {
        Order order = service.getOrElseThrow(request.getOrderId());
        if (!CommonAppUtils.isAdmin()) {
            User currentUser = userService.getByUsername(CommonAppUtils.getCurrentAccount());
            if (!currentUser.getBranchId().equals(order.getUser().getBranchId())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Không có quyền thực thi với chi nhánh này!!");
            }
        }
        EOrderStatus status = order.getStatus();
        boolean isConfirm = request.getIsConfirm();
        if (EOrderStatus.IN_PROGRESS.equals(status) || EOrderStatus.PENDING.equals(status)) {
            User approvePerson = userService.getByUsername(CommonAppUtils.getCurrentAccount());
            List<OrderDetail> orderDetails = order.getOrderDetails();
            List<UserCourse> userCourses = userCoursesService.findAllInProgressByUserId(order.getUserId());
            Set<String> ptCoursesExist = userCourses.stream().map(userCourse -> userCourse.getPtCourse().getId()).collect(Collectors.toSet());
            if(isConfirm) {
                for (OrderDetail od : orderDetails) {
                    PTCourse ptCourse = ptCourseService.getOrElseThrow(od.getPtCourseId());
                    if (ptCoursesExist.contains(ptCourse.getId())) {
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Người dùng đã tham gia khóa học " + ptCourse.getCourse().getTitle());
                    }
                }
            }
            order.setStatus(isConfirm ? EOrderStatus.COMPLETE : EOrderStatus.REJECT);
            order.setApproveId(approvePerson.getId());
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Yêu cầu không hợp lệ!!");
        }
        order = service.saveOrUpdate(order, order.getId());
        if (order.getStatus().equals(EOrderStatus.COMPLETE)) {
            this.addUserCourse(order.getOrderDetails(), order.getUserId());
        }
        return transform.toDto(order);
    }

    private OrderDTO buildOrder(OrderRequest request, String username, String approveId) {
        User buyer = userService.getByUsername(username);
        OrderInfo orderInfo = this.checkPrivacyItem(buyer, request.getItems());
        List<OrderDetail> orderDetails = orderInfo.getOrderDetails();
        Order order = new Order();
        order.setSearchOrder(orderInfo.getKeyword());
        order.setUserId(buyer.getId());
        order.setStatus(request.getStatus());
        if (approveId != null) {
            order.setApproveId(approveId);
        }

        OrderDTO response = transform.toDto(service.saveOrUpdate(order, null));
        orderDetails.forEach(o -> o.setOrderId(response.getId()));
        orderDetails = new ArrayList<>(orderDetailService.saveByList(orderDetails));
        if (request.getStatus().equals(EOrderStatus.COMPLETE)) {
            this.addUserCourse(orderDetails, buyer.getId());
        }
        return response;
    }

    @Override
    public void deleteById(String s) {
        Order order = service.getOrElseThrow(s);
//        if (!order.getStatus().equals(EOrderStatus.PENDING)) {
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Không thể xóa đơn hàng!!");
//        }
        super.deleteById(s);
    }


    @Async
    protected void addUserCourse(List<OrderDetail> orderDetails, String buyerId) {
        List<UserCourse> userCourses = new ArrayList<>();
        orderDetails.forEach(od -> {
            UserCourse uc = new UserCourse();
            uc.setUserId(buyerId);
            uc.setPtCourseId(od.getPtCourseId());
            uc.setStatus(EUserCourseStatus.IN_PROGRESS);
            uc.setSlot(od.getSlot());
            Long currentDate = new Date().getTime();
            uc.setStartDate(currentDate);
            uc.setOrderDetailId(od.getId());
            uc.setEndDate(this.getEndDate(currentDate, od.getPtCourse().getCourse().getRangeProcess()));
            userCourses.add(uc);
        });
        userCoursesService.saveByList(userCourses);
    }

    public String createPayment(PaymentRequest request) {
        User user = userService.getByUsername(CommonAppUtils.getCurrentAccount());
        Order order = service.getOrElseThrow(request.getOrderId());
        if (!order.getUser().getId().equals(user.getId()) || order.getStatus().equals(EOrderStatus.COMPLETE)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Đơn hàng không hợp lệ!!");
        }
        long totalMoney = 0;
        for (OrderDetail od : order.getOrderDetails()) {
            totalMoney += od.getPrice();
        }
        totalMoney *= 100;
        Map<String, String> VNPAYParams = VNPAYConfig.buildParams();
        VNPAYParams.put("vnp_TxnRef", order.getId());
        VNPAYParams.put("vnp_OrderInfo", "Thanh toán khóa học: " + order.getUser().getUsername());
        VNPAYParams.put("vnp_Amount", String.valueOf(totalMoney));
        VNPAYParams.put("vnp_BankCode", request.getBankCode());//NCB default
        return VNPAYConfig.getUrl(VNPAYParams);
    }

    public void updateStatusOrderSuccess(String orderId, boolean isSuccess) {
        Order order = service.getOrElseThrow(orderId);
        if (isSuccess) {
            order.setStatus(EOrderStatus.IN_PROGRESS);
        } else {
            order.setStatus(EOrderStatus.FAILED);
        }
        service.saveOrUpdate(order, order.getId());
    }

    private Long getEndDate(Long startDate, Integer progressTime) {
        Date currentDate = new Date(startDate);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentDate);
        //xử lí tính ra thời gian kết thúc khóa học
        calendar.add(Calendar.MONTH, progressTime);
        return calendar.getTime().getTime();
    }

    //API assign nhân viên xử lí các yêu cầu mua khóa học
    public void assignStaffToOrders(AssignRequest request) {
        List<String> orderIds = request.getOrderIds();
        List<Order> orders = service.getByIds(orderIds);
        log.info("====================> assignStaffToOrder. Size of order: " + orders.size());
        //Xử lí kiểm tra thực thi chi nhánh đối với role khác ADMIN
        if (!CommonAppUtils.isAdmin()) {
            User currentUser = userService.getByUsername(CommonAppUtils.getCurrentAccount());
            for (Order o : orders) {
                if (!o.getUser().getBranchId().equals(currentUser.getBranchId())) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bạn không có quyền xử lí đơn ở chi nhánh khác!!");
                }
            }
        }

        User assigneeUser = userService.getOrElseThrow(request.getAssignId());
        if (!assigneeUser.getRole().getPosition().equals("STAFF")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Không thể phân công cho người khác vị trí là nhân viên!!");
        }


        String userId = assigneeUser.getId();
        for (Order o : orders) {
            if (!o.getUser().getBranchId().equals(assigneeUser.getBranchId())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Không thể gán quyền xử lí cho nhân viên ở khác chi nhánh!!");
            }

            if (!o.getStatus().equals(EOrderStatus.PENDING) && !o.getStatus().equals(EOrderStatus.IN_PROGRESS)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Không thể gán quyền xử lí đơn ở trạng thái xử lí hoặc chưa thanh toán!!");
            }
            if (o.getAssignId() != null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Đơn duyệt đã được giao cho người khác!!");
            }
            o.setAssignId(userId);
        }
        service.saveAll(orders);
    }

    private OrderInfo checkPrivacyItem(User buyer, List<OrderItem> items) {
        String branchId = buyer.getBranchId();
        if (branchId == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Yêu cầu người dùng lựa chọn chi nhánh!!");
        }
        OrderInfo result = new OrderInfo();
        List<OrderDetail> orderDetails = new ArrayList<>();
        Set<String> uniqueCourseIds = new HashSet<>();
        StringBuilder keyword = new StringBuilder();
        List<UserCourse> userCourses = userCoursesService.findAllInProgressByUserId(buyer.getId());
        Set<String> ptCoursesExist = userCourses.stream().map(userCourse -> userCourse.getPtCourse().getId()).collect(Collectors.toSet());
        for (OrderItem oItem : items) {
            if (uniqueCourseIds.contains(oItem.getCourseId())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Trùng khóa học!!");
            }
            User pt = userService.getOrElseThrow(oItem.getPtId());
            Course course = courseService.getOrElseThrow(oItem.getCourseId());
            if (!course.getBranchId().equals(branchId)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Không thể mua khóa học khác chi nhánh!!");
            }
            PTCourse ptCourse = ptCourseService.getByPTIdAndCourseId(pt.getId(), course.getId()).orElseThrow(() ->
                    new ResponseStatusException(HttpStatus.BAD_REQUEST, "Không tìm thấy gói khóa học!!"));
            if (ptCoursesExist.contains(ptCourse.getId())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Người dùng đã tham gia khóa học " + ptCourse.getCourse().getTitle());
            }
            keyword.append(pt.getUsername()).append("-")
                    .append(ptCourse.getCourse().getTitle())
                    .append("-").append(pt.getFullName()).append("-").append(buyer.getFullName()).append("-").append(buyer.getUsername());

            OrderDetail od = new OrderDetail();
            od.setPtCourseId(ptCourse.getId());
            od.setPtCourse(ptCourse);
            od.setPrice(course.getPrice());
            od.setSlot(this.getSpecificSlot(oItem.getSlot()));
            orderDetails.add(od);
            uniqueCourseIds.add(course.getId());
        }

        result.setOrderDetails(orderDetails);
        result.setKeyword(keyword.toString());
        return result;
    }

    public List<UserDTO> getUserManage(String branchId) {
        var users = userService.getManageOrderByBranch(branchId);
        return users.stream().map(userTransform::toDto).collect(Collectors.toList());
    }

    public List<UserDTO> getUserAssign(String branchId) {
        var users = userService.getUserAssign(branchId);
        return users.stream().map(userTransform::toDto).collect(Collectors.toList());
    }

    private void checkPermission(List<OrderItem> items) {//dành cho nhân viên trong công ty
        if (CommonAppUtils.isAdmin()) {
            return;
        }
        User currentUser = userService.getByUsername(CommonAppUtils.getCurrentAccount());
        String branchId = currentUser.getBranchId();
        items.forEach(item -> {
            Course course = courseService.getOrElseThrow(item.getCourseId());
            if (!course.getType().getName().equals("PUBLIC")) {
                User mentor = userService.getOrElseThrow(item.getPtId());
                if (!course.getBranchId().equals(branchId) || !mentor.getBranchId().equals(branchId)) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Sai yêu cầu!!");
                }
            } else {
                if (!course.getBranchId().equals(branchId)) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Sai yêu cầu!!");
                }
            }
        });
    }

    private EUserCourseSlot getSpecificSlot(int slot) {
        switch (slot) {
            case 4: {
                return EUserCourseSlot.SLOT4;
            }
            case 3: {
                return EUserCourseSlot.SLOT3;
            }
            case 2: {
                return EUserCourseSlot.SLOT2;
            }
            default:
                return EUserCourseSlot.SLOT1;
        }
    }

    @Getter
    @Setter
    private static class OrderInfo {
        List<OrderDetail> orderDetails;
        String keyword;
    }
}
