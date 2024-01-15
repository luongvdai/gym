package fpt.gymmanagement.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import fpt.gymmanagement.config.security.filter.JwtAuthenticationFilter;
import fpt.gymmanagement.dto.GymInformationDTO;
import fpt.gymmanagement.processor.GymInformationProcessor;
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
public class GymInformationControllerTest {
    @Mock
    GymInformationProcessor processor;
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
        processor = Mockito.mock(GymInformationProcessor.class);
        userService = Mockito.mock(UserService.class);
        validator = Validation.buildDefaultValidatorFactory().getValidator();
        jwtAuthenticationFilter = Mockito.mock(JwtAuthenticationFilter.class);
    }

    private GymInformationDTO initialSuccess() {
        return GymInformationDTO.builder()
                .userId("userId1")
                .pbfNormal(1.2)
                .userCourseId("userCourseId")
                .height(1.8)
                .weight(60.0)
                .ecw_tbw(0.5)
                .protein(1.0)
                .pbf(0.3)
                .whr(0.8)
                .bustMeasurement(60.0)
                .waistMeasurement(40.0)
                .hipMeasurement(70.0)
                .note("Khong duoc an nhieu do mo")
                .build();
    }

    private GymInformationDTO initialErrorUserCourseId() {
        return GymInformationDTO.builder()
                .userId("userId1")
                .pbfNormal(1.2)
                .height(1.8)
                .weight(60.0)
                .ecw_tbw(0.5)
                .protein(1.0)
                .pbf(0.3)
                .whr(0.8)
                .bustMeasurement(60.0)
                .waistMeasurement(40.0)
                .hipMeasurement(70.0)
                .note("Khong duoc an nhieu do mo")
                .build();
    }

    private GymInformationDTO initialErrorUserId() {
        return GymInformationDTO.builder()
                .pbfNormal(1.2)
                .userCourseId("userCourseId")
                .height(1.8)
                .weight(60.0)
                .ecw_tbw(0.5)
                .protein(1.0)
                .pbf(0.3)
                .whr(0.8)
                .bustMeasurement(60.0)
                .waistMeasurement(40.0)
                .hipMeasurement(70.0)
                .note("Khong duoc an nhieu do mo")
                .build();
    }

    private GymInformationDTO initialErrorPbfNormal() {
        return GymInformationDTO.builder()
                .userId("userId1")
                .height(1.8)
                .weight(60.0)
                .ecw_tbw(0.5)
                .protein(1.0)
                .pbf(0.3)
                .whr(0.8)
                .bustMeasurement(60.0)
                .waistMeasurement(40.0)
                .hipMeasurement(70.0)
                .note("Khong duoc an nhieu do mo")
                .build();
    }

    private GymInformationDTO initialErrorHeight() {
        return GymInformationDTO.builder()
                .userId("userId1")
                .pbfNormal(1.2)
                .userCourseId("userCourseId")
                .weight(60.0)
                .ecw_tbw(0.5)
                .protein(1.0)
                .pbf(0.3)
                .whr(0.8)
                .bustMeasurement(60.0)
                .waistMeasurement(40.0)
                .hipMeasurement(70.0)
                .note("Khong duoc an nhieu do mo")
                .build();
    }

    private GymInformationDTO initialErrorHeightWithBound() {
        return GymInformationDTO.builder()
                .userId("userId1")
                .pbfNormal(1.2)
                .userCourseId("userCourseId")
                .height(-1.0)
                .weight(60.0)
                .ecw_tbw(0.5)
                .protein(1.0)
                .pbf(0.3)
                .whr(0.8)
                .bustMeasurement(60.0)
                .waistMeasurement(40.0)
                .hipMeasurement(70.0)
                .note("Khong duoc an nhieu do mo")
                .build();
    }

    private GymInformationDTO initialErrorWeight() {
        return GymInformationDTO.builder()
                .userId("userId1")
                .pbfNormal(1.2)
                .userCourseId("userCourseId")
                .height(1.8)
                .ecw_tbw(0.5)
                .protein(1.0)
                .pbf(0.3)
                .whr(0.8)
                .bustMeasurement(60.0)
                .waistMeasurement(40.0)
                .hipMeasurement(70.0)
                .note("Khong duoc an nhieu do mo")
                .build();
    }

    private GymInformationDTO initialErrorWeightWithBound() {
        return GymInformationDTO.builder()
                .userId("userId1")
                .pbfNormal(1.2)
                .userCourseId("userCourseId")
                .height(1.8)
                .weight(-1.0)
                .ecw_tbw(0.5)
                .protein(1.0)
                .pbf(0.3)
                .whr(0.8)
                .bustMeasurement(60.0)
                .waistMeasurement(40.0)
                .hipMeasurement(70.0)
                .note("Khong duoc an nhieu do mo")
                .build();
    }

    private GymInformationDTO initialErrorEcw_tbw() {
        return GymInformationDTO.builder()
                .userId("userId1")
                .pbfNormal(1.2)
                .userCourseId("userCourseId")
                .height(1.8)
                .weight(60.0)
                .protein(1.0)
                .pbf(0.3)
                .whr(0.8)
                .bustMeasurement(60.0)
                .waistMeasurement(40.0)
                .hipMeasurement(70.0)
                .note("Khong duoc an nhieu do mo")
                .build();
    }

    private GymInformationDTO initialErrorProtein() {
        return GymInformationDTO.builder()
                .userId("userId1")
                .pbfNormal(1.2)
                .userCourseId("userCourseId")
                .height(1.8)
                .weight(60.0)
                .ecw_tbw(0.5)
                .pbf(0.3)
                .whr(0.8)
                .bustMeasurement(60.0)
                .waistMeasurement(40.0)
                .hipMeasurement(70.0)
                .note("Khong duoc an nhieu do mo")
                .build();
    }

    private GymInformationDTO initialErrorPdf() {
        return GymInformationDTO.builder()
                .userId("userId1")
                .pbfNormal(1.2)
                .userCourseId("userCourseId")
                .height(1.8)
                .weight(60.0)
                .ecw_tbw(0.5)
                .protein(1.0)
                .whr(0.8)
                .bustMeasurement(60.0)
                .waistMeasurement(40.0)
                .hipMeasurement(70.0)
                .note("Khong duoc an nhieu do mo")
                .build();
    }

    private GymInformationDTO initialErrorWhr() {
        return GymInformationDTO.builder()
                .userId("userId1")
                .pbfNormal(1.2)
                .userCourseId("userCourseId")
                .height(1.8)
                .weight(60.0)
                .ecw_tbw(0.5)
                .protein(1.0)
                .pbf(0.3)
                .bustMeasurement(60.0)
                .waistMeasurement(40.0)
                .hipMeasurement(70.0)
                .note("Khong duoc an nhieu do mo")
                .build();
    }

    private GymInformationDTO initialErrorBustMeasurement() {
        return GymInformationDTO.builder()
                .userId("userId1")
                .pbfNormal(1.2)
                .userCourseId("userCourseId")
                .height(1.8)
                .weight(60.0)
                .ecw_tbw(0.5)
                .protein(1.0)
                .pbf(0.3)
                .whr(0.8)
                .waistMeasurement(40.0)
                .hipMeasurement(70.0)
                .note("Khong duoc an nhieu do mo")
                .build();
    }

    private GymInformationDTO initialErrorWaistMeasurement() {
        return GymInformationDTO.builder()
                .userId("userId1")
                .pbfNormal(1.2)
                .userCourseId("userCourseId")
                .height(1.8)
                .weight(60.0)
                .ecw_tbw(0.5)
                .protein(1.0)
                .pbf(0.3)
                .whr(0.8)
                .bustMeasurement(60.0)
                .hipMeasurement(70.0)
                .note("Khong duoc an nhieu do mo")
                .build();
    }

    private GymInformationDTO initialErrorHipMeasurement() {
        return GymInformationDTO.builder()
                .userId("userId1")
                .pbfNormal(1.2)
                .userCourseId("userCourseId")
                .height(1.8)
                .weight(60.0)
                .ecw_tbw(0.5)
                .protein(1.0)
                .pbf(0.3)
                .whr(0.8)
                .bustMeasurement(60.0)
                .waistMeasurement(40.0)
                .note("Khong duoc an nhieu do mo")
                .build();
    }

    private GymInformationDTO initialErrorNote() {
        return GymInformationDTO.builder()
                .userId("userId1")
                .pbfNormal(1.2)
                .userCourseId("userCourseId")
                .height(1.8)
                .weight(60.0)
                .ecw_tbw(0.5)
                .protein(1.0)
                .pbf(0.3)
                .whr(0.8)
                .bustMeasurement(60.0)
                .waistMeasurement(40.0)
                .hipMeasurement(70.0)
                .build();
    }

    @Test
    void testCreateErrorUserCourseId() throws Exception {
        GymInformationDTO dto = initialErrorUserCourseId();
        String json = new ObjectMapper().writeValueAsString(dto);
        MvcResult result = mvc.perform(post("/gym-information")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + CommonTest.TOKEN_ADMIN))
                .andExpect(status().isBadRequest()).andReturn();
        String x = result.getResolvedException().getMessage();
        Assertions.assertTrue(x.contains("userCourseId không được bỏ trống"));
    }

    @Test
    void testCreateErrorUserId() throws Exception {
        GymInformationDTO dto = initialErrorUserId();
        String json = new ObjectMapper().writeValueAsString(dto);
        MvcResult result = mvc.perform(post("/gym-information")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + CommonTest.TOKEN_ADMIN))
                .andExpect(status().isBadRequest()).andReturn();
        String x = result.getResolvedException().getMessage();
        Assertions.assertTrue(x.contains("UserId không được bỏ trống"));
    }

    @Test
    void testCreateErrorPtId() throws Exception {
        GymInformationDTO dto = initialErrorPbfNormal();
        String json = new ObjectMapper().writeValueAsString(dto);
        MvcResult result = mvc.perform(post("/gym-information")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + CommonTest.TOKEN_ADMIN))
                .andExpect(status().isBadRequest()).andReturn();
        String x = result.getResolvedException().getMessage();
        Assertions.assertTrue(x.contains("pbfNormal không được bỏ trống"));
    }

    @Test
    void testCreateErrorHeight() throws Exception {
        GymInformationDTO dto = initialErrorHeight();
        String json = new ObjectMapper().writeValueAsString(dto);
        MvcResult result = mvc.perform(post("/gym-information")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + CommonTest.TOKEN_ADMIN))
                .andExpect(status().isBadRequest()).andReturn();
        String x = result.getResolvedException().getMessage();
        Assertions.assertTrue(x.contains("Chiều cao không được bỏ trống"));
    }

    @Test
    void testCreateErrorHeightWithBound() throws Exception {
        GymInformationDTO dto = initialErrorHeightWithBound();
        String json = new ObjectMapper().writeValueAsString(dto);
        MvcResult result = mvc.perform(post("/gym-information")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + CommonTest.TOKEN_ADMIN))
                .andExpect(status().isBadRequest()).andReturn();
        String x = result.getResolvedException().getMessage();
        Assertions.assertTrue(x.contains("Không nằm trong khoảng chiều cao quy định"));
    }

    @Test
    void testCreateErrorWeight() throws Exception {
        GymInformationDTO dto = initialErrorWeight();
        String json = new ObjectMapper().writeValueAsString(dto);
        MvcResult result = mvc.perform(post("/gym-information")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + CommonTest.TOKEN_ADMIN))
                .andExpect(status().isBadRequest()).andReturn();
        String x = result.getResolvedException().getMessage();
        Assertions.assertTrue(x.contains("Cân nặng không được bỏ trống"));
    }

    @Test
    void testCreateErrorWeightWithBound() throws Exception {
        GymInformationDTO dto = initialErrorWeightWithBound();
        String json = new ObjectMapper().writeValueAsString(dto);
        MvcResult result = mvc.perform(post("/gym-information")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + CommonTest.TOKEN_ADMIN))
                .andExpect(status().isBadRequest()).andReturn();
        String x = result.getResolvedException().getMessage();
        Assertions.assertTrue(x.contains("Không nằm trong khoảng cân nặng quy định"));
    }

    @Test
    void testCreateErrorEcw_tbw() throws Exception {
        GymInformationDTO dto = initialErrorEcw_tbw();
        String json = new ObjectMapper().writeValueAsString(dto);
        MvcResult result = mvc.perform(post("/gym-information")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + CommonTest.TOKEN_ADMIN))
                .andExpect(status().isBadRequest()).andReturn();
        String x = result.getResolvedException().getMessage();
        Assertions.assertTrue(x.contains("Tỉ lệ nước không được bỏ trống"));
    }

    @Test
    void testCreateErrorProtein() throws Exception {
        GymInformationDTO dto = initialErrorProtein();
        String json = new ObjectMapper().writeValueAsString(dto);
        MvcResult result = mvc.perform(post("/gym-information")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + CommonTest.TOKEN_ADMIN))
                .andExpect(status().isBadRequest()).andReturn();
        String x = result.getResolvedException().getMessage();
        Assertions.assertTrue(x.contains("Protein không được bỏ trống"));
    }

    @Test
    void testCreateErrorPdf() throws Exception {
        GymInformationDTO dto = initialErrorPdf();
        String json = new ObjectMapper().writeValueAsString(dto);
        MvcResult result = mvc.perform(post("/gym-information")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + CommonTest.TOKEN_ADMIN))
                .andExpect(status().isBadRequest()).andReturn();
        String x = result.getResolvedException().getMessage();
        Assertions.assertTrue(x.contains("Pbf không được bỏ trống"));
    }

    @Test
    void testCreateErrorWhr() throws Exception {
        GymInformationDTO dto = initialErrorWhr();
        String json = new ObjectMapper().writeValueAsString(dto);
        MvcResult result = mvc.perform(post("/gym-information")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + CommonTest.TOKEN_ADMIN))
                .andExpect(status().isBadRequest()).andReturn();
        String x = result.getResolvedException().getMessage();
        Assertions.assertTrue(x.contains("Whr không được bỏ trống"));
    }

    @Test
    void testCreateErrorBustMeasurement() throws Exception {
        GymInformationDTO dto = initialErrorBustMeasurement();
        String json = new ObjectMapper().writeValueAsString(dto);
        MvcResult result = mvc.perform(post("/gym-information")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + CommonTest.TOKEN_ADMIN))
                .andExpect(status().isBadRequest()).andReturn();
        String x = result.getResolvedException().getMessage();
        Assertions.assertTrue(x.contains("Vòng một không được bỏ trống"));
    }

    @Test
    void testCreateErrorWaistMeasurement() throws Exception {
        GymInformationDTO dto = initialErrorWaistMeasurement();
        String json = new ObjectMapper().writeValueAsString(dto);
        MvcResult result = mvc.perform(post("/gym-information")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + CommonTest.TOKEN_ADMIN))
                .andExpect(status().isBadRequest()).andReturn();
        String x = result.getResolvedException().getMessage();
        Assertions.assertTrue(x.contains("Vòng hai không được bỏ trống"));
    }

    @Test
    void testCreateErrorHipMeasurement() throws Exception {
        GymInformationDTO dto = initialErrorHipMeasurement();
        String json = new ObjectMapper().writeValueAsString(dto);
        MvcResult result = mvc.perform(post("/gym-information")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + CommonTest.TOKEN_ADMIN))
                .andExpect(status().isBadRequest()).andReturn();
        String x = result.getResolvedException().getMessage();
        Assertions.assertTrue(x.contains("Vòng ba không được bỏ trống"));
    }

    @Test
    void testCreateErrorNote() throws Exception {
        GymInformationDTO dto = initialErrorNote();
        String json = new ObjectMapper().writeValueAsString(dto);
        MvcResult result = mvc.perform(post("/gym-information")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + CommonTest.TOKEN_ADMIN))
                .andExpect(status().isBadRequest()).andReturn();
        String x = result.getResolvedException().getMessage();
        Assertions.assertTrue(x.contains("Note không được bỏ trống"));
    }

}
