package com.app.quantitymeasurement.controller;

import com.app.quantitymeasurement.DTO.QuantityDTO;
import com.app.quantitymeasurement.security.JwtFilter;
import com.app.quantitymeasurement.security.CustomUserDetailsService;
import com.app.quantitymeasurement.service.IQuantityMeasurementService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(QuantityMeasurementController.class)
@AutoConfigureMockMvc(addFilters=false)
class QuantityMeasurementControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IQuantityMeasurementService service;
    
    @MockBean
    private JwtFilter jwtFilter;

    @MockBean
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testCompareEndpoint() throws Exception {

        QuantityDTO[] input={
                new QuantityDTO(12.0,"INCH","LENGTH"),
                new QuantityDTO(1.0,"FEET","LENGTH")
        };

        Mockito.when(service.compare(Mockito.any(),Mockito.any()))
                .thenReturn(true);

        mockMvc.perform(post("/api/v1/quantities/compare")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }
}