package com.worktrack.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.worktrack.entity.Task;
import com.worktrack.util.TaskStatus;

public interface TaskRepository extends JpaRepository<Task, UUID> {

    List<Task> findByProjectId(UUID projectId);
    List<Task> findByAssignedUserId(UUID userId);
    List<Task> findByStatus(TaskStatus status);
    List<Task> findByProjectIdAndStatus(UUID projectId, TaskStatus status);

}
