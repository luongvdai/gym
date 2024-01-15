package fpt.gymmanagement.service;

import fpt.gymmanagement.common.CommonService;
import fpt.gymmanagement.dto.search.OrderDetailSearch;
import fpt.gymmanagement.entity.OrderDetail;
import org.springframework.data.domain.Page;

import java.util.List;

public interface OrderDetailService extends CommonService<OrderDetail, String> {
    Page<OrderDetail> getPage(OrderDetailSearch orderDetail);

    List<OrderDetail> saveByList(List<OrderDetail> dtos);

    List<OrderDetail> getAllByOrderId(String orderId);

    void deleteAll(List<OrderDetail> orderDetails);
}
