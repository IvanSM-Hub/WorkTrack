package com.worktrack.dtos;

import java.util.List;

import com.worktrack.util.ProjectStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProjectsFilteredByStatusRequest {

    private List<ProjectStatus> projectStatus;

}
