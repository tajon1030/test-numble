package com.timedeal.numble.controller.user;

import com.timedeal.numble.entity.UserRole;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
@EqualsAndHashCode(of = "loginId")
public class SignUpRequest {
    @Size(min = 2, max = 25, message = "아이디는 2자 - 25자 이어야합니다.")
    private String loginId;
    @Size(min = 6, max = 50, message = "비밀번호는 6자 - 50자 이어야합니다.")
    private String password;
    @Size(min = 2, max = 25, message = "사용자 이름은 2자 - 25자 이어야합니다.")
    private String userName;
    @NotNull
    private UserRole userRole;
}
