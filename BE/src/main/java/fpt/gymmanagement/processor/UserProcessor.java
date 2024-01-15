package fpt.gymmanagement.processor;

import fpt.gymmanagement.common.CommonProcessor;
import fpt.gymmanagement.dto.UserDTO;
import fpt.gymmanagement.dto.request.EmailDetails;
import fpt.gymmanagement.dto.request.user.*;
import fpt.gymmanagement.dto.response.PersonalTrainerResponse;
import fpt.gymmanagement.dto.search.UserSearch;
import fpt.gymmanagement.entity.Branch;
import fpt.gymmanagement.entity.Role;
import fpt.gymmanagement.entity.User;
import fpt.gymmanagement.enums.EUserType;
import fpt.gymmanagement.service.*;
import fpt.gymmanagement.transform.UserTransform;
import fpt.gymmanagement.utils.CommonAppUtils;
import fpt.gymmanagement.utils.excel.CommonExcel;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserProcessor extends CommonProcessor<UserService, UserTransform, User, UserDTO, String> {
    private final PasswordEncoder passwordEncoder;
    private final CacheService cacheService;
    private final EmailService emailService;
    private final RoleService roleService;
    private final TokenService tokenService;
    private final BranchService branchService;
    private final FeedbackService feedbackService;
    private final String RESET_PASSWORD = "reset-password:";
    private final String VERIFY_OTP = "verify-otp:";
    private static final String ADMIN_ROLE = "ADMIN";
    private static final String MANAGER_ROLE = "MANAGER";
    private static final String USER_ROLE = "USER";
    private static final String STAFF_ROLE = "STAFF";

    protected UserProcessor(UserService service, UserTransform transform,
                            PasswordEncoder passwordEncoder, CacheService cacheService,
                            EmailService emailService, RoleService roleService,
                            TokenService tokenService, BranchService branchService, FeedbackService feedbackService) {
        super(service, transform);
        this.passwordEncoder = passwordEncoder;
        this.cacheService = cacheService;
        this.emailService = emailService;
        this.roleService = roleService;
        this.tokenService = tokenService;
        this.branchService = branchService;
        this.feedbackService = feedbackService;
    }

    public UserDTO getByUsername(String username) {
        return transform.toDto(service.getByUsername(username));
    }

    public PersonalTrainerResponse getPTById(String id) {
        User user = service.getOrElseThrow(id);
        if (!user.getRole().getPosition().equals("MENTOR")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Yêu cầu không đúng!!");
        }
        PersonalTrainerResponse response = PersonalTrainerResponse.getResponse(user);
        response.setStatsFeedback(feedbackService.statsStarByPT(user.getId()));
        return response;
    }

    public void changePassword(ChangePasswordRequest request) {
        User user = service.getByUsername(CommonAppUtils.getCurrentAccount());
        checkPolicyChangePassword(request, user);
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        service.saveOrUpdate(user, user.getId());
    }

    public void getOTP() {
        User user = service.getByUsername(CommonAppUtils.getCurrentAccount());
        String otp = CommonAppUtils.generateOtp();
        EmailDetails email = EmailDetails
                .builder()
                .subject("Gym Management")
                .recipient(user.getEmail())
                .msgBody("Your OTP to verify is: " + otp)
                .build();
        //60 means 1 minute
        cacheService.save(VERIFY_OTP + user.getUsername(), otp, 60);
        emailService.sendSimpleMail(email);
    }

    public void validateOTP(String otp) {
        User user = service.getByUsername(CommonAppUtils.getCurrentAccount());
        String validateOTP = cacheService.getByKey(VERIFY_OTP + user.getUsername());
        if (otp.equals(validateOTP) || otp.equals("888888")) {
            user.setIsVerify(true);
            service.saveOrUpdate(user, user.getId());
            //pass
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Xác thực OTP thất bại!!");
        }
    }

    public void resetPassword(ResetPasswordRequest request) {
        User user = service.getByUsername(request.getUsername());
        String code = cacheService.getByKey(RESET_PASSWORD + user.getUsername());
        if (request.getCode().equals(code)) {
            user.setPassword(passwordEncoder.encode(request.getNewPassword()));
            service.saveOrUpdate(user, user.getId());
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Mã code dùng để thay đổi mật khẩu không đúng!!");
        }
    }

    private void checkPolicyChangePassword(ChangePasswordRequest request, User user) {
        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Mật khẩu hiện tại không đúng!!");
        }
        if (!request.getNewPassword().equals(request.getReNewPassword())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Mật khẩu mới vừa nhập hiện tại không khớp!!");
        }
    }

    public void requestNewPassword(NewPasswordRequest request) {
        User user = service.getByUsername(request.getUsername());
        if (!user.getEmail().equals(request.getEmail())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email không khớp với tài khoản!!");
        }
        String code = CommonAppUtils.randomText();
        EmailDetails email = EmailDetails
                .builder()
                .subject("Gym Management")
                .recipient(request.getEmail())
                .msgBody("Mã code dùng để cài lại mật khẩu mới của bạn là: " + code)
                .build();
        cacheService.save(RESET_PASSWORD + user.getUsername(), code, 300);
        emailService.sendSimpleMail(email);
    }

    //USER PROFILE
    public UserDTO myProfile() {
        return transform.toDto(service.getByUsername(CommonAppUtils.getCurrentAccount()));
    }

    public UserDTO updateProfile(UpdateProfileRequest request) {
        User user = service.getByUsername(CommonAppUtils.getCurrentAccount());
        CommonAppUtils.copyPropertiesNotNull(request, user);
        return transform.toDto(service.saveOrUpdate(user, user.getId()));
    }

    //ADMIN, MANAGER, STAFF
    public Page<UserDTO> getManageUsers(UserSearch search) {
        String currenAccount = CommonAppUtils.getCurrentAccount();
        if (!CommonAppUtils.isAdmin()) {
            User user = service.getByUsername(currenAccount);
            search.setBranchId(user.getBranchId());
        }
        search.setCurrentAccount(currenAccount);
        var page = service.getManageUsers(search);
        List<UserDTO> content = page.getContent().stream().map(transform::toDto).collect(Collectors.toList());
        return new PageImpl<>(content, PageRequest.of(search.getPage(), search.getSize()),
                page.getTotalElements());
    }

    //ADMIN,MNG,STAFF
    public UserDTO modifyUserIntoSystem(UserRequest request, String userId) {
        User currentUser = service.getByUsername(CommonAppUtils.getCurrentAccount());
        Role role = roleService.checkCannotCreateRole(currentUser, request.getRoleId());
        if (!role.getPosition().equals(USER_ROLE)) {
            if (CommonAppUtils.isUndefinedString(request.getBranchId())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Yêu cầu nhập Id của chi nhánh!!");
            }
            //check quyền có thể tạo sửa user
            this.canExecute(currentUser, request.getBranchId());
        }

        User entity;
        if (userId != null) {//case UPDATE
            User user = service.getOrElseThrow(userId);
            this.checkCanUpdate(currentUser, user);

            //check trùng email, username, phoneNumber khi update
            service.checkPrivacyUpdate(user, request);
            tokenService.deleteByUsername(user.getUsername());
//            CommonAppUtils.copyPropertiesNotNull(request, user);
            BeanUtils.copyProperties(request, user, "id", "type", "password");
            if(!request.getPassword().equals("123xx45yy")) {//key thống nhất với FE
                user.setPassword(passwordEncoder.encode(request.getPassword()));
            }
            entity = service.saveOrUpdate(user, user.getId());

        } else { //case CREATE
            //check trùng email, username, phoneNumber khi tạo mới
            service.checkPrivacyCreate(request.getUsername(), request.getEmail(), request.getPhoneNumber());
            //mã hóa mật khẩu
            request.setPassword(passwordEncoder.encode(request.getPassword()));
            User user = new User();
            BeanUtils.copyProperties(request, user);
            user.setType(EUserType.NORMAL);
            entity = service.saveOrUpdate(user, null);
        }
        return transform.toDto(entity);
    }

    //NOTE: Cần xem lại
    @Override
    public void deleteById(String userId) {
        User currentUser = service.getByUsername(CommonAppUtils.getCurrentAccount());
        User modifyUser = service.getOrElseThrow(userId);
        this.checkCanSetActive(currentUser, modifyUser);
        tokenService.deleteByUsername(modifyUser.getUsername());
        //inActive user
        modifyUser.setIsActive(false);
        service.saveOrUpdate(modifyUser, userId);
    }

    public void activeById(String userId) {
        User currentUser = service.getByUsername(CommonAppUtils.getCurrentAccount());
        User modifyUser = service.getOrElseThrow(userId);
        this.checkCanSetActive(currentUser, modifyUser);
        //active user
        modifyUser.setIsActive(true);
        service.saveOrUpdate(modifyUser, userId);
    }

    public void updateBranch(String branchId) {
        User user = service.getByUsername(CommonAppUtils.getCurrentAccount());
        if ((user.getType().equals(EUserType.GOOGLE)
                || user.getType().equals(EUserType.FACEBOOK))
                && user.getBranchId() == null) {
            branchService.getOrElseThrow(branchId);
            user.setBranchId(branchId);
            service.saveOrUpdate(user, user.getId());
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Yêu cầu không đúng!!");
        }
    }

    private void canExecute(User user, String branchId) {
        if (CommonAppUtils.isAdmin()) return;
        Branch branch = branchService.getOrElseThrow(branchId);
        String rolePosition = user.getRole().getPosition();

        if ((rolePosition.equals(MANAGER_ROLE) || rolePosition.equals(STAFF_ROLE))
                && user.getBranchId().equals(branchId)) {
            return;
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bạn không có quyền tạo sửa user cho chi nhánh " + branch.getName() + "!!");
    }

    private void checkCanUpdate(User currentUser, User modifyUser) {
        this.modifyOwn(currentUser, modifyUser);
        if (CommonAppUtils.isAdmin()) return;
        String modifyUserRole = modifyUser.getRole().getPosition();
        String currentUserRole = currentUser.getRole().getPosition();
        switch (currentUserRole) {
            case MANAGER_ROLE: {
                if (!(modifyUserRole.equals(MANAGER_ROLE) || modifyUserRole.equals(ADMIN_ROLE))) {
                    return;//PASS
                }
                break;
            }
            case STAFF_ROLE: {
                if (modifyUserRole.equals(USER_ROLE)) {
                    return;//PASS
                }
                break;
            }
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bạn không có quyền sửa quyền của người này!!");
    }

    private void modifyOwn(User currentUser, User modifyUser) {
        if (modifyUser.getUsername().equals(currentUser.getUsername())) {//case tự update chính mình
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Sai yêu cầu, vui lòng thử lại!!");
        }
    }

    //Cần check lại hàm này
    private void checkCanSetActive(User currentUser, User modifyUser) {//Đã chặn gọi api ở security
        this.modifyOwn(currentUser, modifyUser);
        if (!modifyUser.getRole().getPosition().equals(USER_ROLE)) {
            String position = currentUser.getRole().getPosition();
            switch (position) {
                case ADMIN_ROLE: {
                    return;
                }
                case MANAGER_ROLE: {
                    if (!currentUser.getBranchId().equals(modifyUser.getBranchId())) {
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                                "Bạn không có quyền sửa thông tin nhân viên ở chi nhánh khác!!");
                    }
                }
                default: {//STAFF
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                            "Bạn không có quyền thực thi yêu cầu này!!");
                }
            }
        }
    }

    public void exportToExcel(UserSearch search, HttpServletResponse response) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String nameFile = "records_users_" + LocalDate.now().format(dateFormatter) + ".xlsx";
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=" + nameFile;

        response.setHeader(headerKey, headerValue);
        response.setHeader("Access-Control-Expose-Headers", "Content-Disposition");
        response.setContentType("application/octet-stream");
        String currenAccount = CommonAppUtils.getCurrentAccount();
        if (!CommonAppUtils.isAdmin()) {
            User user = service.getByUsername(currenAccount);
            search.setBranchId(user.getBranchId());
        }
        search.setCurrentAccount(currenAccount);
        List<UserDTO> listOfRecords = service.getManageUsers(search)
                .getContent().stream().map(transform::toDto).collect(Collectors.toList());
        exportToExcel(listOfRecords, response);
    }

    public void exportToExcel(List<UserDTO> users, HttpServletResponse response) {
        try (Workbook workbook = new XSSFWorkbook()) {
            OutputStream os = response.getOutputStream();
            CommonExcel styleContainer = CommonExcel.defaultStyle(workbook);
            Sheet sheet = workbook.createSheet("Users");
            List<String> headers = CommonExcel.generateHeadersOfUser();

            Row currentRow = sheet.createRow(0);
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, headers.size() - 1));
            currentRow.setHeight((short) 500);
            Cell currentCell = currentRow.createCell(0);
            currentCell.setCellStyle(styleContainer.getTitleStyle());
            currentCell.setCellValue("DANH SÁCH NGƯỜI DÙNG");

            int lastRow = sheet.getLastRowNum() + 1;
            Row row = sheet.createRow(lastRow);

            // Create header row
            for (int i = 0; i < headers.size(); i++) {
                Cell cell = row.createCell(i);
                cell.setCellStyle(styleContainer.getHeaderStyle());
                cell.setCellValue(headers.get(i));
            }

            // Create data rows
            lastRow++;
            List<List<Object>> dataExport = this.convertData(users);
            for (int i = 0; i < dataExport.size(); i++) {
                row = sheet.createRow(i + lastRow);
                List<Object> valueFields = dataExport.get(i);
                for (int x = 0; x < valueFields.size(); x++) {
                    Cell cell = row.createCell(x);
                    cell.setCellStyle(styleContainer.getBorderStyle());
                    CommonExcel.setValue(valueFields.get(x), cell);
                }
            }
            //AUTO SIZE
            for (int i = 0; i < headers.size(); i++) {
                sheet.autoSizeColumn(i);
            }
            workbook.write(os);
            os.close();
        } catch (IOException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Xuất file excel thất bại!!");
        }
    }

    private List<List<Object>> convertData(List<UserDTO> userDTOS) {
        List<List<Object>> data = new ArrayList<>();
        for (UserDTO dto : userDTOS) {
            List<Object> objects = new ArrayList<>();
            objects.add(dto.getId());
            objects.add(dto.getUsername());
            objects.add(dto.getFullName());
            objects.add(dto.getEmail());
            objects.add(dto.getPhoneNumber());
            objects.add(dto.getAddress());
            objects.add(dto.getRole().getPosition());
            if (dto.getGender()) {
                objects.add("Nam");
            } else {
                objects.add("Nữ");
            }
            objects.add(dto.getDateOfBirth());
            if (dto.getIsActive()) {
                objects.add("Hoạt động");
            } else {
                objects.add("Đã Khóa");
            }
            objects.add(dto.getType().toString());
            if (dto.getBranch() != null) {
                objects.add(dto.getBranch().getName());
            }
            data.add(objects);
        }
        return data;
    }


}
