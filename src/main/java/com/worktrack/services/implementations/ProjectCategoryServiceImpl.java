package com.worktrack.services.implementations;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.worktrack.dtos.CreateProjectCategoryRequest;
import com.worktrack.dtos.ProjectCategoryEnabledResponse;
import com.worktrack.dtos.ProjectCategoryResponse;
import com.worktrack.dtos.RequestById;
import com.worktrack.dtos.UpdateProjectCategoryRequest;
import com.worktrack.entities.ProjectCategory;
import com.worktrack.exceptions.CategoryAlreadyExistsException;
import com.worktrack.exceptions.CategoryDisabledException;
import com.worktrack.exceptions.CategoryNotFoundException;
import com.worktrack.repositories.ProjectCategoryRepository;
import com.worktrack.services.ProjectCategoryService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProjectCategoryServiceImpl extends CRUDServiceImpl<ProjectCategory, UUID> implements ProjectCategoryService {

    private final ProjectCategoryRepository categoryRepository;

    @Override
    protected JpaRepository<ProjectCategory, UUID> getRepository() {
        return categoryRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProjectCategoryResponse> findAll() {
        return categoryRepository.findAll().stream()
               .map(this::toResponse)
               .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProjectCategoryEnabledResponse> findAllEnabled() {
        return categoryRepository.findAll().stream()
               .filter(category -> Boolean.TRUE.equals(category.isEnabled()))
               .map(
                    category -> ProjectCategoryEnabledResponse.builder()
                    .id(category.getId())
                    .name(category.getName())
                    .description(category.getDescription())
                    .build()
               )
               .toList();
    }
    
    @Override
    @Transactional(readOnly = true)
    public ProjectCategoryResponse findOne(RequestById id) {

        ProjectCategory category = super.read(id.getObjectId());

        if (category == null) {
            throw new CategoryNotFoundException("Category not found with id: " 
                + id.getObjectId());
        }

        return toResponse(category);
    }

    @Override
    @Transactional(readOnly = true)
    public ProjectCategory findOne(String nameCategory) {
        if (nameCategory.isEmpty() || nameCategory.isBlank()) {
            throw new IllegalArgumentException("Category name is required");
        }

        ProjectCategory category = categoryRepository.findByName(nameCategory).orElse(null);

        if (category == null) {
            throw new CategoryNotFoundException("Category not found with name: " 
            + nameCategory);
        }

        if (!category.isEnabled()) {
            throw new CategoryDisabledException("Category with the name: "
                + nameCategory + " is disabled."
            );
        }

        return category;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public ProjectCategoryResponse createOne(CreateProjectCategoryRequest createProjectCategoryRequest) {
        if (categoryRepository.existsByName(createProjectCategoryRequest.getName())) {
            throw new CategoryAlreadyExistsException("Category already exists: " 
                + createProjectCategoryRequest.getName());
        }

        ProjectCategory newCategory = ProjectCategory.builder()
        .name(createProjectCategoryRequest.getName())
        .description(createProjectCategoryRequest.getDescription())
        .build();

        ProjectCategory saveCategory = categoryRepository.save(newCategory);

        return toResponse(saveCategory);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public ProjectCategoryResponse deleteOne(RequestById id) {
        if (id.getObjectId() == null) {
            throw new IllegalArgumentException("Id is required");
        }

        ProjectCategory category = super.read(id.getObjectId());

        if (category == null) {
            throw new CategoryNotFoundException("Category not found: " 
                + id.getObjectId());
        }

        category.setEnabled(false);

        return toResponse(super.update(category));
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public ProjectCategoryResponse updateOne(UpdateProjectCategoryRequest updateProjectCategoryRequest) {
        ProjectCategory category = super.read(updateProjectCategoryRequest.getId());

        if (category == null) {
            throw new CategoryNotFoundException("Category not found: " 
                + updateProjectCategoryRequest.getName());
        }

        category.setName(updateProjectCategoryRequest.getName());
        category.setDescription(updateProjectCategoryRequest.getDescription());

        ProjectCategory updatedCategory = super.update(category);

        return toResponse(updatedCategory);
    }

    private ProjectCategoryResponse toResponse(ProjectCategory category) {
        return ProjectCategoryResponse.builder()
        .id(category.getId())
        .name(category.getName())
        .description(category.getDescription())
        .enabled(category.isEnabled())
        .build();
    }

}
