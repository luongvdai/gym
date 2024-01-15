package fpt.gymmanagement.processor;

import fpt.gymmanagement.entity.Role;
import fpt.gymmanagement.entity.User;
import fpt.gymmanagement.utils.CommonAppUtils;
import fpt.gymmanagement.service.RoleService;
import fpt.gymmanagement.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class RoleProcessor {
    private final RoleService roleService;
    private final UserService userService;

    public List<Role> getAll() {
        User user = userService.getByUsername(CommonAppUtils.getCurrentAccount());
        return roleService.getAll(user);
    }

    public List<Role> getFilter() {
        User user = userService.getByUsername(CommonAppUtils.getCurrentAccount());
        return roleService.getFilter(user);
    }
}
