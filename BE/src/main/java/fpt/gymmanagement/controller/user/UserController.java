package fpt.gymmanagement.controller.user;

import fpt.gymmanagement.config.SwaggerConfiguration;
import fpt.gymmanagement.dto.request.order.OrderRequest;
import fpt.gymmanagement.dto.request.user.ChangePasswordRequest;
import fpt.gymmanagement.dto.request.user.NewPasswordRequest;
import fpt.gymmanagement.dto.request.user.ResetPasswordRequest;
import fpt.gymmanagement.dto.request.user.UpdateProfileRequest;
import fpt.gymmanagement.processor.OrderProcessor;
import fpt.gymmanagement.processor.UserProcessor;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
@Api(tags = {SwaggerConfiguration.USER_TAG})
public class UserController {
    private final UserProcessor processor;
    private final OrderProcessor orderProcessor;

    @PatchMapping("change-password")
    public ResponseEntity<?> getByUsername(@RequestBody @Valid ChangePasswordRequest request) {
        processor.changePassword(request);
        return ResponseEntity.ok().build();
    }

    @GetMapping("get-otp")
    public ResponseEntity<?> getOTP() {
        processor.getOTP();
        return ResponseEntity.ok().build();
    }

    @PostMapping("validate-otp")
    public ResponseEntity<?> validateOTP(@RequestParam String otp) {
        processor.validateOTP(otp);
        return ResponseEntity.ok().build();
    }

    @GetMapping("request-new-password")
    public ResponseEntity<?> getNewPassword(@ModelAttribute @Valid NewPasswordRequest request) {
        processor.requestNewPassword(request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody @Valid ResetPasswordRequest request) {
        processor.resetPassword(request);
        return ResponseEntity.ok().build();
    }

    @GetMapping("mentor/{id}")
    public ResponseEntity<?> getPTById(@PathVariable String id) {
        return ResponseEntity.ok(processor.getPTById(id));
    }

    @GetMapping("my-profile")
    public ResponseEntity<?> myProfile() {
        return ResponseEntity.ok(processor.myProfile());
    }

    @PostMapping("my-profile")
    public ResponseEntity<?> updateProfile(@RequestBody @Valid UpdateProfileRequest request) {
        return ResponseEntity.ok(processor.updateProfile(request));
    }

    @GetMapping("{username}/info")
    public ResponseEntity<?> getByUsername(@PathVariable String username) {
        return ResponseEntity.ok(processor.getByUsername(username));
    }

    @PostMapping("/create-order")
    public ResponseEntity<?> createOrderByUser(@RequestBody @Valid OrderRequest request) {
        return ResponseEntity.ok(orderProcessor.createByUser(request));
    }

    @PostMapping("/update-branch")
    public ResponseEntity<?> updateBranch(@RequestParam String branchId) {
        processor.updateBranch(branchId);
        return ResponseEntity.ok().build();
    }
}
