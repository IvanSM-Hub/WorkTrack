package com.worktrack.services.implementations;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import com.worktrack.dtos.CreateUserRequest;
import com.worktrack.dtos.RequestById;
import com.worktrack.dtos.UserRequest;
import com.worktrack.dtos.UserResponse;
import com.worktrack.dtos.secureAuthApi.SecureAuthLoginRequest;
import com.worktrack.dtos.secureAuthApi.SecureAuthLoginResponse;
import com.worktrack.dtos.secureAuthApi.SecureAuthRegisterResponse;
import com.worktrack.entities.User;
import com.worktrack.exceptions.UserNotFoundException;
import com.worktrack.repositories.UserRepository;
import com.worktrack.services.UserService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UserServiceImpl extends CRUDServiceImpl<User, UUID> implements UserService {

    private static final String REGISTER_PATH = "/api/user/register";
    private static final String LOGIN_PATH = "/api/auth/login";

    @Value("${secureauth.api.base-url:http://localhost:8081}")
    private String secureAuthBaseUrl;

    private final RestClient restClient = RestClient.create(secureAuthBaseUrl);

    private final UserRepository userRepository;


    @Override
    public List<UserResponse> findAll() {
        List<User> users = userRepository.findAll();
        
        if (users.isEmpty()) {
            throw new UserNotFoundException("Users not found.");
        }

        return users.stream().map(
                user -> UserResponse.builder()
                        .email(user.getEmail())
                        .username(user.getUsername())
                        .active(user.isActive())
                        .build())
                .toList();
    }

    
    @Override
    public UserResponse createOne(CreateUserRequest createUserRequest) {

        SecureAuthRegisterResponse registerResponse = restClient.post()
                .uri(REGISTER_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .body(createUserRequest)
                .retrieve()
                .body(SecureAuthRegisterResponse.class);

        SecureAuthLoginRequest loginRequest = new SecureAuthLoginRequest(
                createUserRequest.getUsername(), 
                createUserRequest.getPassword()
            );

        SecureAuthLoginResponse loginResponse = restClient.post()
                .uri(LOGIN_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .body(loginRequest)
                .retrieve()
                .body(SecureAuthLoginResponse.class);

        User user = User.builder()
                .authUserId(UUID.fromString(loginResponse.getId()))
                .username(registerResponse.getUsername())
                .email(registerResponse.getEmail())
                .active(true)
                .build();

        User savedUser = super.create(user);

        return UserResponse.builder()
                .username(savedUser.getUsername())
                .email(savedUser.getEmail())
                .active(savedUser.isActive())
                .build();
    }
    
    @Override
    public boolean deleteOne(RequestById id) {

        

        return false;
    }
    
    @Override
    public UserResponse findOne(RequestById id) {
        User userFound = super.read(id);
        if (userFound == null) {
            throw new UserNotFoundException("User not found.");
        }
        return null;
    }
    
    @Override
    public UserResponse updateOne(UserRequest userRequest) {
        return null;
    }
    
    @Override
    protected JpaRepository<User, UUID> getRepository() {
        return userRepository;
    }

}
