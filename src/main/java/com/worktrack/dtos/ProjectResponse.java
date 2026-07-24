package com.worktrack.dtos;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProjectResponse {

    private UUID id;
    private String name;
    private String description;
    private LocalDateTime startedAt;
    private LocalDateTime duration;
    private String projectStatus;
    private String category;
    private Map<String, String> projectMembers;
    private Map<String, String> tasks;


}
