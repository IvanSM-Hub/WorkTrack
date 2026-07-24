package com.worktrack.entities;

import java.time.LocalDateTime;
import java.util.List;

import com.worktrack.util.ProjectStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = "projects")
public class Project extends BaseEntity {

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = true, length = 500)
    private String description;

    @Column(nullable = true)
    private LocalDateTime startedAt;
    
    @Column(nullable = true)
    private LocalDateTime duration;

    @Column(nullable = false, name = "status")
    @Enumerated(EnumType.STRING)
    private  ProjectStatus projectStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private ProjectCategory category;

    @OneToMany(mappedBy = "project")
    private List<ProjectMember> projectMembers;

    @OneToMany(mappedBy = "project")
    private List<Task> tasks;

}
