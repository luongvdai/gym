package fpt.gymmanagement.service.impl;

import com.querydsl.jpa.impl.JPAQuery;
import fpt.gymmanagement.common.CommonJPAServiceImpl;
import fpt.gymmanagement.dto.search.BlogSearch;
import fpt.gymmanagement.entity.Blog;
import fpt.gymmanagement.entity.QBlog;
import fpt.gymmanagement.repository.BlogRepository;
import fpt.gymmanagement.service.BlogService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;

@Service
public class BlogServiceImpl extends CommonJPAServiceImpl<Blog, String, BlogRepository, QBlog>
        implements BlogService {


    public BlogServiceImpl(EntityManager em, BlogRepository repo) {
        super(em, QBlog.blog, repo);
    }

    @Override
    protected String notFoundMessage() {
        return "Không tìm thấy blog!!";
    }

    @Override
    public Page<Blog> getPage(BlogSearch search) {
        this.addFilterBlog(search);
        long count = this.query.stream().count();
        var data = this.query.offset((long) search.getPage() * search.getSize()).limit(search.getSize()).fetch();
        return new PageImpl<>(data, PageRequest.of(search.getPage(), search.getSize()), count);
    }

    @Override
    public Blog saveOrUpdate(Blog blog, String s) {
        return super.saveOrUpdate(blog, s);
    }

    private void addFilterBlog(BlogSearch search) {
        this.query = new JPAQuery<>(em);
        this.query.select(q).from(q);

        if (StringUtils.hasText(search.getKeyword())) {
            String keyword = search.getKeyword();
            this.query.where(q.title.containsIgnoreCase(keyword)
                    .or(q.content.containsIgnoreCase(keyword))
                    .or(q.user.username.containsIgnoreCase(keyword)));
        }

        if (StringUtils.hasText(search.getTypeId())) {
            this.query.where(q.typeId.eq(search.getTypeId()));
        }

        if (StringUtils.hasText(search.getCreatedBy())) {
            this.query.where(q.user.username.containsIgnoreCase(search.getCreatedBy()));
        }

        if (StringUtils.hasText(search.getBranchId())) {
            this.query.where(q.branchId.eq(search.getBranchId()));
        }

        this.query.orderBy(q.updatedDate.desc(), q.createdDate.desc());
    }

    @Override
    public void deleteById(String id) {
        repo.deleteById(id);
    }
}
