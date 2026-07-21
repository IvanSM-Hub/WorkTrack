package com.worktrack.services;

import java.util.List;

import com.worktrack.dtos.CreateProjectCategoryRequest;
import com.worktrack.dtos.ProjectCategoryResponse;
import com.worktrack.dtos.RequestById;
import com.worktrack.dtos.UpdateProjectCategoryRequest;

public interface ProjectCategoryService {

    List<ProjectCategoryResponse> findAll();
    ProjectCategoryResponse findOne(RequestById id);
    ProjectCategoryResponse createOne(CreateProjectCategoryRequest createProjectCategoryRequest);
    ProjectCategoryResponse updateOne(UpdateProjectCategoryRequest updateProjectCategoryRequest);
    boolean deleteOne(RequestById id);

}
