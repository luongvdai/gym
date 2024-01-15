package fpt.gymmanagement.job;

import fpt.gymmanagement.dto.request.EmailDetails;
import fpt.gymmanagement.entity.UserCourse;
import fpt.gymmanagement.enums.EUserCourseStatus;
import fpt.gymmanagement.service.EmailService;
import fpt.gymmanagement.service.TokenService;
import fpt.gymmanagement.service.UserCourseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
@Slf4j
@Transactional
public class ApplicationJobs {
    private final EmailService emailService;
    private final TokenService tokenService;
    private final UserCourseService userCourseService;

    public ApplicationJobs(EmailService emailService, TokenService tokenService, UserCourseService userCourseService) {
        this.emailService = emailService;
        this.tokenService = tokenService;
        this.userCourseService = userCourseService;
    }

    @Scheduled(cron = "0 */2 * * * *") //Chạy mỗi 2 phút(Dùng để test nhanh)
//    @Scheduled(cron = "0 0 0 * * ?")//Chạy 12h đêm hằng ngày
    public void updateCompleteUserCourses() {
        log.info("====================> EXECUTE JOB USER COURSES");
        List<UserCourse> userCourses = userCourseService.findAllInProgress();
        log.info("====================> updateCompleteUserCourses. Start with size: " + userCourses.size());
        long currentDate = new Date().getTime();
        List<UserCourse> dataUpdateDB = new ArrayList<>();

        for (UserCourse userCourse : userCourses) {
            if (currentDate > userCourse.getEndDate()) {
                userCourse.setStatus(EUserCourseStatus.COMPLETE_COURSE);
                dataUpdateDB.add(userCourse);
            }
        }

        if (!dataUpdateDB.isEmpty()) {
            log.info("====================> updateCompleteUserCourses. Start send email!!");
            userCourseService.saveByList(dataUpdateDB);
            this.sendMessageCompleteCourse(userCourses);
        } else {
            log.info("====================> updateCompleteUserCourses. No data update!!");
        }
    }

    @Async
    protected void sendMessageCompleteCourse(List<UserCourse> userCourses) {
        for (UserCourse u : userCourses) {
            String userEmail = u.getUser().getEmail();
            EmailDetails email = EmailDetails
                    .builder()
                    .subject("Gym Management")
                    .recipient(userEmail)
                    .msgBody("Chúc mừng " + u.getUser().getFullName() + " đã hoàn thành khóa học :"
                            + u.getPtCourse().getCourse().getTitle() + "." +
                            "\nBạn có thể vào viết đánh giá cho người huấn luyện của mình. " +
                            "Xin chân thành cảm ơn và chúc bạn thật nhiều sức khỏe!!")
                    .build();
            emailService.sendSimpleMail(email);
            log.info("=======================> sendMessageCompleteCourse. Send success to: " + userEmail);
        }
    }

    @Scheduled(cron = "0 */5 * * * *")
    public void clearTokens() {
        log.info("====================> EXECUTE JOB CLEAR TOKENSS");
        tokenService.clearTokenExpired();
        log.info("====================> END JOB CLEAR TOKENSS");
    }
}
