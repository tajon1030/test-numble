package com.timedeal.numble.controller.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.timedeal.numble.controller.error.CustomException;
import com.timedeal.numble.controller.error.ErrorCode;
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

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    UserService userService;

    @Test
    public void 회원가입() throws Exception {
        SignUpRequest request = SignUpRequest.builder()
                .loginId("loginId")
                .userName("userName")
                .password("password")
                .userRole(UserRole.MEMBER)
                .build();

        doNothing().when(userService).join(request);

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request))
                ).andDo(print())
                .andExpect(status().isOk());
    }


    @Test
    public void 회원가입시_이미_회원가입된_loginId로_회원가입을_하는경우_에러반환() throws Exception {
        SignUpRequest request = SignUpRequest.builder()
                .loginId("loginId")
                .userName("userName")
                .password("password")
                .userRole(UserRole.MEMBER)
                .build();

        doThrow(new CustomException(ErrorCode.DUPLICATED_LOGIN_ID))
                .when(userService).join(eq(request));

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(SignUpRequest.builder()
                                .loginId("loginId")
                                .userName("userName2")
                                .password("password2")
                                .userRole(UserRole.MEMBER)
                                .build()))
                ).andDo(print())
                .andExpect(status().isConflict());
    }

    @Test
    public void 회원탈퇴() throws Exception {
        User user = new User("loginId", "userName", UserRole.MEMBER);

        doNothing().when(userService).withdraw(user.getLoginId());

        MockHttpSession mockHttpSession = new MockHttpSession();
        mockHttpSession.setAttribute("loginUser", user);

        mockMvc.perform(delete("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .session(mockHttpSession)
                ).andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void 내정보조회() throws Exception {
        User user = new User("loginId", "userName", UserRole.MEMBER);

        when(userService.findByLoginId(user.getLoginId()))
                .thenReturn(user);

        MockHttpSession mockHttpSession = new MockHttpSession();
        mockHttpSession.setAttribute("loginUser", user);

        mockMvc.perform(get("/api/users/me")
                        .contentType(MediaType.APPLICATION_JSON)
                        .session(mockHttpSession)
                ).andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(user)));
    }

    @Test
    public void 내정보수정() throws Exception {
        UserEntity userEntity = UserEntity.builder()
                .id(1L)
                .loginId("loginId")
                .password("password")
                .userName("userName")
                .userRole(UserRole.MEMBER)
                .build();
        User user = User.fromUserEntity(userEntity);
        UserUpdateRequest updateRequest = new UserUpdateRequest("userName2", "password2");

        UserEntity updateUserEntity = UserEntity.builder()
                .id(userEntity.getId())
                .loginId(userEntity.getLoginId())
                .password(updateRequest.getPassword())
                .userName(updateRequest.getUserName())
                .userRole(userEntity.getUserRole())
                .build();
        when(userService.modifyUser(anyString(), any()))
                .thenReturn(User.fromUserEntity(updateUserEntity));

        MockHttpSession mockHttpSession = new MockHttpSession();
        mockHttpSession.setAttribute("loginUser", user);

        mockMvc.perform(patch("/api/users/me")
                        .contentType(MediaType.APPLICATION_JSON)
                        .session(mockHttpSession)
                        .content(objectMapper.writeValueAsBytes(updateRequest))
                ).andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(User.fromUserEntity(updateUserEntity))));
    }
}