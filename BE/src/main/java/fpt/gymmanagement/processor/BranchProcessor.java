package fpt.gymmanagement.processor;

import fpt.gymmanagement.common.CommonProcessor;
import fpt.gymmanagement.dto.BranchDTO;
import fpt.gymmanagement.dto.search.BranchSearch;
import fpt.gymmanagement.entity.Branch;
import fpt.gymmanagement.service.BranchService;
import fpt.gymmanagement.transform.BranchTransform;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
public class BranchProcessor extends CommonProcessor<BranchService, BranchTransform, Branch, BranchDTO, String> {
    protected BranchProcessor(BranchService service, BranchTransform transform) {
        super(service, transform);
    }

    public Page<BranchDTO> getPage(BranchSearch search) {
        var page = service.getPage(search);
        List<BranchDTO> content = page.getContent().stream().map(transform::toDto).collect(Collectors.toList());
        return new PageImpl<>(content, PageRequest.of(search.getPage(), search.getSize()),
                page.getTotalElements());
    }
}
