package com.timedeal.numble.controller.security;

import com.timedeal.numble.entity.UserEntity;
import com.timedeal.numble.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api")
public class SecurityController {

    private final UserService userService;

    /**
     * 로그인
     */
    @PostMapping("login")
    public ResponseEntity<?> login(HttpServletRequest request, @RequestBody SignInRequest signInRequest){
        UserEntity loginUserEntity = userService.login(signInRequest);
        // 세션저장
        HttpSession session = request.getSession();
        session.setAttribute("loginUser", loginUserEntity);
        return ResponseEntity.ok().build();
    }

    /**
     * 로그아웃
     * @param request
     * @return
     */
    @GetMapping("logout")
    public ResponseEntity<?> logout(HttpServletRequest request){
        // 세션 삭제
        request.getSession(false).invalidate();
        return ResponseEntity.ok().build();
    }

}
