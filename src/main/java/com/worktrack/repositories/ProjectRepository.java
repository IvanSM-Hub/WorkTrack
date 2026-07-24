package com.worktrack.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.worktrack.entities.Project;
import com.worktrack.util.ProjectStatus;

public interface ProjectRepository extends JpaRepository<Project, UUID> {

    List<Project> findByProjectStatusIn(List<ProjectStatus> statuses);

}
