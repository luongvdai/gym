package fpt.gymmanagement.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import fpt.gymmanagement.dto.request.order.ApproveOrderRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ManageOrderControllerTest {
    @Autowired
    MockMvc mvc;

    @Test
    void testEmptyOrderId() throws Exception {
        ApproveOrderRequest request = new ApproveOrderRequest();
        request.setOrderId("");
        request.setIsConfirm(true);
        String json = new ObjectMapper().writeValueAsString(request);
        MvcResult result = mvc.perform(put("/manage/orders/action")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + CommonTest.TOKEN_ADMIN))
                .andExpect(status().isBadRequest()).andReturn();
        String x = result.getResolvedException().getMessage();
        Assertions.assertTrue(x.contains("ID của order không được bỏ trống"));
    }

    @Test
    void testNullOrderId() throws Exception {
        ApproveOrderRequest request = new ApproveOrderRequest();
        request.setOrderId("");
        request.setIsConfirm(true);
        String json = new ObjectMapper().writeValueAsString(request);
        MvcResult result = mvc.perform(put("/manage/orders/action")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + CommonTest.TOKEN_ADMIN))
                .andExpect(status().isBadRequest()).andReturn();
        String x = result.getResolvedException().getMessage();
        Assertions.assertTrue(x.contains("ID của order không được bỏ trống"));
    }

    @Test
    void testNullIsConfirm() throws Exception {
        ApproveOrderRequest request = new ApproveOrderRequest();
        request.setOrderId("id9823kj");
        String json = new ObjectMapper().writeValueAsString(request);
        MvcResult result = mvc.perform(put("/manage/orders/action")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + CommonTest.TOKEN_ADMIN))
                .andExpect(status().isBadRequest()).andReturn();
        String x = result.getResolvedException().getMessage();
        Assertions.assertTrue(x.contains("Yêu cầu phê duyệt không được bỏ trống"));
    }

    @Test
    void testUnauthorized() throws Exception {
        ApproveOrderRequest request = new ApproveOrderRequest();
        request.setOrderId("id9823kj");
        String json = new ObjectMapper().writeValueAsString(request);
        MvcResult result = mvc.perform(put("/manage/orders/action")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized()).andReturn();
    }
}
