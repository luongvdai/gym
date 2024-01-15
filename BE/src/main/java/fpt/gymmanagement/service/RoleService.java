package fpt.gymmanagement.service;

import fpt.gymmanagement.entity.Role;
import fpt.gymmanagement.entity.User;

import java.util.List;

public interface RoleService {
    Role getDefault();

    Role checkCannotCreateRole(User user, Long id);

    List<Role> getAll(User user);

    List<Role> getFilter(User user);

    Role findById(Long id);
}
