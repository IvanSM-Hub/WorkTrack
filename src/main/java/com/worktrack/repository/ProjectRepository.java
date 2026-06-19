package com.worktrack.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.worktrack.entity.Project;

public interface ProjectRepository extends JpaRepository<Project, UUID> {

    List<Project> findProjects_byUserId(UUID userId);

}
