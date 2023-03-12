package com.timedeal.numble.controller.user;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
@AllArgsConstructor
public class UserUpdateRequest {
    @Nullable
    @Size(min = 2, max = 25, message = "사용자 이름은 2자 - 25자 이어야합니다.")
    private String userName;
    @Nullable
    @Size(min = 6, max = 50, message = "비밀번호는 6자 - 50자 이어야합니다.")
    private String password;
}
