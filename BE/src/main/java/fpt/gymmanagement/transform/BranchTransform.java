package fpt.gymmanagement.transform;

import fpt.gymmanagement.common.CommonTransform;
import fpt.gymmanagement.dto.BranchDTO;
import fpt.gymmanagement.entity.Branch;
import org.mapstruct.Mapper;

@Mapper
public interface BranchTransform extends CommonTransform<Branch, BranchDTO> {
}
