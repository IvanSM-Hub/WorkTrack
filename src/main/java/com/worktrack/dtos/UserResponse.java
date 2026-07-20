package com.worktrack.dtos;

import com.worktrack.entities.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {

    private String username;

    private String email;

    private boolean active;

    public UserResponse mapper(User user) {
        return UserResponse.builder()
            .email(user.getEmail())
            .username(user.getUsername())
            .active(user.isActive())
            .build();
    }

}
