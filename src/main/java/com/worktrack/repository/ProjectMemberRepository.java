package com.worktrack.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.worktrack.entity.ProjectMember;

public interface ProjectMemberRepository extends JpaRepository<ProjectMember, UUID> {

    List<ProjectMember> findByProjectId(UUID projectId);
    boolean existsByProjectIdAndUserId(UUID projectId, UUID userId);

}
