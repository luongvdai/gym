package fpt.gymmanagement.service.impl;

import com.querydsl.jpa.impl.JPAQuery;
import fpt.gymmanagement.common.CommonJPAServiceImpl;
import fpt.gymmanagement.dto.search.OrderDetailSearch;
import fpt.gymmanagement.dto.search.OrderSearch;
import fpt.gymmanagement.entity.OrderDetail;
import fpt.gymmanagement.entity.QOrderDetail;
import fpt.gymmanagement.repository.OrderDetailRepository;
import fpt.gymmanagement.service.OrderDetailService;
import fpt.gymmanagement.utils.CommonAppUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import java.util.List;

@Service
public class OrderDetailServiceImpl extends CommonJPAServiceImpl<OrderDetail, String, OrderDetailRepository, QOrderDetail>
        implements OrderDetailService {
    public OrderDetailServiceImpl(EntityManager em, OrderDetailRepository repo) {
        super(em, QOrderDetail.orderDetail, repo);
    }

    @Override
    protected String notFoundMessage() {
        return "Không tìm thấy thông tin chi tiết gói khóa học!!";
    }

    @Override
    public Page<OrderDetail> getPage(OrderDetailSearch search) {
        this.query = new JPAQuery<>(em);
        this.query.select(q).from(q);
        if (StringUtils.hasText(search.getKeyword())) {
            String keyword = search.getKeyword();
            this.query.where(q.ptCourse.course.title.containsIgnoreCase(keyword)
                    .or(q.ptCourse.user.fullName.containsIgnoreCase(keyword)));
        }

        if(StringUtils.hasText(search.getOrderId())) {
            this.query.where(q.orderId.eq(search.getOrderId()));
        }

        this.query.orderBy(q.createdDate.desc());
        return (Page<OrderDetail>) CommonAppUtils.getDataPage(this.query, search.getPage(), search.getSize());
    }

    @Override
    public List<OrderDetail> saveByList(List<OrderDetail> dtos) {
        return repo.saveAll(dtos);
    }

    @Override
    public OrderDetail saveOrUpdate(OrderDetail orderDetail, String s) {
        return super.saveOrUpdate(orderDetail, s);
    }

    @Override
    public List<OrderDetail> getAllByOrderId(String orderId) {
        return repo.findAllByOrderId(orderId);
    }

    @Override
    public void deleteAll(List<OrderDetail> orderDetails) {
        repo.deleteAll(orderDetails);
    }
}
