package com.timedeal.numble.controller.user;

import com.timedeal.numble.entity.UserEntity;
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
     * @return
     */
    @PostMapping
    public ResponseEntity<?> join(@RequestBody SignUpRequest signUpRequest){
        userService.join(signUpRequest);
        return ResponseEntity.ok().build();
    }

    /**
     * 회원탈퇴
     */
    @DeleteMapping
    public ResponseEntity<?> withdraw(
            HttpServletRequest request,
            @SessionAttribute("loginUser") UserEntity userEntity){
        // 회원 탈퇴
        userService.withdraw(userEntity.getId());
        // 로그인 세션 제거
        request.getSession(false).invalidate();
        return ResponseEntity.ok().build();
    }

    /**
     * 내 정보 조회
     * @return
     */
    @GetMapping("me")
    public ResponseEntity<User> me(@SessionAttribute("loginUser") UserEntity userEntity){
        User user = User.fromUserEntity(userService.findByUserId(userEntity.getId()));
        return ResponseEntity.ok(user);
    }

    /**
     * 내 정보 수정
     * @return
     */
    @PatchMapping("me")
    public ResponseEntity<User> modifyMe(@SessionAttribute("loginUser") UserEntity userEntity,
                                         @RequestBody UserUpdateRequest request){
        UserEntity userEntity1 = userService.modifyUser(userEntity.getId(), request);
        User user = User.fromUserEntity(userEntity1);
        return ResponseEntity.ok(user);
    }

}
