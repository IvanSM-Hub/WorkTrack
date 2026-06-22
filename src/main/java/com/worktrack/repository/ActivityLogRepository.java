package com.worktrack.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.worktrack.entity.ActivityLog;
import com.worktrack.util.EntityType;

public interface ActivityLogRepository extends JpaRepository<ActivityLog, UUID> {

    List<ActivityLog> findByUserId(UUID userId);
    List<ActivityLog> findByEntityTypeAndEntityId(EntityType entityType, UUID entityId);

}
