package fpt.gymmanagement.service;

import fpt.gymmanagement.common.CommonService;
import fpt.gymmanagement.dto.search.BlogSearch;
import fpt.gymmanagement.entity.Blog;
import org.springframework.data.domain.Page;

public interface BlogService extends CommonService<Blog, String> {
    Page<Blog> getPage(BlogSearch search);
}
