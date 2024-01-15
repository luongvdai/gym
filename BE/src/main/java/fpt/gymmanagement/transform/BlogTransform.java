package fpt.gymmanagement.transform;

import fpt.gymmanagement.common.CommonTransform;
import fpt.gymmanagement.dto.BlogDTO;
import fpt.gymmanagement.entity.Blog;
import org.mapstruct.Mapper;

@Mapper
public interface BlogTransform extends CommonTransform<Blog, BlogDTO> {
}
