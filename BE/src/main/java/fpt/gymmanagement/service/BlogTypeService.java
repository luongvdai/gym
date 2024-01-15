package fpt.gymmanagement.service;

import fpt.gymmanagement.common.CommonService;
import fpt.gymmanagement.entity.BlogType;

import java.util.List;

public interface BlogTypeService extends CommonService<BlogType, String> {
    List<BlogType> getAll();
}
