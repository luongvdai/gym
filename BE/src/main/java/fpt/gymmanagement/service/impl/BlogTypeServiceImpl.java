package fpt.gymmanagement.service.impl;

import fpt.gymmanagement.common.CommonJPAServiceImpl;
import fpt.gymmanagement.entity.BlogType;
import fpt.gymmanagement.entity.QBlogType;
import fpt.gymmanagement.repository.BlogTypeRepository;
import fpt.gymmanagement.service.BlogTypeService;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import java.util.List;

@Service
public class BlogTypeServiceImpl extends CommonJPAServiceImpl<BlogType, String, BlogTypeRepository, QBlogType>
        implements BlogTypeService {
    public BlogTypeServiceImpl(EntityManager em, BlogTypeRepository repo) {
        super(em, QBlogType.blogType, repo);
    }

    @Override
    protected String notFoundMessage() {
        return "Không tìm thấy kiểu blog!!";
    }

    @Override
    public List<BlogType> getAll() {
        return repo.findAll();
    }
}
