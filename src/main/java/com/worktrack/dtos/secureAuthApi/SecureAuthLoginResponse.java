package com.worktrack.dtos.secureAuthApi;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class SecureAuthLoginResponse {

    private String id;
    private String username;
    private String email;
    private String role;
    private String accessToken;
    private String refreshToken;

}
