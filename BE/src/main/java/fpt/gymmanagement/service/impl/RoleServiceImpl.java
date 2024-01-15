package fpt.gymmanagement.service.impl;

import fpt.gymmanagement.entity.Role;
import fpt.gymmanagement.entity.User;
import fpt.gymmanagement.repository.RoleRepository;
import fpt.gymmanagement.service.RoleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {
    private final RoleRepository repository;
    private static final String ADMIN = "ADMIN";
    private static final String USER = "USER";
    private static final String MANAGER = "MANAGER";
    private static final String STAFF = "STAFF";
    private static final String MENTOR = "MENTOR";
    private static final String FOR_BIDDEN_MESSAGE = "Bạn không có quyền truy cập!!";

    @Override
    public Role getDefault() {
        return repository.findByPosition(USER)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Không tồn tại quyền user thường!!"));
    }

    @Override
    public Role checkCannotCreateRole(User user, Long id) {
        Role role = repository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.BAD_REQUEST, "Không tồn tại quyền này!!"));
        String roleCreate = role.getPosition();

        String creatorPosition = user.getRole().getPosition();
        switch (creatorPosition) {//switch type user call API
            case ADMIN: {
                if (roleCreate.equals(ADMIN)) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Không thể tạo user là admin!!");
                }
                return role;
            }
            case MANAGER: {
                if (roleCreate.equals(ADMIN) || roleCreate.equals(MANAGER)) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Không thể tạo user là admin hoặc manager!!");
                }
                return role;
            }
            case STAFF: {
            }
            case MENTOR: {
                if (!roleCreate.equals(USER)) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Không thể tạo tài khoản ngoài user thường!!");
                }
                return role;
            }
        }
        throw new ResponseStatusException(HttpStatus.FORBIDDEN, FOR_BIDDEN_MESSAGE);
    }

    @Override
    public List<Role> getAll(User user) {//API call for create user
        return repository.findAll().stream()
                .filter(r -> (!r.getPosition().equals(ADMIN)))
                .collect(Collectors.toList());
    }

    @Override
    public List<Role> getFilter(User user) {//API call for filter user
        return repository.findAll().stream()
                .filter(r -> (!r.getPosition().equals(ADMIN)))
                .collect(Collectors.toList());
    }

    @Override
    public Role findById(Long id) {
        return repository.findById(id).orElseThrow(()
                -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Không tìm thấy quyền!!"));
    }
}

