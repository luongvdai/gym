package fpt.gymmanagement.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import fpt.gymmanagement.dto.CourseTypeDTO;
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
public class CourseTypeControllerTest {
    @Autowired
    MockMvc mvc;

    @Test
    void testCreateSuccess() throws Exception {
        CourseTypeDTO dto = CourseTypeDTO.builder().name("course type moi").build();
        String json = new ObjectMapper().writeValueAsString(dto);
        MvcResult result = mvc.perform(post("/course-types")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + CommonTest.TOKEN_ADMIN))
                .andExpect(status().isOk()).andReturn();
    }

    @Test
    void testCreateErrorEmpty() throws Exception {
        CourseTypeDTO dto = CourseTypeDTO.builder().name("").build();
        String json = new ObjectMapper().writeValueAsString(dto);
        MvcResult result = mvc.perform(post("/course-types")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + CommonTest.TOKEN_ADMIN))
                .andExpect(status().isBadRequest()).andReturn();
        String x = result.getResolvedException().getMessage();
        Assertions.assertTrue(x.contains("Tên kiểu khóa học không được bỏ trống"));
    }

    @Test
    void testCreateErrorNull() throws Exception {
        CourseTypeDTO dto = CourseTypeDTO.builder().build();
        String json = new ObjectMapper().writeValueAsString(dto);
        MvcResult result = mvc.perform(post("/course-types")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + CommonTest.TOKEN_ADMIN))
                .andExpect(status().isBadRequest()).andReturn();
        String x = result.getResolvedException().getMessage();
        Assertions.assertTrue(x.contains("Tên kiểu khóa học không được bỏ trống"));
    }

    @Test
    void testCreateErrorLength() throws Exception {
        CourseTypeDTO dto = CourseTypeDTO.builder().name("5te").build();
        String json = new ObjectMapper().writeValueAsString(dto);
        MvcResult result = mvc.perform(post("/course-types")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + CommonTest.TOKEN_ADMIN))
                .andExpect(status().isBadRequest()).andReturn();
        String x = result.getResolvedException().getMessage();
        Assertions.assertTrue(x.contains("Yêu cầu nhập độ dài phù hợp"));
    }
}
