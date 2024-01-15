package fpt.gymmanagement.transform;

import fpt.gymmanagement.common.CommonTransform;
import fpt.gymmanagement.dto.UserDTO;
import fpt.gymmanagement.entity.User;
import org.mapstruct.Mapper;

@Mapper
public interface UserTransform extends CommonTransform<User, UserDTO> {
}
