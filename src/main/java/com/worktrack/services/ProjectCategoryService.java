package com.worktrack.services;

import java.util.List;

import com.worktrack.dtos.CreateProjectCategoryRequest;
import com.worktrack.dtos.ProjectCategoryEnabledResponse;
import com.worktrack.dtos.ProjectCategoryResponse;
import com.worktrack.dtos.RequestById;
import com.worktrack.dtos.UpdateProjectCategoryRequest;
import com.worktrack.entities.ProjectCategory;

public interface ProjectCategoryService {

    List<ProjectCategoryResponse> findAll();
    List<ProjectCategoryEnabledResponse> findAllEnabled();
    ProjectCategoryResponse findOne(RequestById id);
    ProjectCategory findOne(String nameCategory);
    ProjectCategoryResponse createOne(CreateProjectCategoryRequest createProjectCategoryRequest);
    ProjectCategoryResponse updateOne(UpdateProjectCategoryRequest updateProjectCategoryRequest);
    ProjectCategoryResponse deleteOne(RequestById id);

}
