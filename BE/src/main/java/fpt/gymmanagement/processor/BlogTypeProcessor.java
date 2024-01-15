package fpt.gymmanagement.processor;

import fpt.gymmanagement.common.CommonProcessor;
import fpt.gymmanagement.dto.BlogTypeDTO;
import fpt.gymmanagement.entity.BlogType;
import fpt.gymmanagement.service.BlogTypeService;
import fpt.gymmanagement.transform.BlogTypeTransform;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class BlogTypeProcessor extends CommonProcessor<BlogTypeService, BlogTypeTransform, BlogType, BlogTypeDTO, String> {

    protected BlogTypeProcessor(BlogTypeService service, BlogTypeTransform transform) {
        super(service, transform);
    }

    public List<BlogTypeDTO> getAll() {
        return service.getAll().stream().map(transform::toDto).collect(Collectors.toList());
    }
}
