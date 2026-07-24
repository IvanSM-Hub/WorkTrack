package com.worktrack.controllers;

import org.springframework.web.bind.annotation.RestController;

import com.worktrack.dtos.CreateProjectCategoryRequest;
import com.worktrack.dtos.ProjectCategoryResponse;
import com.worktrack.dtos.RequestById;
import com.worktrack.dtos.UpdateProjectCategoryRequest;
import com.worktrack.services.ProjectCategoryService;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/categories")
public class ProjectCategoryController {

    private final ProjectCategoryService categoryService;

    @GetMapping
    public ResponseEntity<List<ProjectCategoryResponse>> findAll() {
        return ResponseEntity.ok(categoryService.findAll());
    }
    
    @GetMapping("/find")
    public ResponseEntity<ProjectCategoryResponse> findOne(@RequestBody RequestById id) {
        return ResponseEntity.ok(categoryService.findOne(id));
    }

    @PostMapping("/create")
    public ResponseEntity<ProjectCategoryResponse> createOne(@Valid @RequestBody CreateProjectCategoryRequest request) {
        return ResponseEntity.ok(categoryService.createOne(request));
    }
    
    @PutMapping("/update")
    public ResponseEntity<ProjectCategoryResponse> updateOne(@Valid @RequestBody UpdateProjectCategoryRequest request) {
        return ResponseEntity.ok(categoryService.updateOne(request));
    }

    @PutMapping("/delete")
    public ResponseEntity<ProjectCategoryResponse> deleteOne(@RequestBody RequestById id) {
        return ResponseEntity.ok(categoryService.deleteOne(id));
    }

}
