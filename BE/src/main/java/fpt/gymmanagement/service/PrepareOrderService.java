package fpt.gymmanagement.service;

import fpt.gymmanagement.dto.PrepareOrderDTO;
import fpt.gymmanagement.dto.search.PrepareOrderSearch;
import fpt.gymmanagement.entity.PrepareOrder;
import org.springframework.data.domain.Page;

public interface PrepareOrderService {
    Page<PrepareOrder> getAll(PrepareOrderSearch search);

    PrepareOrder getById(String id);

    PrepareOrder create(PrepareOrderDTO dto);

    void deleteById(String id);

    void checkAlreadyExist(String ptCourseId, String userId);
}
