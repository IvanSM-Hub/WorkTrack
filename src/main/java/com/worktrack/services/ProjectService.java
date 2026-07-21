package com.worktrack.services;

import java.util.List;

import com.worktrack.dtos.CreateProjectRequest;
import com.worktrack.dtos.ProjectResponse;
import com.worktrack.dtos.RequestById;
import com.worktrack.dtos.UpdateProjectRequest;

public interface ProjectService {

    List<ProjectResponse> findAll();
    ProjectResponse findOne(RequestById id);
    ProjectResponse createOne(CreateProjectRequest createProjectCategoryRequest);
    ProjectResponse updateOne(UpdateProjectRequest updateProjectCategoryRequest);
    boolean deleteOne(RequestById id);
    ProjectResponse startProject(RequestById id);

}
