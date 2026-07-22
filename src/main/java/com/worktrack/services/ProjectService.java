package com.worktrack.services;

import java.util.List;

import com.worktrack.dtos.CreateProjectRequest;
import com.worktrack.dtos.ProjectAsignCategoryRequest;
import com.worktrack.dtos.ProjectResponse;
import com.worktrack.dtos.ProjectsFilteredByStatusRequest;
import com.worktrack.dtos.RequestById;
import com.worktrack.dtos.UpdateProjectRequest;

public interface ProjectService {

    List<ProjectResponse> findAll();
    List<ProjectResponse> findAll(ProjectsFilteredByStatusRequest request);
    ProjectResponse findOne(RequestById id);
    ProjectResponse createOne(CreateProjectRequest createProjectRequest);
    ProjectResponse updateOne(UpdateProjectRequest updateProjectRequest);
    ProjectResponse archiveOne(RequestById id);
    ProjectResponse startProject(RequestById id);
    ProjectResponse asignCategory(ProjectAsignCategoryRequest asignCategory);

}
