package com.timedeal.numble.controller.security;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SignInRequest {
    @Size(min = 2, max = 25, message = "아이디는 2자 - 25자 이어야합니다.")
    private String loginId;
    @Size(min = 6, max = 50, message = "비밀번호는 6자 - 50자 이어야합니다.")
    private String password;
}
