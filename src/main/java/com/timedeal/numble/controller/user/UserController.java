package com.timedeal.numble.controller.user;

import com.timedeal.numble.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * 회원가입
     *
     * @return
     */
    @PostMapping
    public ResponseEntity<?> join(@RequestBody SignUpRequest signUpRequest) {
        userService.join(signUpRequest);
        return ResponseEntity.ok().build();
    }

    /**
     * 회원탈퇴
     */
    @DeleteMapping
    public ResponseEntity<?> withdraw(
            HttpServletRequest request,
            @SessionAttribute("loginUser") User user) {
        // 회원 탈퇴
        userService.withdraw(user.getLoginId());
        // 로그인 세션 제거
        request.getSession(false).invalidate();
        return ResponseEntity.ok().build();
    }

    /**
     * 내 정보 조회
     *
     * @return
     */
    @GetMapping("me")
    public ResponseEntity<User> me(@SessionAttribute("loginUser") User user) {
        return ResponseEntity.ok(userService.findByLoginId(user.getLoginId()));
    }

    /**
     * 내 정보 수정
     *
     * @return
     */
    @PatchMapping("me")
    public ResponseEntity<User> modifyMe(@SessionAttribute("loginUser") User user,
                                         @RequestBody UserUpdateRequest request) {
        return ResponseEntity.ok(userService.modifyUser(user.getLoginId(), request));
    }

}
