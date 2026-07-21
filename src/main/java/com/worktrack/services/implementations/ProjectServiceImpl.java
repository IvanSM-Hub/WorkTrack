package com.worktrack.services.implementations;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import com.worktrack.dtos.CreateProjectRequest;
import com.worktrack.dtos.ProjectResponse;
import com.worktrack.dtos.RequestById;
import com.worktrack.dtos.UpdateProjectRequest;
import com.worktrack.entities.Project;
import com.worktrack.repositories.ProjectRepository;
import com.worktrack.services.ProjectService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProjectServiceImpl extends CRUDServiceImpl<Project, UUID> implements ProjectService {

    private final ProjectRepository projectRepository;

    @Override
    protected JpaRepository<Project, UUID> getRepository() {
        return projectRepository;
    }

    @Override
    public List<ProjectResponse> findAll() {
        return projectRepository.findAll().stream().map(this::toResponse).toList();
    }
    
    @Override
    public ProjectResponse findOne(RequestById id) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ProjectResponse createOne(CreateProjectRequest createProjectCategoryRequest) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ProjectResponse updateOne(UpdateProjectRequest updateProjectCategoryRequest) {
        // TODO Auto-generated method stub
        return null;
    }
    @Override
    
    public ProjectResponse startProject(RequestById id) {
        // TODO Auto-generated method stub
        return null;
    }
    
    @Override
    public boolean deleteOne(RequestById id) {
        // TODO Auto-generated method stub
        return false;
    }

    private ProjectResponse toResponse(Project project) {
        Map<String, String> members = project.getProjectMembers() == null 
            ? Collections.emptyMap()
            : project.getProjectMembers().stream()
                .collect(Collectors.toMap(
                    pm -> pm.getId().toString(), 
                    pm -> pm.getUser().getUsername()
                ));
                
        Map<String, String> tasks = project.getTasks() == null 
            ? Collections.emptyMap()
            : project.getTasks().stream()
                .collect(Collectors.toMap(
                    task -> task.getId().toString(), 
                    task -> task.getTitle()
                ));

        return ProjectResponse.builder()
        .id(project.getId())
        .name(project.getName())
        .description(project.getDescription())
        .startedAt(project.getStartedAt())
        .duration(project.getDuration())
        .projectStatus(project.getProjectStatus().toString())
        .category(project.getCategory().getName())
        .projectMembers(members)
        .tasks(tasks)
        .build();
    }

}
