package com.worktrack.dtos;

import java.time.LocalDateTime;
import java.util.UUID;

import com.worktrack.util.ProjectStatus;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateProjectRequest {

    @NotNull(message = "Id is required")
    private UUID id;

    @NotBlank(message = "Name is required")
    @Size(max = 100, message = "Name cannot exceed 100 characters")
    private String name;

    @Size(max = 255, message = "Description cannot exceed 255 characters")
    private String description;

    private LocalDateTime duration;

    @NotNull(message = "Status is required")
    private ProjectStatus projectStatus;

    @NotNull(message = "Category id is required")
    private UUID categoryId;

}
