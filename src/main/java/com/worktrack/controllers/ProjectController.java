package com.worktrack.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.worktrack.dtos.CreateProjectRequest;
import com.worktrack.dtos.ProjectAsignCategoryRequest;
import com.worktrack.dtos.ProjectResponse;
import com.worktrack.dtos.ProjectsFilteredByStatusRequest;
import com.worktrack.dtos.RequestById;
import com.worktrack.dtos.UpdateProjectRequest;
import com.worktrack.services.ProjectService;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/projects")
public class ProjectController {

    private final ProjectService projectService;

    @GetMapping
    public ResponseEntity<List<ProjectResponse>> findAll() {
        return ResponseEntity.ok(projectService.findAll());
    }

    @GetMapping("/filter")
    public ResponseEntity<List<ProjectResponse>> findAll(ProjectsFilteredByStatusRequest request) {
        return ResponseEntity.ok(projectService.findAll(request));
    }
    
    @GetMapping("/find")
    public ResponseEntity<ProjectResponse> findOne(@Valid @RequestBody RequestById id) {
        return ResponseEntity.ok(projectService.findOne(id));
    }

    @PostMapping("/create")
    public ResponseEntity<ProjectResponse> createOne(@Valid @RequestBody CreateProjectRequest request) {
        return ResponseEntity.ok(projectService.createOne(request));
    }
    
    @PutMapping("/update")
    public ResponseEntity<ProjectResponse> updateOne(@Valid @RequestBody UpdateProjectRequest request) {
        return ResponseEntity.ok(projectService.updateOne(request));
    }

    @PutMapping("/start-project")
    public ResponseEntity<ProjectResponse> startProject(@Valid @RequestBody RequestById id) {
        return ResponseEntity.ok(projectService.startProject(id));
    }

    @PutMapping("/archive")
    public ResponseEntity<ProjectResponse> archiveOne(@Valid @RequestBody RequestById id) {
        return ResponseEntity.ok(projectService.archiveOne(id));
    }
    
    @PutMapping("/asign-category")
    public ResponseEntity<ProjectResponse> asignCategory(@Valid @RequestBody ProjectAsignCategoryRequest request) {
        return ResponseEntity.ok(projectService.asignCategory(request));
    }

}
