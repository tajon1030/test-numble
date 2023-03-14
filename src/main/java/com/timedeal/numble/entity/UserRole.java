package com.timedeal.numble.entity;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum UserRole {
    MEMBER("member"),
    ADMIN("admin");

    private final String value;

    @JsonCreator
    public static UserRole from(String value) {
        for (UserRole status : UserRole.values()) {
            if (status.getValue().equals(value)) {
                return status;
            }
        }
        return null;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

}
