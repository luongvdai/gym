package fpt.gymmanagement.service.impl;

import com.querydsl.jpa.impl.JPAQuery;
import fpt.gymmanagement.common.CommonJPAServiceImpl;
import fpt.gymmanagement.dto.request.user.UserRequest;
import fpt.gymmanagement.dto.response.UserStatsResponse;
import fpt.gymmanagement.dto.search.UserSearch;
import fpt.gymmanagement.entity.QUser;
import fpt.gymmanagement.entity.User;
import fpt.gymmanagement.enums.EUserType;
import fpt.gymmanagement.repository.UserRepository;
import fpt.gymmanagement.service.UserService;
import fpt.gymmanagement.utils.CommonAppUtils;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.EntityManager;
import java.util.*;

@Service
public class UserServiceImpl extends CommonJPAServiceImpl<User, String, UserRepository, QUser> implements UserService {
    private static final String USERNAME_EXIST = "Tên đăng nhập đã tồn tại!!";
    private static final String EMAIL_EXIST = "Email đã được dùng để đăng kí!!";
    private static final String PHONE_NUMBER_EXIST = "Số điện thoại đã được dùng để đăng kí!!";

    public UserServiceImpl(EntityManager em, UserRepository repo) {
        super(em, QUser.user, repo);
    }

    @Override
    protected String notFoundMessage() {
        return "Không tìm thấy user!!";
    }

    @Override
    public User getByUsername(String username) {
        return repo.findByUsernameOrEmail(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, notFoundMessage()));
    }

    @Override
    public Optional<User> getByUsernameAndProvider(String username, EUserType provider) {
        return repo.findByUsernameAndType(username, provider);
    }

    @Override
    public Optional<User> getByEmail(String email) {
        return repo.findByEmail(email);
    }

    @Override
    public boolean checkExistByUsername(String username) {
        return repo.findByUsername(username).isPresent();
    }

    @Override
    public boolean checkExistByEmail(String email) {
        return repo.findByEmail(email).isPresent();
    }

    @Override
    public Page<User> getManageUsers(UserSearch search) {
        this.createCondition(search);
        return (Page<User>) CommonAppUtils.getDataPage(this.query, search.getPage(), search.getSize());
    }

    @Override
    public void checkPrivacyCreate(String username, String email, String phoneNumber) {
        if (checkExistByUsername(username) && username != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, USERNAME_EXIST);
        }
        if (checkExistByEmail(email) && email != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, EMAIL_EXIST);
        }
    }

    @Override
    public void checkPrivacyUpdate(User user, UserRequest dto) {
        if (!user.getUsername().equals(dto.getUsername()) && this.checkExistByUsername(dto.getUsername())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, USERNAME_EXIST);
        }
        if (!user.getEmail().equals(dto.getEmail()) && checkExistByEmail(dto.getEmail())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, EMAIL_EXIST);
        }
    }

    //STATISTIC
    @Override
    public UserStatsResponse getTotalNewUserByTime(UserSearch search) {
        search.setPage(0);
        search.setSize(Integer.MAX_VALUE);
        UserStatsResponse response = new UserStatsResponse();
        this.query = new JPAQuery<>(em);
        this.query.select(q).from(q);

        if (StringUtils.hasText(search.getBranchId())) {
            this.query.where(q.branchId.eq(search.getBranchId()));
        }

        if (search.getFromDate() == null && search.getToDate() == null) {
            //Mặc định lấy user mới đăng kí theo tháng
            Map<String, Long> currentDay = CommonAppUtils.getFromDateAndToDateCurrentMonth();
            long fromDate = currentDay.get("fromDate");
            long toDate = currentDay.get("toDate");
            this.query.where(q.createdDate.gt(fromDate).and(q.createdDate.lt(toDate)));
        } else {
            //Filter theo ngày tháng
            if (search.getFromDate() != null && search.getToDate() == null) {
                this.query.where(q.createdDate.gt(search.getFromDate()));
            } else if (search.getToDate() != null && search.getFromDate() == null) {
                this.query.where(q.createdDate.lt(search.getToDate()));
            } else if (search.getFromDate() != null && search.getToDate() != null) {
                this.query.where(q.createdDate.gt(search.getFromDate()).and(q.createdDate.lt(search.getToDate())));
            }
        }
        long total = this.query.stream().count();
        response.setTotal(total);
        long totalMales = 0;
        long totalFemales = 0;
        List<User> users = this.query.fetch();
        for (User u : users) {
            if (u.getGender().equals(Boolean.TRUE)) {
                totalMales++;
            } else {
                totalFemales++;
            }
        }
        response.setTotalMale(totalMales);
        response.setTotalFemale(totalFemales);
        return response;
    }

    @Override
    public List<User> getManageOrderByBranch(String branchId) {
        if (branchId != null) {
            this.query = new JPAQuery<>(em);
            this.query.select(q).from(q).where(q.branchId.eq(branchId));
            this.query.where(q.role.position.eq("MANAGER")
                    .or(q.role.position.eq("STAFF")
                            .or(q.role.position.eq("ADMIN"))));
            return this.query.fetch();
        }
        return new ArrayList<>();
    }

    @Override
    public List<User> getUserAssign(String branchId) {
        if (branchId != null) {
            this.query = new JPAQuery<>(em);
            this.query.select(q).from(q).where(q.branchId.eq(branchId));
            this.query.select(q).from(q).where(q.role.position.eq("STAFF"));
            return this.query.fetch();
        }
        return new ArrayList<>();
    }

    @Override
    public List<User> getManageBlogByBranch(String branchId) {
        if (branchId != null) {
            this.query = new JPAQuery<>(em);
            this.query.select(q).from(q).where(q.branchId.eq(branchId));
            this.query.where(q.role.position.eq("MANAGER")
                    .or(q.role.position.eq("MENTOR")
                            .or(q.role.position.eq("ADMIN"))));
            return this.query.fetch();
        }
        return new ArrayList<>();
    }

    //OVERRIDE test xem save update ổn định không
    @Override
    public User saveOrUpdate(User user, String s) {
        if (s != null) {
            user.setUpdatedDate(new Date().getTime());
        }
        return repo.save(user);
    }

    private void createCondition(UserSearch search) {
        this.query = new JPAQuery<>(em);
        this.query.where(q.role.position.ne("ADMIN"));//ko query admin
        if (StringUtils.hasText(search.getBranchId())) {
            this.query.select(q).from(q)
                    .where(q.branchId.eq(search.getBranchId()).or(q.branchId.isNull()));
        } else {
            this.query.select(q).from(q);
        }
        //không tự query chính mình + ko query ra admin
        this.query.where(q.username.notIn(search.getCurrentAccount()));

        if (StringUtils.hasText(search.getKeyword())) {
            String keyword = search.getKeyword();
            this.query.where(q.username.containsIgnoreCase(keyword)
                    .or(q.fullName.containsIgnoreCase(keyword))
                    .or(q.email.containsIgnoreCase(keyword))
                    .or(q.address.containsIgnoreCase(keyword))
                    .or(q.phoneNumber.containsIgnoreCase(keyword)));
        }

        if (search.getRoleId() != null) {
            this.query.where(q.roleId.eq(search.getRoleId()));
        }

        if (search.getGender() != null) {
            this.query.where(q.gender.eq(search.getGender()));
        }

        if (search.getIsActive() != null) {
            this.query.where(q.isActive.eq(search.getIsActive()));
        }

        if (search.getIsVerify() != null) {
            this.query.where(q.isVerify.eq(search.getIsVerify()));
        }

        this.query.orderBy(q.updatedDate.desc(), q.createdDate.desc());
    }
}
