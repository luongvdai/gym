package fpt.gymmanagement.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import fpt.gymmanagement.config.security.filter.JwtAuthenticationFilter;
import fpt.gymmanagement.dto.CourseDTO;
import fpt.gymmanagement.processor.CourseProcessor;
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
public class CourseControllerTest {
    @Mock
    CourseProcessor processor;
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
        processor = Mockito.mock(CourseProcessor.class);
        userService = Mockito.mock(UserService.class);
        validator = Validation.buildDefaultValidatorFactory().getValidator();
        jwtAuthenticationFilter = Mockito.mock(JwtAuthenticationFilter.class);
    }

    private CourseDTO initialSuccess() {
        return CourseDTO.builder()
                .title("Hoc yoga deo nguoi")
                .rangeProcess(2)
                .description("Khoa tac giup co the deo dai hơn")
                .isActive(true)
                .price(20000000.0)
                .courseTypeId("TypeId1")
                .branchId("branchId1")
                .build();
    }

    private CourseDTO initialErrorTitle() {
        return CourseDTO.builder()
                .rangeProcess(2)
                .description("Khoa tac giup co the deo dai hơn")
                .isActive(true)
                .price(20000000.0)
                .courseTypeId("TypeId1")
                .branchId("branchId1")
                .build();
    }

    private CourseDTO initialErrorRangeProcess() {
        return CourseDTO.builder()
                .title("Hoc yoga deo nguoi")
                .description("Khoa tac giup co the deo dai hơn")
                .isActive(true)
                .price(20000000.0)
                .courseTypeId("Type1")
                .branchId("branchId1")
                .build();
    }

    private CourseDTO initialErrorDescription() {
        return CourseDTO.builder()
                .title("Hoc yoga deo nguoi")
                .rangeProcess(2)
                .isActive(true)
                .price(20000000.0)
                .courseTypeId("Type1")
                .branchId("branchId1")
                .build();
    }

    private CourseDTO initialErrorIsActive() {
        return CourseDTO.builder()
                .title("Hoc yoga deo nguoi")
                .rangeProcess(2)
                .description("Khoa tac giup co the deo dai hơn")
                .price(20000000.0)
                .courseTypeId("Type1")
                .branchId("branchId1")
                .build();
    }

    private CourseDTO initialErrorPrice() {
        return CourseDTO.builder()
                .title("Hoc yoga deo nguoi")
                .rangeProcess(2)
                .description("Khoa tac giup co the deo dai hơn")
                .isActive(true)
                .courseTypeId("Type1")
                .branchId("branchId1")
                .build();
    }

    private CourseDTO initialErrorCourseTypeId() {
        return CourseDTO.builder()
                .title("Hoc yoga deo nguoi")
                .rangeProcess(2)
                .description("Khoa tac giup co the deo dai hơn")
                .isActive(true)
                .price(20000000.0)
                .branchId("branchId1")
                .build();
    }

    private CourseDTO initialErrorBranchId() {
        return CourseDTO.builder()
                .title("Hoc yoga deo nguoi")
                .rangeProcess(2)
                .description("Khoa tac giup co the deo dai hơn")
                .isActive(true)
                .price(20000000.0)
                .courseTypeId("Type1")
                .build();
    }

    private CourseDTO initialErrorBoundRangeProcess() {
        return CourseDTO.builder()
                .title("Hoc yoga deo nguoi")
                .rangeProcess(-1)
                .description("Khoa tac giup co the deo dai hơn")
                .isActive(true)
                .price(20000000.0)
                .courseTypeId("Type1")
                .build();
    }

    @Test
    void testCreateErrorBoundRangeProcess() throws Exception {
        CourseDTO dto = initialErrorBoundRangeProcess();
        String json = new ObjectMapper().writeValueAsString(dto);
        MvcResult result = mvc.perform(post("/courses")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + CommonTest.TOKEN_ADMIN))
                .andExpect(status().isBadRequest()).andReturn();
        String x = result.getResolvedException().getMessage();
        Assertions.assertTrue(x.contains("Không nằm trong khoảng thời gian range process"));
    }

    @Test
    void testCreateErrorContent() throws Exception {
        CourseDTO dto = initialErrorTitle();
        String json = new ObjectMapper().writeValueAsString(dto);
        MvcResult result = mvc.perform(post("/courses")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + CommonTest.TOKEN_ADMIN))
                .andExpect(status().isBadRequest()).andReturn();
        String x = result.getResolvedException().getMessage();
        Assertions.assertTrue(x.contains("Tên khóa học không được bỏ trống"));
    }

    @Test
    void testCreateErrorRangeProcess() throws Exception {
        CourseDTO dto = initialErrorRangeProcess();
        String json = new ObjectMapper().writeValueAsString(dto);
        MvcResult result = mvc.perform(post("/courses")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + CommonTest.TOKEN_ADMIN))
                .andExpect(status().isBadRequest()).andReturn();
        String x = result.getResolvedException().getMessage();
        Assertions.assertTrue(x.contains("Thời gian khóa học không được bỏ trống"));
    }

    @Test
    void testCreateErrorDescription() throws Exception {
        CourseDTO dto = initialErrorDescription();
        String json = new ObjectMapper().writeValueAsString(dto);
        MvcResult result = mvc.perform(post("/courses")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + CommonTest.TOKEN_ADMIN))
                .andExpect(status().isBadRequest()).andReturn();
        String x = result.getResolvedException().getMessage();
        Assertions.assertTrue(x.contains("Mô tả khóa học không được bỏ trống"));
    }

    @Test
    void testCreateErrorIsActive() throws Exception {
        CourseDTO dto = initialErrorIsActive();
        String json = new ObjectMapper().writeValueAsString(dto);
        MvcResult result = mvc.perform(post("/courses")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + CommonTest.TOKEN_ADMIN))
                .andExpect(status().isBadRequest()).andReturn();
        String x = result.getResolvedException().getMessage();
        Assertions.assertTrue(x.contains("Trạng thái khóa học không được bỏ trống"));
    }

    @Test
    void testCreateErrorPrice() throws Exception {
        CourseDTO dto = initialErrorPrice();
        String json = new ObjectMapper().writeValueAsString(dto);
        MvcResult result = mvc.perform(post("/courses")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + CommonTest.TOKEN_ADMIN))
                .andExpect(status().isBadRequest()).andReturn();
        String x = result.getResolvedException().getMessage();
        Assertions.assertTrue(x.contains("Gía khóa học không được bỏ trống"));
    }

    @Test
    void testCreateErrorCourseTypeId() throws Exception {
        CourseDTO dto = initialErrorCourseTypeId();
        String json = new ObjectMapper().writeValueAsString(dto);
        MvcResult result = mvc.perform(post("/courses")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + CommonTest.TOKEN_ADMIN))
                .andExpect(status().isBadRequest()).andReturn();
        String x = result.getResolvedException().getMessage();
        Assertions.assertTrue(x.contains("Id kiểu khóa học không được bỏ trống"));
    }

    @Test
    void testCreateErrorBranchId() throws Exception {
        CourseDTO dto = initialErrorBranchId();
        String json = new ObjectMapper().writeValueAsString(dto);
        MvcResult result = mvc.perform(post("/courses")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + CommonTest.TOKEN_ADMIN))
                .andExpect(status().isBadRequest()).andReturn();
        String x = result.getResolvedException().getMessage();
        Assertions.assertTrue(x.contains("Id chi nhánh không được bỏ trống"));
    }
}
