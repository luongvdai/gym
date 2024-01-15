package fpt.gymmanagement.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import fpt.gymmanagement.dto.CourseTypeDTO;
import fpt.gymmanagement.dto.request.ptcourse.RemovePTRequest;
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
public class PTCourseControllerTest {
    @Autowired
    MockMvc mvc;


    @Test
    void testCreateCourseIdEmpty() throws Exception {
        RemovePTRequest request = new RemovePTRequest();
        request.setPtId("1258bdcb-e460-4a02-b2a2-7d8f92c2b554");
        request.setCourseId("");
        String json = new ObjectMapper().writeValueAsString(request);
        MvcResult result = mvc.perform(post("/pt-courses")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + CommonTest.TOKEN_ADMIN))
                .andExpect(status().isBadRequest()).andReturn();
        String x = result.getResolvedException().getMessage();
        Assertions.assertTrue(x.contains("ID của course không được bỏ trống"));
    }

    @Test
    void testCreateCourseIdNull() throws Exception {
        RemovePTRequest request = new RemovePTRequest();
        request.setPtId("1258bdcb-e460-4a02-b2a2-7d8f92c2b554");
        String json = new ObjectMapper().writeValueAsString(request);
        MvcResult result = mvc.perform(post("/pt-courses")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + CommonTest.TOKEN_ADMIN))
                .andExpect(status().isBadRequest()).andReturn();
        String x = result.getResolvedException().getMessage();
        Assertions.assertTrue(x.contains("ID của course không được bỏ trống"));
    }

    @Test
    void testCreatePTIdNull() throws Exception {
        RemovePTRequest request = new RemovePTRequest();
        request.setCourseId("3cc7c9b5-f8e3-49d7-b84f-0fe6a820bc92");
        String json = new ObjectMapper().writeValueAsString(request);
        MvcResult result = mvc.perform(post("/pt-courses")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + CommonTest.TOKEN_ADMIN))
                .andExpect(status().isBadRequest()).andReturn();
        String x = result.getResolvedException().getMessage();
        Assertions.assertTrue(x.contains("ID của mentor không được bỏ trống"));
    }

    @Test
    void testCreatePTIdEmpty() throws Exception {
        RemovePTRequest request = new RemovePTRequest();
        request.setPtId("");
        request.setCourseId("3cc7c9b5-f8e3-49d7-b84f-0fe6a820bc92");
        String json = new ObjectMapper().writeValueAsString(request);
        MvcResult result = mvc.perform(post("/pt-courses")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + CommonTest.TOKEN_ADMIN))
                .andExpect(status().isBadRequest()).andReturn();
        String x = result.getResolvedException().getMessage();
        Assertions.assertTrue(x.contains("ID của mentor không được bỏ trống"));
    }

    @Test
    void testCreateUnauthorized() throws Exception {
        RemovePTRequest request = new RemovePTRequest();
        request.setPtId("");
        request.setCourseId("3cc7c9b5-f8e3-49d7-b84f-0fe6a820bc92");
        String json = new ObjectMapper().writeValueAsString(request);
        MvcResult result = mvc.perform(post("/pt-courses")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer "))
                .andExpect(status().isUnauthorized()).andReturn();
    }
}
