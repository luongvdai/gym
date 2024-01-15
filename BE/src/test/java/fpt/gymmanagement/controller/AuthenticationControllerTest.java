package fpt.gymmanagement.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import fpt.gymmanagement.dto.request.AuthenticationRequest;
import fpt.gymmanagement.dto.request.user.RegisterAccountRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthenticationControllerTest {
    @Autowired
    MockMvc mvc;

    @Test
    void testAuthenticateErrorUsername() throws Exception {
        AuthenticationRequest request = new AuthenticationRequest();
        request.setPassword("Abcd1234");
        String json = new ObjectMapper().writeValueAsString(request);
        MvcResult result = mvc.perform(post("/api/auth/authenticate")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest()).andReturn();
        String x = result.getResolvedException().getMessage();
        Assertions.assertTrue(x.contains("Username không được bỏ trống"));
    }

    @Test
    void testAuthenticateErrorUsernameEmpty() throws Exception {
        AuthenticationRequest request = new AuthenticationRequest();
        request.setUsername("");
        request.setPassword("Abcd1234");
        String json = new ObjectMapper().writeValueAsString(request);
        MvcResult result = mvc.perform(post("/api/auth/authenticate")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest()).andReturn();
        String x = result.getResolvedException().getMessage();
        Assertions.assertTrue(x.contains("Username không được bỏ trống"));
    }

    @Test
    void testAuthenticateErrorPassword() throws Exception {
        AuthenticationRequest request = new AuthenticationRequest();
        request.setUsername("huypape");
        String json = new ObjectMapper().writeValueAsString(request);
        MvcResult result = mvc.perform(post("/api/auth/authenticate")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest()).andReturn();
        String x = result.getResolvedException().getMessage();
        Assertions.assertTrue(x.contains("Password không được bỏ trống"));
    }

    @Test
    void testAuthenticateErrorPasswordEmpty() throws Exception {
        AuthenticationRequest request = new AuthenticationRequest();
        request.setUsername("huypape");
        request.setPassword("");
        String json = new ObjectMapper().writeValueAsString(request);
        MvcResult result = mvc.perform(post("/api/auth/authenticate")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest()).andReturn();
        String x = result.getResolvedException().getMessage();
        Assertions.assertTrue(x.contains("Password không được bỏ trống"));
    }

    @Test
    void testRegisterErrorFullName() throws Exception {
        RegisterAccountRequest request = new RegisterAccountRequest();
        request.setUsername("ad213sad");
        request.setEmail("dasdas@gmail.com");
        request.setPassword("1223");
        request.setAddress("Hong Jd");
        request.setPhoneNumber("213056043");
        request.setGender(true);
        String json = new ObjectMapper().writeValueAsString(request);
        MvcResult result = mvc.perform(post("/api/auth/register")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest()).andReturn();
        String x = result.getResolvedException().getMessage();
        Assertions.assertTrue(x.contains("Họ và tên không được bỏ trống"));
    }

    @Test
    void testRegisterErrorUsername() throws Exception {
        RegisterAccountRequest request = new RegisterAccountRequest();
        request.setEmail("dasdas@gmail.com");
        request.setAddress("Hong Jd");
        request.setPassword("1223");
        request.setPhoneNumber("213056043");
        request.setGender(true);
        request.setFullName("HAITEST");
        String json = new ObjectMapper().writeValueAsString(request);
        MvcResult result = mvc.perform(post("/api/auth/register")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest()).andReturn();
        String x = result.getResolvedException().getMessage();
        Assertions.assertTrue(x.contains("Tên đăng nhập không được bỏ trống"));
    }

    @Test
    void testRegisterErrorAddress() throws Exception {
        RegisterAccountRequest request = new RegisterAccountRequest();
        request.setUsername("ad213sad");
        request.setPassword("1223");
        request.setEmail("dasdas@gmail.com");
        request.setPhoneNumber("213056043");
        request.setGender(true);
        request.setFullName("HAITEST");
        String json = new ObjectMapper().writeValueAsString(request);
        MvcResult result = mvc.perform(post("/api/auth/register")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest()).andReturn();
        String x = result.getResolvedException().getMessage();
        Assertions.assertTrue(x.contains("Địa chỉ không được bỏ trống"));
    }

    @Test
    void testRegisterErrorEmail() throws Exception {
        RegisterAccountRequest request = new RegisterAccountRequest();
        request.setUsername("ad213sad");
        request.setAddress("Hong Jd");
        request.setPassword("1223");
        request.setPhoneNumber("213056043");
        request.setGender(true);
        request.setFullName("HAITEST");
        String json = new ObjectMapper().writeValueAsString(request);
        MvcResult result = mvc.perform(post("/api/auth/register")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest()).andReturn();
        String x = result.getResolvedException().getMessage();
        Assertions.assertTrue(x.contains("Email không được bỏ trống"));
    }

    @Test
    void testRegisterErrorPassword() throws Exception {
        RegisterAccountRequest request = new RegisterAccountRequest();
        request.setUsername("ad213sad");
        request.setEmail("dasdas@gmail.com");
        request.setAddress("Hong Jd");
        request.setPhoneNumber("213056043");
        request.setFullName("HAITEST");
        request.setGender(true);
        String json = new ObjectMapper().writeValueAsString(request);
        MvcResult result = mvc.perform(post("/api/auth/register")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest()).andReturn();
        String x = result.getResolvedException().getMessage();
        Assertions.assertTrue(x.contains("Mật khẩu không được bỏ trống"));
    }

    @Test
    void testRegisterErrorPhoneNumber() throws Exception {
        RegisterAccountRequest request = new RegisterAccountRequest();
        request.setUsername("ad213sad");
        request.setEmail("dasdas@gmail.com");
        request.setPassword("1223");
        request.setAddress("Hong Jd");
        request.setFullName("HAITEST");
        request.setGender(true);
        String json = new ObjectMapper().writeValueAsString(request);
        MvcResult result = mvc.perform(post("/api/auth/register")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest()).andReturn();
        String x = result.getResolvedException().getMessage();
        Assertions.assertTrue(x.contains("Số điện thoại không được bỏ trống"));
    }

//    @Test
//    void testRegisterErrorPhoneNumberEmpty() throws Exception {
//        RegisterAccountRequest request = new RegisterAccountRequest();
//        request.setUsername("ad213sad");
//        request.setEmail("dasdas@gmail.com");
//        request.setPhoneNumber("");
//        request.setPassword("1223");
//        request.setAddress("Hong Jd");
//        request.setFullName("HAITEST");
//        request.setGender(true);
//        String json = new ObjectMapper().writeValueAsString(request);
//        MvcResult result = mvc.perform(post("/api/auth/register")
//                        .content(json)
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isBadRequest()).andReturn();
//        String x = result.getResolvedException().getMessage();
//        Assertions.assertTrue(x.contains("Số điện thoại không được bỏ trống"));
//    }

    @Test
    void testRegisterErrorGender() throws Exception {
        RegisterAccountRequest request = new RegisterAccountRequest();
        request.setUsername("ad213sad");
        request.setEmail("dasdas@gmail.com");
        request.setAddress("Hong Jd");
        request.setPassword("1223");
        request.setPhoneNumber("213056043");
        request.setFullName("HAITEST");
        String json = new ObjectMapper().writeValueAsString(request);
        MvcResult result = mvc.perform(post("/api/auth/register")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest()).andReturn();
        String x = result.getResolvedException().getMessage();
        Assertions.assertTrue(x.contains("Yêu cầu nhập giới tính"));
    }
}
