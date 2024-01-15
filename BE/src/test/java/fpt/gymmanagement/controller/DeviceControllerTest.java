package fpt.gymmanagement.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import fpt.gymmanagement.config.security.filter.JwtAuthenticationFilter;
import fpt.gymmanagement.dto.DeviceDTO;
import fpt.gymmanagement.processor.DeviceProcessor;
import fpt.gymmanagement.service.DeviceTypeService;
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
public class DeviceControllerTest {
    @Mock DeviceProcessor processor;

    @Mock
    UserService userService;

    @Mock
    DeviceTypeService deviceTypeService;
    @Autowired
    Validator validator;
    @Autowired
    MockMvc mvc;
    @Autowired
    JwtAuthenticationFilter jwtAuthenticationFilter;

    @BeforeEach
    void setup() {
        processor = Mockito.mock(DeviceProcessor.class);
        userService = Mockito.mock(UserService.class);
        deviceTypeService = Mockito.mock(DeviceTypeService.class);
        validator = Validation.buildDefaultValidatorFactory().getValidator();
        jwtAuthenticationFilter = Mockito.mock(JwtAuthenticationFilter.class);
    }

    private DeviceDTO initialErrorName() {
        return DeviceDTO.builder()
                .rangeMaintain(3)
                .status(true)
                .typeId("typeId1")
                .price(20000000.0)
                .branchId("branchId1")
                .build();
    }

    private DeviceDTO initialErrorRangeMaintain() {
        return DeviceDTO.builder()
                .name("May tap bung")
                .status(true)
                .typeId("typeId1")
                .price(20000000.0)
                .branchId("branchId1")
                .build();
    }

    private DeviceDTO initialErrorStatus() {
        return DeviceDTO.builder()
                .name("May tap bung")
                .rangeMaintain(3)
                .typeId("typeId1")
                .price(20000000.0)
                .branchId("branchId1")
                .build();
    }

    private DeviceDTO initialErrorTypeId() {
        return DeviceDTO.builder()
                .name("May tap bung")
                .rangeMaintain(3)
                .status(true)
                .price(20000000.0)
                .branchId("branchId1")
                .build();
    }

    private DeviceDTO initialErrorPrice() {
        return DeviceDTO.builder()
                .name("May tap bung")
                .rangeMaintain(3)
                .status(true)
                .typeId("typeId1")
                .branchId("branchId1")
                .build();
    }

    @Test
    void testCreateErrorName() throws Exception {
        DeviceDTO dto = initialErrorName();
        String json = new ObjectMapper().writeValueAsString(dto);
        MvcResult result = mvc.perform(post("/devices")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + CommonTest.TOKEN_ADMIN))
                .andExpect(status().isBadRequest()).andReturn();
        String x = result.getResolvedException().getMessage();
        Assertions.assertTrue(x.contains("Tên thiết bị không được bỏ trống"));
    }

    @Test
    void testCreateErrorBranch() throws Exception {
        DeviceDTO dto = DeviceDTO.builder()
                .name("May tap bung")
                .rangeMaintain(3)
                .status(true)
                .typeId("typeId1")
                .price(20000000.0)
                .build();
        String json = new ObjectMapper().writeValueAsString(dto);
        MvcResult result = mvc.perform(post("/devices")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + CommonTest.TOKEN_ADMIN))
                .andExpect(status().isBadRequest()).andReturn();
        String x = result.getResolvedException().getMessage();
        Assertions.assertTrue(x.contains("Yêu cầu nhập id của chi nhánh"));
    }

    @Test
    void testCreateErrorBranchEmpty() throws Exception {
        DeviceDTO dto = DeviceDTO.builder()
                .name("May tap bung")
                .rangeMaintain(3)
                .status(true)
                .typeId("typeId1")
                .price(20000000.0)
                .build();
        String json = new ObjectMapper().writeValueAsString(dto);
        MvcResult result = mvc.perform(post("/devices")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + CommonTest.TOKEN_ADMIN))
                .andExpect(status().isBadRequest()).andReturn();
        String x = result.getResolvedException().getMessage();
        Assertions.assertTrue(x.contains("Yêu cầu nhập id của chi nhánh"));
    }

    @Test
    void testCreateErrorRangeMaintain() throws Exception {
        DeviceDTO dto = initialErrorRangeMaintain();
        String json = new ObjectMapper().writeValueAsString(dto);
        MvcResult result = mvc.perform(post("/devices")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + CommonTest.TOKEN_ADMIN))
                .andExpect(status().isBadRequest()).andReturn();
        String x = result.getResolvedException().getMessage();
        Assertions.assertTrue(x.contains("Khoảng thời gian để sửa thiết bị không được bỏ trống"));
    }

    @Test
    void testCreateErrorStatus() throws Exception {
        DeviceDTO dto = initialErrorStatus();
        String json = new ObjectMapper().writeValueAsString(dto);
        MvcResult result = mvc.perform(post("/devices")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + CommonTest.TOKEN_ADMIN))
                .andExpect(status().isBadRequest()).andReturn();
        String x = result.getResolvedException().getMessage();
        Assertions.assertTrue(x.contains("Trạng thái thiết bị không được bỏ trống"));
    }

    @Test
    void testCreateErrorTypeId() throws Exception {
        DeviceDTO dto = initialErrorTypeId();
        String json = new ObjectMapper().writeValueAsString(dto);
        MvcResult result = mvc.perform(post("/devices")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + CommonTest.TOKEN_ADMIN))
                .andExpect(status().isBadRequest()).andReturn();
        String x = result.getResolvedException().getMessage();
        Assertions.assertTrue(x.contains("Id kiểu thiết bị không được bỏ trống"));
    }

    @Test
    void testCreateErrortypePrice() throws Exception {
        DeviceDTO dto = initialErrorPrice();
        String json = new ObjectMapper().writeValueAsString(dto);
        MvcResult result = mvc.perform(post("/devices")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + CommonTest.TOKEN_ADMIN))
                .andExpect(status().isBadRequest()).andReturn();
        String x = result.getResolvedException().getMessage();
        Assertions.assertTrue(x.contains("Giá tiền thiết bị không được bỏ trống"));
    }
}
