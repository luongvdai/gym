package fpt.gymmanagement.service.impl;

import com.querydsl.jpa.impl.JPAQuery;
import fpt.gymmanagement.common.CommonJPAServiceImpl;
import fpt.gymmanagement.dto.search.OrderSearch;
import fpt.gymmanagement.entity.Order;
import fpt.gymmanagement.entity.QOrder;
import fpt.gymmanagement.entity.QUser;
import fpt.gymmanagement.enums.EOrderStatus;
import fpt.gymmanagement.repository.OrderRepository;
import fpt.gymmanagement.service.OrderService;
import fpt.gymmanagement.utils.CommonAppUtils;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class OrderServiceImpl extends CommonJPAServiceImpl<Order, String, OrderRepository, QOrder>
        implements OrderService {

    public OrderServiceImpl(EntityManager em, OrderRepository repo) {
        super(em, QOrder.order, repo);
    }

    @Override
    protected String notFoundMessage() {
        return "Không tìm thấy đơn hàng!!";
    }

    @Override
    public Page<Order> getPage(OrderSearch search) {
        this.addFilterOrder(search);
        return (Page<Order>) CommonAppUtils.getDataPage(this.query, search.getPage(), search.getSize());
    }

    @Override
    public Page<Order> getMyOrder(OrderSearch search) {
        this.query = new JPAQuery<>(em);
        this.query.select(q).from(q).where(q.userId.eq(search.getUserId()));

        if (StringUtils.hasText(search.getKeyword())) {
            this.query.where(q.searchOrder.containsIgnoreCase(search.getKeyword()));
        }

        if (StringUtils.hasText(search.getBranchId())) {
            this.query.where(q.user.branchId.eq(search.getBranchId()));
        }

        if (search.getStatus() != null) {
            this.query.where(q.status.eq(search.getStatus()));
        }
        return (Page<Order>) CommonAppUtils.getDataPage(this.query, search.getPage(), search.getSize());
    }

    @Override
    public List<Order> getByIds(List<String> ids) {
        return repo.getByIds(ids);
    }

    @Override
    public void saveAll(List<Order> orders) {
        repo.saveAll(orders);
    }

    @Override
    public List<Order> getTotalRevenueByTime(OrderSearch search) {
        QUser user = QUser.user;
        search.setPage(0);
        search.setSize(Integer.MAX_VALUE);
        this.query = new JPAQuery<>(em);
        this.query.select(q).from(q).join(user).on(q.userId.eq(user.id))
                .where(q.status.eq(EOrderStatus.COMPLETE));//chỉ những đơn hàng thành công
        if (StringUtils.hasText(search.getBranchId())) {
            this.query.where(q.user.branchId.eq(search.getBranchId()));
        }
        if (search.getFromDate() == null && search.getToDate() == null) {
            //Mặc định lấy user mới đăng kí theo tháng
            Map<String, Long> currentDay = CommonAppUtils.getFromDateAndToDateCurrentMonth();
            long fromDate = currentDay.get("fromDate");
            long toDate = currentDay.get("toDate");
            this.query.where(q.createdDate.gt(fromDate).and(q.createdDate.lt(toDate)));
        } else {
            //Filter theo ngày tháng
            if (search.getFromDate() != null && search.getToDate() == null) {
                this.query.where(q.createdDate.gt(search.getFromDate()));
            } else if (search.getToDate() != null && search.getFromDate() == null) {
                this.query.where(q.createdDate.lt(search.getToDate()));
            } else if (search.getFromDate() != null && search.getToDate() != null) {
                this.query.where(q.createdDate.gt(search.getFromDate()).and(q.createdDate.lt(search.getToDate())));
            }
        }
        return this.query.fetch();
    }

    @Override
    public Map<String, Double> getTotalRevenueByYear(OrderSearch search, int year) {
        search.setPage(0);
        search.setSize(Integer.MAX_VALUE);
        Map<String, Double> response = new LinkedHashMap<>();
        Map<String, CommonAppUtils.FilterTimeStats> data = CommonAppUtils.getStatsEveryMonthByYear(year);
        for (Map.Entry<String, CommonAppUtils.FilterTimeStats> entry : data.entrySet()) {
            String key = entry.getKey();//month
            CommonAppUtils.FilterTimeStats filterTimeStats = entry.getValue();
            search.setFromDate(filterTimeStats.getFromDate());
            search.setToDate(filterTimeStats.getToDate());
            List<Order> orders = this.getTotalRevenueByTime(search);
            Double revenue = CommonAppUtils.getTotalMoneyByOrders(orders);
            response.put(key, revenue);
        }
        return response;
    }

    private void addFilterOrder(OrderSearch search) {
        this.query = new JPAQuery<>(em);
        this.query.select(q).from(q);

        if (StringUtils.hasText(search.getKeyword())) {
            this.query.where(q.searchOrder.containsIgnoreCase(search.getKeyword()));
        }

        if (StringUtils.hasText(search.getBuyer())) {
            this.query.where(q.user.username.containsIgnoreCase(search.getBuyer())
                    .or(q.user.fullName.containsIgnoreCase(search.getBuyer())));
        }

        if (StringUtils.hasText(search.getUserId())) {
            this.query.where(q.userId.eq(search.getUserId()));
        }

        if (StringUtils.hasText(search.getBranchId())) {
            this.query.where(q.user.branchId.eq(search.getBranchId()));
        }

        if (search.getStatus() != null) {
            this.query.where(q.status.eq(search.getStatus()));
        }

        if (StringUtils.hasText(search.getAssignId())) {
            this.query.where(q.assignId.eq(search.getAssignId()));
        }

        if(StringUtils.hasText(search.getApproveId())) {
            this.query.where(q.approveId.eq(search.getApproveId()));
        }

        this.query.orderBy(q.updatedDate.desc(), q.createdDate.desc());
    }
}
