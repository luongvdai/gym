package fpt.gymmanagement.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import fpt.gymmanagement.config.security.filter.JwtAuthenticationFilter;
import fpt.gymmanagement.dto.BlogDTO;
import fpt.gymmanagement.processor.BlogProcessor;
import fpt.gymmanagement.service.BlogTypeService;
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
public class BlogControllerTest {
    @Mock BlogProcessor processor;
    @Mock UserService userService;
    @Mock BlogTypeService blogTypeService;
    @Autowired Validator validator;
    @Autowired MockMvc mvc;
    @Autowired JwtAuthenticationFilter jwtAuthenticationFilter;

    @BeforeEach
    void setup() {
        processor = Mockito.mock(BlogProcessor.class);
        userService = Mockito.mock(UserService.class);
        blogTypeService = Mockito.mock(BlogTypeService.class);
        validator = Validation.buildDefaultValidatorFactory().getValidator();
        jwtAuthenticationFilter = Mockito.mock(JwtAuthenticationFilter.class);
    }

    //SET UP TEST CASE
    private BlogDTO initialSuccess() {
        return BlogDTO.builder()
                .content("Đi ăn rau")
                .title("Ăn uống")
                .attach_url("dasdas")
                .branchId("1")
                .typeId("1")
                .build();
    }

    private BlogDTO initialErrorContent() {
        return BlogDTO.builder()
                .title("Ăn uống")
                .attach_url("dasdas")
                .branchId("1")
                .typeId("1")
                .build();
    }

    private BlogDTO initialErrorTitle() {
        return BlogDTO.builder()
                .content("HAHAHAH")
                .attach_url("dasdas")
                .branchId("1")
                .typeId("1")
                .build();
    }

    @Test
    void testCreateErrorContent() throws Exception {
        BlogDTO dto = initialErrorContent();
        String json = new ObjectMapper().writeValueAsString(dto);
        MvcResult result = mvc.perform(post("/blogs")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + CommonTest.TOKEN_ADMIN))
                .andExpect(status().isBadRequest()).andReturn();
        String x = result.getResolvedException().getMessage();
        Assertions.assertTrue(x.contains("Nội dung blog không được bỏ trống"));
    }

    @Test
    void testCreateErrorTitle() throws Exception {
        BlogDTO dto = initialErrorTitle();
        String json = new ObjectMapper().writeValueAsString(dto);
        MvcResult result = mvc.perform(post("/blogs")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + CommonTest.TOKEN_ADMIN))
                .andExpect(status().isBadRequest()).andReturn();
        String x = result.getResolvedException().getMessage();
        Assertions.assertTrue(x.contains("Tiêu đề blog không được bỏ trống"));
    }

    @Test
    void testCreateErrorTitleEmpty() throws Exception {
        BlogDTO dto = BlogDTO.builder().title("").content("Đi ăn rau").typeId("1").branchId("1").build();
        String json = new ObjectMapper().writeValueAsString(dto);
        MvcResult result = mvc.perform(post("/blogs")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + CommonTest.TOKEN_ADMIN))
                .andExpect(status().isBadRequest()).andReturn();
        String x = result.getResolvedException().getMessage();
        Assertions.assertTrue(x.contains("Tiêu đề blog không được bỏ trống"));
    }

    @Test
    void testCreateErrorTypeId() throws Exception {
        BlogDTO dto = BlogDTO.builder().title("A").content("đâs").branchId("1").build();
        String json = new ObjectMapper().writeValueAsString(dto);
        MvcResult result = mvc.perform(post("/blogs")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + CommonTest.TOKEN_ADMIN))
                .andExpect(status().isBadRequest()).andReturn();
        String x = result.getResolvedException().getMessage();
        Assertions.assertTrue(x.contains("ID kiểu blog không được bỏ trống"));
    }

    @Test
    void testCreateErrorBranchId() throws Exception {
        BlogDTO dto = BlogDTO.builder().title("A").content("đâs").typeId("1").build();
        String json = new ObjectMapper().writeValueAsString(dto);
        MvcResult result = mvc.perform(post("/blogs")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + CommonTest.TOKEN_ADMIN))
                .andExpect(status().isBadRequest()).andReturn();
        String x = result.getResolvedException().getMessage();
        Assertions.assertTrue(x.contains("ID chi nhánh không được bỏ trống"));
    }

//    @Test
//    void testCreateSuccess() throws Exception {
//        BlogDTO dto = initialSuccess();
//        String json = new ObjectMapper().writeValueAsString(dto);
//        Mockito.when(blogTypeService.getOrElseThrow(dto.getTypeId())).thenReturn(initialBlogType());
//        MvcResult result = mvc.perform(post("/blogs")
//                        .content(json)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .header("Authorization", "Bearer " + CommonTest.TOKEN_ADMIN))
//                .andExpect(status().isBadRequest()).andReturn();
//        System.out.println();
//    }
//
//    @Test
//    void testGetByIdSuccess() throws Exception {
//        BlogDTO dto = initialSuccess();
//        Mockito.when(processor.getById("2")).thenReturn(dto);
//        MvcResult result = mvc.perform(get("/blogs/2")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .header("Authorization", "Bearer " + CommonTest.TOKEN_ADMIN))
//                .andExpect(status().isOk()).andReturn();
//        String x = result.getResolvedException().getMessage();
//        Assertions.assertTrue(x.contains("Không tìm thấy blog!!"));
//    }
}
