package fpt.gymmanagement.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import fpt.gymmanagement.config.security.filter.JwtAuthenticationFilter;
import fpt.gymmanagement.dto.GuestConsultantDTO;
import fpt.gymmanagement.processor.GuestConsultantProcessor;
import fpt.gymmanagement.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import javax.validation.Validation;
import javax.validation.Validator;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class GuestConsultantControllerTest {

    @Mock
    GuestConsultantProcessor processor;
    @Mock
    UserService userService;
    @Autowired
    Validator validator;
    @Autowired
    MockMvc mvc;
    @Autowired
    JwtAuthenticationFilter jwtAuthenticationFilter;

    @BeforeEach
    void setup() {
        processor = Mockito.mock(GuestConsultantProcessor.class);
        userService = Mockito.mock(UserService.class);
        validator = Validation.buildDefaultValidatorFactory().getValidator();
        jwtAuthenticationFilter = Mockito.mock(JwtAuthenticationFilter.class);
    }

    private GuestConsultantDTO initialErrorName() {
        return GuestConsultantDTO.builder()
                .phoneNumber("0904785351")
                .email("truongtrinh.m@gmail.com")
                .note("An nhieu rau nhung van beo")
                .branchId("branchId 1")
                .build();
    }

    private GuestConsultantDTO initialErrorPhoneNumber() {
        return GuestConsultantDTO.builder()
                .name("Trinh Xuan Truong")
                .email("truongtrinh.m@gmail.com")
                .note("An nhieu rau nhung van beo")
                .branchId("branchId 1")
                .build();
    }

    private GuestConsultantDTO initialErrorEmail() {
        return GuestConsultantDTO.builder()
                .name("Trinh Xuan Truong")
                .phoneNumber("0904785351")
                .note("An nhieu rau nhung van beo")
                .branchId("branchId 1")
                .build();
    }

    private GuestConsultantDTO initialErrorNote() {
        return GuestConsultantDTO.builder()
                .name("Trinh Xuan Truong")
                .phoneNumber("0904785351")
                .email("truongtrinh.m@gmail.com")
                .branchId("branchId 1")
                .build();
    }

    private GuestConsultantDTO initialErrorBranchId() {
        return GuestConsultantDTO.builder()
                .name("Trinh Xuan Truong")
                .phoneNumber("0904785351")
                .email("truongtrinh.m@gmail.com")
                .note("An nhieu rau nhung van beo")
                .build();
    }

    @Test
    void testCreateErrorName() throws Exception {
        GuestConsultantDTO dto = initialErrorName();
        String json = new ObjectMapper().writeValueAsString(dto);
        MvcResult result = mvc.perform(post("/guest-consultant")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + CommonTest.TOKEN_ADMIN))
                .andExpect(status().isBadRequest()).andReturn();
        String x = result.getResolvedException().getMessage();
        Assertions.assertTrue(x.contains("Tên của bạn không được bỏ trống"));
    }

    @Test
    void testCreateErrorPhoneNumber() throws Exception {
        GuestConsultantDTO dto = initialErrorPhoneNumber();
        String json = new ObjectMapper().writeValueAsString(dto);
        MvcResult result = mvc.perform(post("/guest-consultant")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + CommonTest.TOKEN_ADMIN))
                .andExpect(status().isBadRequest()).andReturn();
        String x = result.getResolvedException().getMessage();
        Assertions.assertTrue(x.contains("Số điện thoại không được bỏ trống"));
    }

    @Test
    void testCreateErrorEmail() throws Exception {
        GuestConsultantDTO dto = initialErrorEmail();
        String json = new ObjectMapper().writeValueAsString(dto);
        MvcResult result = mvc.perform(post("/guest-consultant")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + CommonTest.TOKEN_ADMIN))
                .andExpect(status().isBadRequest()).andReturn();
        String x = result.getResolvedException().getMessage();
        Assertions.assertTrue(x.contains("Email Không được bỏ trống"));
    }

    @Test
    void testCreateErrorNote() throws Exception {
        GuestConsultantDTO dto = initialErrorNote();
        String json = new ObjectMapper().writeValueAsString(dto);
        MvcResult result = mvc.perform(post("/guest-consultant")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + CommonTest.TOKEN_ADMIN))
                .andExpect(status().isBadRequest()).andReturn();
        String x = result.getResolvedException().getMessage();
        Assertions.assertTrue(x.contains("Ghi chú Không được bỏ trống"));
    }

    @Test
    void testCreateErrorBranch() throws Exception {
        GuestConsultantDTO dto = initialErrorBranchId();
        String json = new ObjectMapper().writeValueAsString(dto);
        MvcResult result = mvc.perform(post("/guest-consultant")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + CommonTest.TOKEN_ADMIN))
                .andExpect(status().isBadRequest()).andReturn();
        String x = result.getResolvedException().getMessage();
        Assertions.assertTrue(x.contains("Chi nhánh không được bỏ trống"));
    }

}
