package fpt.gymmanagement.controller.manage;

import fpt.gymmanagement.processor.UserCourseProcessor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("manage-user-courses")
public class ManageUserCourseController {
    private final UserCourseProcessor processor;

    public ManageUserCourseController(UserCourseProcessor processor) {
        this.processor = processor;
    }

    @DeleteMapping("{userId}")
    public ResponseEntity<?> removeUser(@PathVariable String userId){
        return null;
    }
}
