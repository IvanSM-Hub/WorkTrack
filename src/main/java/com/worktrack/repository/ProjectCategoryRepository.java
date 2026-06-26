package com.worktrack.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.worktrack.entity.ProjectCategory;

public interface ProjectCategoryRepository extends JpaRepository<ProjectCategory, UUID> {

    List<ProjectCategory> findByProjectId(UUID projectId);
    boolean existsByProjectIdAndCategoryId(UUID projectId, UUID categoryId);

}
