package com.timedeal.numble.service;

import com.timedeal.numble.controller.error.CustomException;
import com.timedeal.numble.controller.error.ErrorCode;
import com.timedeal.numble.controller.security.SignInRequest;
import com.timedeal.numble.controller.user.SignUpRequest;
import com.timedeal.numble.controller.user.UserUpdateRequest;
import com.timedeal.numble.entity.UserEntity;
import com.timedeal.numble.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

@Service
@RequiredArgsConstructor
@Slf4j
@Validated
public class UserService {

    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public UserEntity findByUserId(Long userId){
        return userRepository.findById(userId)
                .orElseThrow(()->new CustomException(ErrorCode.USER_NOT_FOUND));
    }

    @Transactional
    public void join(@Valid SignUpRequest signUpRequest){
        // 아이디 존재여부 확인
        userRepository.findByLoginId(signUpRequest.getLoginId()).ifPresent(userEntity -> {
            throw new CustomException(ErrorCode.DUPLICATED_LOGIN_ID);
        });

        UserEntity userEntity = UserEntity.builder()
                .loginId(signUpRequest.getLoginId())
                .password(signUpRequest.getPassword())
                .userName(signUpRequest.getUserName())
                .userRole(signUpRequest.getUserRole())
                .build();

        userRepository.save(userEntity);
    }

    @Transactional(readOnly = true)
    public UserEntity login(@Valid SignInRequest signInRequest) {
        // 검색
        return userRepository.findByLoginId(signInRequest.getLoginId())
                .map(userEntity -> {
                    // 패스워드 일치 시
                    if(userEntity.getPassword().equals(signInRequest.getPassword())){
                        return userEntity;
                    }else{
                        throw new CustomException(ErrorCode.INVALID_PASSWORD);
                    }
                })
                .orElseThrow(()->new CustomException(ErrorCode.USER_NOT_FOUND));
    }

    @Transactional
    public void withdraw(Long userId) {
        userRepository.deleteById(userId);
    }

    @Transactional
    public UserEntity modifyUser(Long userId, @Valid UserUpdateRequest request) {
        return userRepository.findById(userId).map(userEntity -> userRepository.save(
                userEntity.update(
                        StringUtils.defaultIfBlank(request.getUserName(),userEntity.getUserName()),
                        StringUtils.defaultIfBlank(request.getPassword(),userEntity.getPassword())
                )
        )).orElseThrow(()->new CustomException(ErrorCode.USER_NOT_FOUND));
    }
}
