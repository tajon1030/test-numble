package com.timedeal.numble.controller.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.timedeal.numble.controller.error.CustomException;
import com.timedeal.numble.controller.error.ErrorCode;
import com.timedeal.numble.controller.user.User;
import com.timedeal.numble.entity.UserEntity;
import com.timedeal.numble.entity.UserRole;
import com.timedeal.numble.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class SecurityControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    UserService userService;

    @Test
    public void 로그인() throws Exception {
        String loginId = "loginId";
        String password = "password";
        SignInRequest signInRequest = new SignInRequest(loginId, password);

        UserEntity userEntity = UserEntity.builder()
                .loginId(loginId)
                .password(password)
                .userName("userName")
                .userRole(UserRole.MEMBER)
                .build();

        when(userService.login(signInRequest))
                .thenReturn(User.fromUserEntity(userEntity));

        mockMvc.perform(post("/api/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new SignInRequest(loginId, password)))
                ).andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void 로그인시_회원가입_안된_loginId를_입력할경우_에러반환() throws Exception {
        when(userService.login(new SignInRequest("loginId", any())))
                .thenThrow(new CustomException(ErrorCode.USER_NOT_FOUND));

        String loginId = "loginId";
        String password = "password";

        mockMvc.perform(post("/api/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new SignInRequest(loginId, password)))
                ).andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    public void 로그인시_틀린_password를_입력할경우_에러반환() throws Exception {
        when(userService.login(new SignInRequest(any(), "password")))
                .thenThrow(new CustomException(ErrorCode.INVALID_PASSWORD));

        String loginId = "loginId";
        String password = "password";

        mockMvc.perform(post("/api/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new SignInRequest(loginId, password)))
                ).andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void 로그아웃() throws Exception {
        UserEntity userEntity = UserEntity.builder()
                .loginId("loginId")
                .password("password")
                .userName("userName")
                .userRole(UserRole.MEMBER)
                .build();

        MockHttpSession mockHttpSession = new MockHttpSession();
        mockHttpSession.setAttribute("loginUser", userEntity);

        mockMvc.perform(get("/api/logout")
                        .session(mockHttpSession)
                ).andDo(print())
                .andExpect(status().isOk());
    }

}