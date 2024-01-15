package fpt.gymmanagement.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import fpt.gymmanagement.dto.request.user.ChangePasswordRequest;
import fpt.gymmanagement.dto.request.user.NewPasswordRequest;
import fpt.gymmanagement.dto.request.user.ResetPasswordRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    MockMvc mvc;

    //=============================================CHANGE PASSWORD REQUEST=======================================================

    static ChangePasswordRequest initialErrorCurrentPassword() {
        return ChangePasswordRequest.builder().newPassword("a").reNewPassword("a").build();
    }

    static ChangePasswordRequest initialErrorNewPassword() {
        return ChangePasswordRequest.builder().currentPassword("1").reNewPassword("a").build();
    }

    static ChangePasswordRequest initialErrorReNewPassword() {
        return ChangePasswordRequest.builder().currentPassword("1").newPassword("21").build();
    }

    @Test
    void testChangePasswordErrorCurrentPassword() throws Exception {
        ChangePasswordRequest request = initialErrorCurrentPassword();
        String json = new ObjectMapper().writeValueAsString(request);
        MvcResult result = mvc.perform(patch("/users/change-password")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest()).andReturn();
        String x = result.getResolvedException().getMessage();
        Assertions.assertTrue(x.contains("Mật khẩu hiện tại không được bỏ trống"));
    }

    @Test
    void testChangePasswordErrorNewPassword() throws Exception {
        ChangePasswordRequest request = initialErrorNewPassword();
        String json = new ObjectMapper().writeValueAsString(request);
        MvcResult result = mvc.perform(patch("/users/change-password")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest()).andReturn();
        String x = result.getResolvedException().getMessage();
        Assertions.assertTrue(x.contains("Mật khẩu mới không được bỏ trống"));
    }

    @Test
    void testChangePasswordErrorReNewPassword() throws Exception {
        ChangePasswordRequest request = initialErrorReNewPassword();
        String json = new ObjectMapper().writeValueAsString(request);
        MvcResult result = mvc.perform(patch("/users/change-password")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest()).andReturn();
        String x = result.getResolvedException().getMessage();
        Assertions.assertTrue(x.contains("Mật khẩu mới lần 2 không được bỏ trống"));
    }

    //=============================================REQUEST NEW PASSWORD=======================================================

    static NewPasswordRequest initialErrorUsername() {
        return NewPasswordRequest.builder().email("dasdasdas@gmail.com").build();
    }

    static NewPasswordRequest initialErrorEmail() {
        return NewPasswordRequest.builder().username("dasdaasdas").build();
    }

    @Test
    void testGetNewPasswordErrorUsername() throws Exception {
        NewPasswordRequest request = initialErrorUsername();
        String json = new ObjectMapper().writeValueAsString(request);
        MvcResult result = mvc.perform(get("/users/request-new-password")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest()).andReturn();
        String x = result.getResolvedException().getMessage();
        Assertions.assertTrue(x.contains("Username không được bỏ trống"));
    }

    @Test
    void testGetNewPasswordErrorEmail() throws Exception {
        NewPasswordRequest request = initialErrorEmail();
        String json = new ObjectMapper().writeValueAsString(request);
        MvcResult result = mvc.perform(get("/users/request-new-password")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest()).andReturn();
        String x = result.getResolvedException().getMessage();
        Assertions.assertTrue(x.contains("Email không được bỏ trống"));
    }


    //=============================================RESET PASSWORD=======================================================
    static ResetPasswordRequest initialResetPasswordRequestErrorUsername() {
        return ResetPasswordRequest.builder().newPassword("213211").code("21321").build();
    }

    static ResetPasswordRequest initialResetPasswordRequestErrorCode() {
        return ResetPasswordRequest.builder().newPassword("213211").username("haskjdhask").build();
    }

    static ResetPasswordRequest initialResetPasswordRequestErrorPassword() {
        return ResetPasswordRequest.builder().username("hkdajshda21").code("21321").build();
    }

//    @Test
//    void testResetPasswordErrorUsername() throws Exception {
//        ResetPasswordRequest request = initialResetPasswordRequestErrorUsername();
//        String json = new ObjectMapper().writeValueAsString(request);
//        MvcResult result = mvc.perform(post("/users/reset-password")
//                        .content(json)
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isBadRequest()).andReturn();
//        String x = result.getResolvedException().getMessage();
//        Assertions.assertTrue(x.contains("Username không được bỏ trống"));
//    }
//
//    @Test
//    void testResetPasswordErrorCode() throws Exception {
//        ResetPasswordRequest request = initialResetPasswordRequestErrorCode();
//        String json = new ObjectMapper().writeValueAsString(request);
//        MvcResult result = mvc.perform(post("/users/reset-password")
//                        .content(json)
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isBadRequest()).andReturn();
//        String x = result.getResolvedException().getMessage();
//        Assertions.assertTrue(x.contains("Mã reset mật khẩu không được bỏ trống"));
//    }
//
//    @Test
//    void testResetPasswordErrorNewPassword() throws Exception {
//        ResetPasswordRequest request = initialResetPasswordRequestErrorPassword();
//        String json = new ObjectMapper().writeValueAsString(request);
//        MvcResult result = mvc.perform(post("/users/reset-password")
//                        .content(json)
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isBadRequest()).andReturn();
//        String x = result.getResolvedException().getMessage();
//        Assertions.assertTrue(x.contains("Mật khẩu mới không được bỏ trống"));
//    }
}
