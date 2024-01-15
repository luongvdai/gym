package fpt.gymmanagement.processor;

import fpt.gymmanagement.dto.response.CourseStatsResponse;
import fpt.gymmanagement.dto.response.UserStatsResponse;
import fpt.gymmanagement.dto.search.OrderSearch;
import fpt.gymmanagement.dto.search.UserSearch;
import fpt.gymmanagement.entity.Course;
import fpt.gymmanagement.entity.Order;
import fpt.gymmanagement.entity.OrderDetail;
import fpt.gymmanagement.entity.User;
import fpt.gymmanagement.service.CourseService;
import fpt.gymmanagement.service.OrderService;
import fpt.gymmanagement.service.UserService;
import fpt.gymmanagement.utils.CommonAppUtils;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class StatsProcessor {
    private final UserService userService;
    private final OrderService orderService;
    private final CourseService courseService;

    public StatsProcessor(UserService userService, OrderService orderService, CourseService courseService) {
        this.userService = userService;
        this.orderService = orderService;
        this.courseService = courseService;
    }

    public UserStatsResponse getTotalNewUserByTime(UserSearch search) {
        if (!CommonAppUtils.isAdmin()) {//case là manager các chi nhánh tự động set branch id cho query
            User user = userService.getByUsername(CommonAppUtils.getCurrentAccount());
            search.setBranchId(user.getBranchId());
        }
        return userService.getTotalNewUserByTime(search);
    }

    public double getRevenueByTime(OrderSearch search) {
        if (!CommonAppUtils.isAdmin()) {//case là manager các chi nhánh tự động set branch id cho query
            User user = userService.getByUsername(CommonAppUtils.getCurrentAccount());
            search.setBranchId(user.getBranchId());
        }
        List<Order> orders = orderService.getTotalRevenueByTime(search);
        return CommonAppUtils.getTotalMoneyByOrders(orders);
    }

    public Map<String, Double> getRevenueByYear(Integer year, String branchId) {
        if (year == null) {
            year = CommonAppUtils.getCurrentYear();
        }
        OrderSearch search = new OrderSearch();
        if(branchId != null && CommonAppUtils.isAdmin()) {
            search.setBranchId(branchId);
        } else {
            User user = userService.getByUsername(CommonAppUtils.getCurrentAccount());
            search.setBranchId(user.getBranchId());
        }
        return orderService.getTotalRevenueByYear(search, year);
    }

    public List<CourseStatsResponse> getTopCourse(OrderSearch search) {
        if (!CommonAppUtils.isAdmin()) {//case là manager các chi nhánh tự động set branch id cho query
            User user = userService.getByUsername(CommonAppUtils.getCurrentAccount());
            search.setBranchId(user.getBranchId());
        }
        List<Order> orders = orderService.getTotalRevenueByTime(search);
        List<OrderDetail> orderDetails = new ArrayList<>();
        for(Order o : orders) {
            orderDetails.addAll(o.getOrderDetails());
        }

        Map<String, Integer> courseCountMap = new HashMap<>();
        for (OrderDetail od : orderDetails) {
            String courseId = od.getPtCourse().getCourseId();
            courseCountMap.put(od.getPtCourse().getCourseId(), courseCountMap.getOrDefault(courseId, 0) + 1);
        }
        return courseService.getTopCourse(courseCountMap);
    }
}
