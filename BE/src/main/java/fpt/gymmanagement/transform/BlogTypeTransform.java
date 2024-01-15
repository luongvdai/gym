package fpt.gymmanagement.transform;

import fpt.gymmanagement.common.CommonTransform;
import fpt.gymmanagement.dto.BlogTypeDTO;
import fpt.gymmanagement.entity.BlogType;
import org.mapstruct.Mapper;

@Mapper
public interface BlogTypeTransform extends CommonTransform<BlogType, BlogTypeDTO> {
}
