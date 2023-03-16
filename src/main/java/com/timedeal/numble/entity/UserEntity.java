package com.timedeal.numble.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "User")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // pk

    @Column(updatable = false)
    private String loginId; // 로그인아이디

    private String password; // 비밀번호

    private String userName; // 실명

    @Enumerated(value = EnumType.STRING)
    @Column(updatable = false)
    private UserRole userRole; // 회원, 광고주 구분

    public UserEntity update(String userName, String password) {
        this.userName = userName;
        this.password = password;
        return this;
    }
}
