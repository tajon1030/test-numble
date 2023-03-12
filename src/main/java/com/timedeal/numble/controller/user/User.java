package com.timedeal.numble.controller.user;

import com.timedeal.numble.entity.UserRole;
import com.timedeal.numble.entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class User {
    private String loginId;
    private String userName;
    private UserRole userRole;

    public static User fromUserEntity(UserEntity userEntity) {
        return new User(userEntity.getLoginId(), userEntity.getUserName(), userEntity.getUserRole());
    }
}
