package com.worktrack.services.implementations;

import java.util.List;
import java.util.UUID;

import org.hibernate.validator.internal.constraintvalidators.bv.EmailValidator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.worktrack.dtos.CreateUserRequest;
import com.worktrack.dtos.RequestById;
import com.worktrack.dtos.UserRequest;
import com.worktrack.dtos.UserResponse;
import com.worktrack.entities.User;
import com.worktrack.exceptions.UserNotFoundException;
import com.worktrack.repositories.UserRepository;
import com.worktrack.services.UserService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UserServiceImpl extends CRUDServiceImpl<User, UUID> implements UserService {

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

    private final String CREATE_URL_AUTHSECURITY_API = "/register";
    
    @Override
    public UserResponse createOne(CreateUserRequest createUserRequest) {
        
        RestTemplate restTemplate = new RestTemplate();
        Object loginAdmin = restTemplate.postForEntity("/login-admin", createUserRequest, null);
        Object response = restTemplate.postForEntity(CREATE_URL_AUTHSECURITY_API, createUserRequest, Object.class);

        User user = User.builder()
        .email(createUserRequest.())
        .username(createUserRequest.getUsername())
        .active(createUserRequest.isActive())
        .build();      
        
        return null;
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
