package com.thangdm.auth_service.Controller;

import com.thangdm.auth_service.dto.request.UserCreationRequest;
import com.thangdm.auth_service.dto.response.UserResponse;
import com.thangdm.auth_service.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.assertj.MockMvcTester;

import java.time.LocalDate;

@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    private UserCreationRequest request;
    private UserResponse userResponse;

    @BeforeEach
    void initialData() {
        request = UserCreationRequest.builder()
                .dob(LocalDate.of(2000,10,5))
                .firstName("Đỗ").lastName("Thắng").username("TEST").password("TEST123123")
                .build();

        userResponse = UserResponse.builder()
                .id("test")
                .dob(LocalDate.of(2000,10,5))
                .firstName("Đỗ").lastName("Thắng").username("TEST").password("TEST123123")
                .build();
    }

    @Test
    void createUser(){
        //GIVEN


        //WHEN
//        mockMvc.perform(MockMvcTester.MockMvcRequestBuilder)
        //THEN
    }
}
