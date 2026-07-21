package com.worktrack.services.implementations;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import com.worktrack.dtos.CreateProjectCategoryRequest;
import com.worktrack.dtos.ProjectCategoryResponse;
import com.worktrack.dtos.RequestById;
import com.worktrack.dtos.UpdateProjectCategoryRequest;
import com.worktrack.entities.ProjectCategory;
import com.worktrack.exceptions.CategoryAlreadyExistsException;
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
    public List<ProjectCategoryResponse> findAll() {
        return categoryRepository.findAll().stream().map(
            category -> ProjectCategoryResponse.builder()
                .id(category.getId())
                .name(category.getName())
                .description(category.getDescription())
                .build()
        ).toList();
    }

    @Override
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

        return ProjectCategoryResponse.builder()
        .id(saveCategory.getId())
        .name(saveCategory.getName())
        .description(saveCategory.getDescription())
        .build();
    }

    @Override
    public boolean deleteOne(RequestById id) {
        return super.delete(id.getObjectId()) != null;
    }

    @Override
    public ProjectCategoryResponse findOne(RequestById id) {

        ProjectCategory category = super.read(id.getObjectId());

        if (category == null) {
            throw new CategoryNotFoundException("Category not found with id: " 
                + id.getObjectId());
        }

        return ProjectCategoryResponse.builder()
        .id(category.getId())
        .name(category.getName())
        .description(category.getDescription())
        .build();
    }

    @Override
    public ProjectCategoryResponse updateOne(UpdateProjectCategoryRequest updateProjectCategoryRequest) {
        ProjectCategory category = super.read(updateProjectCategoryRequest.getId());

        if (category == null) {
            throw new CategoryNotFoundException("Category not found: " 
                + updateProjectCategoryRequest.getName());
        }

        category.setName(updateProjectCategoryRequest.getName());
        category.setDescription(updateProjectCategoryRequest.getDescription());

        ProjectCategory updatedCategory = super.update(category);

        return ProjectCategoryResponse.builder()
        .id(updatedCategory.getId())
        .name(updatedCategory.getName())
        .description(updatedCategory.getDescription())
        .build();
    }

}
