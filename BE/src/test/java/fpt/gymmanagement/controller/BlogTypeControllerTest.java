package fpt.gymmanagement.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import fpt.gymmanagement.dto.BlogDTO;
import fpt.gymmanagement.dto.BlogTypeDTO;
import fpt.gymmanagement.processor.BlogProcessor;
import fpt.gymmanagement.processor.BlogTypeProcessor;
import fpt.gymmanagement.service.BlogTypeService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
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
public class BlogTypeControllerTest {
    @Autowired
    MockMvc mvc;
    @Test
    void testCreateSuccess() throws Exception {
        BlogTypeDTO dto = BlogTypeDTO.builder().name("Hoc yoga deo nguoi").build();
        String json = new ObjectMapper().writeValueAsString(dto);
        MvcResult result = mvc.perform(post("/blog-types")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + CommonTest.TOKEN_ADMIN))
                .andExpect(status().isOk()).andReturn();
    }

    @Test
    void testCreateErrorEmpty() throws Exception {
        BlogTypeDTO dto = BlogTypeDTO.builder().name("").build();
        String json = new ObjectMapper().writeValueAsString(dto);
        MvcResult result = mvc.perform(post("/blog-types")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + CommonTest.TOKEN_ADMIN))
                .andExpect(status().isBadRequest()).andReturn();
        String x = result.getResolvedException().getMessage();
        Assertions.assertTrue(x.contains("Tên kiểu blog không được bỏ trống"));
    }

    @Test
    void testCreateErrorNull() throws Exception {
        BlogTypeDTO dto = BlogTypeDTO.builder().build();
        String json = new ObjectMapper().writeValueAsString(dto);
        MvcResult result = mvc.perform(post("/blog-types")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + CommonTest.TOKEN_ADMIN))
                .andExpect(status().isBadRequest()).andReturn();
        String x = result.getResolvedException().getMessage();
        Assertions.assertTrue(x.contains("Tên kiểu blog không được bỏ trống"));
    }

    @Test
    void testCreateErrorLength() throws Exception {
        BlogTypeDTO dto = BlogTypeDTO.builder().name("ab").build();
        String json = new ObjectMapper().writeValueAsString(dto);
        MvcResult result = mvc.perform(post("/blog-types")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + CommonTest.TOKEN_ADMIN))
                .andExpect(status().isBadRequest()).andReturn();
        String x = result.getResolvedException().getMessage();
        Assertions.assertTrue(x.contains("Yêu cầu nhập độ dài phù hợp"));
    }
}
