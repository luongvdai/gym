package fpt.gymmanagement.service.impl;

import com.querydsl.jpa.impl.JPAQuery;
import fpt.gymmanagement.dto.PrepareOrderDTO;
import fpt.gymmanagement.dto.search.PrepareOrderSearch;
import fpt.gymmanagement.entity.PrepareOrder;
import fpt.gymmanagement.entity.QPrepareOrder;
import fpt.gymmanagement.repository.PrepareOrderRepository;
import fpt.gymmanagement.service.PrepareOrderService;
import fpt.gymmanagement.utils.CommonAppUtils;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.EntityManager;
import java.util.Date;

@Service
public class PrepareOrderServiceImpl implements PrepareOrderService {
    private final PrepareOrderRepository repository;
    private final EntityManager em;
    private final QPrepareOrder qPrepareOrder = new QPrepareOrder("prepare_order");

    public PrepareOrderServiceImpl(PrepareOrderRepository repository, EntityManager em) {
        this.repository = repository;
        this.em = em;
    }

    @Override
    public Page<PrepareOrder> getAll(PrepareOrderSearch search) {
        JPAQuery<PrepareOrder> query = new JPAQuery<>(em);
        query.select(qPrepareOrder).from(qPrepareOrder)
                .where(qPrepareOrder.userId.eq(search.getUserId()))
                .orderBy(qPrepareOrder.createdDate.desc());
        return (Page<PrepareOrder>) CommonAppUtils.getDataPage(query, search.getPage(), search.getSize());
    }

    @Override
    public PrepareOrder getById(String id) {
        return repository.findById(id).orElseThrow(()
                -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Không tìm thấy gói lưu trữ!!"));
    }

    @Override
    public PrepareOrder create(PrepareOrderDTO dto) {
        PrepareOrder entity = new PrepareOrder();
        CommonAppUtils.copyPropertiesNotNull(dto, entity);
        Long currentDate = new Date().getTime();
        entity.setCreatedDate(currentDate);
        entity.setUpdatedDate(currentDate);
        return repository.save(entity);
    }

    @Override
    public void deleteById(String id) {
        this.getById(id);
        repository.deleteById(id);
    }

    @Override
    public void checkAlreadyExist(String ptCourseId, String userId) {
        repository.findByPtCourseIdAndUserId(ptCourseId, userId)
                .ifPresent(entity -> {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Đã tồn tại gói khóa học này trong kho lưu trữ!!");
                });
    }
}
