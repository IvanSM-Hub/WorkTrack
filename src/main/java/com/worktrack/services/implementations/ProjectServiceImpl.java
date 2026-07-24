package com.worktrack.services.implementations;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.worktrack.dtos.CreateProjectRequest;
import com.worktrack.dtos.ProjectAsignCategoryRequest;
import com.worktrack.dtos.ProjectResponse;
import com.worktrack.dtos.ProjectsFilteredByStatusRequest;
import com.worktrack.dtos.RequestById;
import com.worktrack.dtos.UpdateProjectRequest;
import com.worktrack.entities.Project;
import com.worktrack.entities.ProjectCategory;
import com.worktrack.exceptions.ProjectNotFoundException;
import com.worktrack.repositories.ProjectRepository;
import com.worktrack.services.ProjectCategoryService;
import com.worktrack.services.ProjectService;
import com.worktrack.util.ProjectStatus;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProjectServiceImpl extends CRUDServiceImpl<Project, UUID> implements ProjectService {

    private final ProjectRepository projectRepository;
    private final ProjectCategoryService categoryService;

    @Override
    protected JpaRepository<Project, UUID> getRepository() {
        return projectRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProjectResponse> findAll() {
        return projectRepository.findAll().stream()
        .map(this::toResponse)
        .toList();
    }
    
    @Override
    public List<ProjectResponse> findAll(ProjectsFilteredByStatusRequest request) {
        return projectRepository.findByProjectStatusIn(request.getProjectStatus()).stream()
        .map(this::toResponse)
        .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ProjectResponse findOne(RequestById id) {
        if (id.getObjectId() == null) {
            throw new IllegalArgumentException("Id is required");
        }

        return toResponse(findProjectOrThrow(id.getObjectId()));
    }

    @Override
    @Transactional
    public ProjectResponse createOne(CreateProjectRequest createProjectRequest) {

        if (createProjectRequest.getName() == null || createProjectRequest.getName().isBlank()) {
            throw new IllegalArgumentException("The name is required");
        }

        String categoryName = createProjectRequest.getCategoryName();
        if (categoryName == null || categoryName.isBlank()) {
            throw new IllegalArgumentException("The category is required");
        }
        ProjectCategory category = categoryService.findOne(categoryName);

        Project project = Project.builder()
        .name(createProjectRequest.getName())
        .description(createProjectRequest.getDescription())
        .startedAt(null)
        .duration(createProjectRequest.getDuration())
        .projectStatus(ProjectStatus.OPEN)
        .category(category)
        .projectMembers(null)
        .tasks(null)
        .build();

        Project saved = super.create(project);
        log.info("Project '{}' created with id {}", saved.getName(), saved.getId());

        return toResponse(saved);
    }

    @Override
    @Transactional
    public ProjectResponse updateOne(UpdateProjectRequest updateProjectRequest) {
        Project project = findProjectOrThrow(updateProjectRequest.getId());

        if (updateProjectRequest.getName() != null && !updateProjectRequest.getName().isBlank()) {
            project.setName(updateProjectRequest.getName());
        }
        if (updateProjectRequest.getDescription() != null && !updateProjectRequest.getDescription().isBlank()) {
             project.setDescription(updateProjectRequest.getDescription());
        }
        if (updateProjectRequest.getDuration() != null
                && !updateProjectRequest.getDuration().isEqual(project.getDuration())) {
            project.setDuration(updateProjectRequest.getDuration());
        }
        if (updateProjectRequest.getProjectStatus() != null
                && !project.getProjectStatus().equals(updateProjectRequest.getProjectStatus())) {
            project.setProjectStatus(updateProjectRequest.getProjectStatus());
        }
        if (updateProjectRequest.getCategoryName() != null
                && !updateProjectRequest.getCategoryName().isBlank()
                && !project.getCategory().getName().equals(updateProjectRequest.getCategoryName())) {
            ProjectCategory category = categoryService.findOne(updateProjectRequest.getCategoryName());
            project.setCategory(category);
        }

        Project updated = super.update(project);
        log.info("Project {} updated", updated.getId());

        return toResponse(updated);
    }

    @Override
    @Transactional
    public ProjectResponse startProject(RequestById id) {
        Project project = findProjectOrThrow(id.getObjectId());
        project.setStartedAt(LocalDateTime.now());

        Project updated = super.update(project);
        log.info("Project {} started at {}", updated.getId(), updated.getStartedAt());

        return toResponse(updated);
    }

    @Override
    @Transactional
    public ProjectResponse archiveOne(RequestById id) {
        Project project = findProjectOrThrow(id.getObjectId());
        if (ProjectStatus.ARCHIVED.equals(project.getProjectStatus())) {
            throw new IllegalArgumentException("The project is already archived.");
        }
        project.setProjectStatus(ProjectStatus.ARCHIVED);

        Project updated = super.update(project);
        log.info("Project {} archived", updated.getId());

        return toResponse(updated);
    }

    @Override
    public ProjectResponse asignCategory(ProjectAsignCategoryRequest asignCategory) {
        
        if (asignCategory.getProjectId() == null) {
            throw new IllegalArgumentException("Project Id is required.");
        }

        if (asignCategory.getCategoryName() == null) {
            throw new IllegalArgumentException("The category name is required");
        }

        Project project = findProjectOrThrow(asignCategory.getProjectId());

        ProjectCategory category = categoryService.findOne(asignCategory.getCategoryName());

        project.setCategory(category);

        Project updated = super.update(project);
        log.info("Project {} assigned to category '{}'", updated.getId(), category.getName());

        return toResponse(updated);
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

    private Project findProjectOrThrow(UUID id) {
        Project project = super.read(id);
        if (project == null) {
            throw new ProjectNotFoundException("Project not found: " + id);
        }
        return project;
    }

}
