package com.worktrack.services;

import java.util.List;

import com.worktrack.dtos.CreateUserRequest;
import com.worktrack.dtos.RequestById;
import com.worktrack.dtos.UserRequest;
import com.worktrack.dtos.UserResponse;

public interface UserService {

    List<UserResponse> findAll();
    UserResponse findOne(RequestById id);
    UserResponse createOne(CreateUserRequest createUserRequest);
    UserResponse updateOne(UserRequest userRequest);
    boolean deleteOne(RequestById id);

}
