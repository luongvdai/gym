package fpt.gymmanagement.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import fpt.gymmanagement.config.security.filter.JwtAuthenticationFilter;
import fpt.gymmanagement.dto.FeedbackDTO;
import fpt.gymmanagement.processor.FeedbackProcessor;
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
public class FeedbackControllerTest {
    @Mock
    FeedbackProcessor processor;

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
        processor = Mockito.mock(FeedbackProcessor.class);
        userService = Mockito.mock(UserService.class);
        validator = Validation.buildDefaultValidatorFactory().getValidator();
        jwtAuthenticationFilter = Mockito.mock(JwtAuthenticationFilter.class);
    }

    private FeedbackDTO initialErrorUserCourseId() {
        return FeedbackDTO.builder()
                .content("dasdasdasdasda")
                .star(4.5)
                .build();
    }

    private FeedbackDTO initialErrorEmptyUserCourseId() {
        return FeedbackDTO.builder()
                .userCourseId("")
                .content("dasdasdasdasda")
                .star(4.5)
                .build();
    }

    private FeedbackDTO initialErrorContent() {
        return FeedbackDTO.builder()
                .userCourseId("1258bdcb-e460-4a02-b2a2-7d8f92c2b554")
                .star(4.5)
                .build();
    }

    private FeedbackDTO initialErrorEmptyContent() {
        return FeedbackDTO.builder()
                .userCourseId("1258bdcb-e460-4a02-b2a2-7d8f92c2b554")
                .content("")
                .star(4.5)
                .build();
    }

    private FeedbackDTO initialErrorStar() {
        return FeedbackDTO.builder()
                .userCourseId("1258bdcb-e460-4a02-b2a2-7d8f92c2b554")
                .content("dasdasdasdasda")
                .build();
    }

    private FeedbackDTO initialErrorMinStar() {
        return FeedbackDTO.builder()
                .userCourseId("1258bdcb-e460-4a02-b2a2-7d8f92c2b554")
                .content("dasdasdasdasda")
                .star(-1.0)
                .build();
    }

    private FeedbackDTO initialErrorMaxStar() {
        return FeedbackDTO.builder()
                .userCourseId("1258bdcb-e460-4a02-b2a2-7d8f92c2b554")
                .content("dasdasdasdasda")
                .star(6.0)
                .build();
    }

    @Test
    void testCreateErrorUserCourse() throws Exception {
        FeedbackDTO dto = initialErrorUserCourseId();
        String json = new ObjectMapper().writeValueAsString(dto);
        MvcResult result = mvc.perform(post("/feedbacks")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + CommonTest.TOKEN_ADMIN))
                .andExpect(status().isBadRequest()).andReturn();
        String x = result.getResolvedException().getMessage();
        Assertions.assertTrue(x.contains("ID khóa học người dùng không được bỏ trống"));
    }

    @Test
    void testCreateErrorEmptyUserCourse() throws Exception {
        FeedbackDTO dto = initialErrorEmptyUserCourseId();
        String json = new ObjectMapper().writeValueAsString(dto);
        MvcResult result = mvc.perform(post("/feedbacks")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + CommonTest.TOKEN_ADMIN))
                .andExpect(status().isBadRequest()).andReturn();
        String x = result.getResolvedException().getMessage();
        Assertions.assertTrue(x.contains("ID khóa học người dùng không được bỏ trống"));
    }

    @Test
    void testCreateErrorContent() throws Exception {
        FeedbackDTO dto = initialErrorContent();
        String json = new ObjectMapper().writeValueAsString(dto);
        MvcResult result = mvc.perform(post("/feedbacks")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + CommonTest.TOKEN_ADMIN))
                .andExpect(status().isBadRequest()).andReturn();
        String x = result.getResolvedException().getMessage();
        Assertions.assertTrue(x.contains("Lời đánh giá không được bỏ trống"));
    }

    @Test
    void testCreateErrorContentEmpty() throws Exception {
        FeedbackDTO dto = initialErrorEmptyContent();
        String json = new ObjectMapper().writeValueAsString(dto);
        MvcResult result = mvc.perform(post("/feedbacks")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + CommonTest.TOKEN_ADMIN))
                .andExpect(status().isBadRequest()).andReturn();
        String x = result.getResolvedException().getMessage();
        Assertions.assertTrue(x.contains("Lời đánh giá không được bỏ trống"));
    }

    @Test
    void testCreateErrorStar() throws Exception {
        FeedbackDTO dto = initialErrorStar();
        String json = new ObjectMapper().writeValueAsString(dto);
        MvcResult result = mvc.perform(post("/feedbacks")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + CommonTest.TOKEN_ADMIN))
                .andExpect(status().isBadRequest()).andReturn();
        String x = result.getResolvedException().getMessage();
        Assertions.assertTrue(x.contains("Đánh giá sao không được bỏ trống"));
    }

    @Test
    void testCreateErrorMinStar() throws Exception {
        FeedbackDTO dto = initialErrorMinStar();
        String json = new ObjectMapper().writeValueAsString(dto);
        MvcResult result = mvc.perform(post("/feedbacks")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + CommonTest.TOKEN_ADMIN))
                .andExpect(status().isBadRequest()).andReturn();
        String x = result.getResolvedException().getMessage();
        Assertions.assertTrue(x.contains("Gía trị không thể nhỏ hơn 1"));
    }

    @Test
    void testCreateErrorMaxStar() throws Exception {
        FeedbackDTO dto = initialErrorMaxStar();
        String json = new ObjectMapper().writeValueAsString(dto);
        MvcResult result = mvc.perform(post("/feedbacks")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + CommonTest.TOKEN_ADMIN))
                .andExpect(status().isBadRequest()).andReturn();
        String x = result.getResolvedException().getMessage();
        Assertions.assertTrue(x.contains("Gía trị không thể lớn hơn 5"));
    }
}
