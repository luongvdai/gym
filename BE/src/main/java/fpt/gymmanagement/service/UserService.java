package fpt.gymmanagement.service;

import fpt.gymmanagement.common.CommonService;
import fpt.gymmanagement.dto.request.user.UserRequest;
import fpt.gymmanagement.dto.response.UserStatsResponse;
import fpt.gymmanagement.dto.search.UserSearch;
import fpt.gymmanagement.entity.User;
import fpt.gymmanagement.enums.EUserType;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface UserService extends CommonService<User, String> {
    User getByUsername(String username);

    Optional<User> getByUsernameAndProvider(String username, EUserType provider);

    Optional<User> getByEmail(String email);

    boolean checkExistByUsername(String username);

    boolean checkExistByEmail(String email);

    Page<User> getManageUsers(UserSearch query);

    void checkPrivacyCreate(String username, String email, String phoneNumber);

    void checkPrivacyUpdate(User user, UserRequest dto);

    UserStatsResponse getTotalNewUserByTime(UserSearch search);

    List<User> getManageOrderByBranch(String branchId);

    List<User> getUserAssign(String branchId);

    List<User> getManageBlogByBranch(String branchId);
}
