package fpt.gymmanagement.service;

import fpt.gymmanagement.common.CommonService;
import fpt.gymmanagement.dto.search.OrderSearch;
import fpt.gymmanagement.entity.Order;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

public interface OrderService extends CommonService<Order, String> {
    Page<Order> getPage(OrderSearch search);
    Page<Order> getMyOrder(OrderSearch search);
    List<Order> getByIds(List<String> ids);
    void saveAll(List<Order> orders);
    List<Order> getTotalRevenueByTime(OrderSearch search);
    Map<String, Double> getTotalRevenueByYear(OrderSearch orderSearch, int year);
}
