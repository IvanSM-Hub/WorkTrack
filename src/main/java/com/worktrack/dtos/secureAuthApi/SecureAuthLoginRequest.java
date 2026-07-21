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
public class SecureAuthLoginRequest {

    private String username;
    private String password;

}
