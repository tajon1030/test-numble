package com.timedeal.numble.controller;

import com.timedeal.numble.controller.error.CustomException;
import com.timedeal.numble.controller.error.ErrorCode;
import com.timedeal.numble.controller.user.User;
import com.timedeal.numble.entity.UserRole;

public class Utils {

    /**
     * 관리자 권한 확인
     *
     * @param user 로그인사용자
     */
    public static void checkAdminPermission(User user) {
        if (!user.getUserRole().equals(UserRole.ADMIN)) {
            throw new CustomException(ErrorCode.INVALID_PERMISSION);
        }
    }
}
