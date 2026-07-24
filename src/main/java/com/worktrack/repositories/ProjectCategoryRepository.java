package com.worktrack.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.worktrack.entities.ProjectCategory;

public interface ProjectCategoryRepository extends JpaRepository<ProjectCategory, UUID> {

    
    Optional<ProjectCategory> findByName(String name);
    boolean existsByName(String name);

}
