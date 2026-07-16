package com.worktrack.entities;

import java.util.UUID;


import com.worktrack.util.ActivityAction;
import com.worktrack.util.EntityType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = "activity_logs")
public class ActivityLog extends BaseEntity {

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private EntityType entityType;

    @Column(nullable = false)
    private UUID entityId;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ActivityAction action;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

}
