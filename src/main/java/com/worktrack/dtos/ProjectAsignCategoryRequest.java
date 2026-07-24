package com.worktrack.dtos;

import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProjectAsignCategoryRequest {

    @NotBlank(message = "Project ID is required.")
    private UUID projectId;

    @NotBlank(message = "Category is required.")
    private String categoryName;

}
