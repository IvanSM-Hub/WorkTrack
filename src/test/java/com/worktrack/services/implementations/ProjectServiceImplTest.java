package com.worktrack.services.implementations;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.worktrack.dtos.CreateProjectRequest;
import com.worktrack.dtos.ProjectAsignCategoryRequest;
import com.worktrack.dtos.ProjectResponse;
import com.worktrack.dtos.ProjectsFilteredByStatusRequest;
import com.worktrack.dtos.RequestById;
import com.worktrack.dtos.UpdateProjectRequest;
import com.worktrack.entities.Project;
import com.worktrack.entities.ProjectCategory;
import com.worktrack.exceptions.CategoryDisabledException;
import com.worktrack.exceptions.CategoryNotFoundException;
import com.worktrack.exceptions.ProjectNotFoundException;
import com.worktrack.repositories.ProjectCategoryRepository;
import com.worktrack.repositories.ProjectRepository;
import com.worktrack.util.ProjectStatus;

@SpringBootTest
@Transactional
public class ProjectServiceImplTest {

    @Autowired
    private ProjectServiceImpl projectService;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private ProjectCategoryRepository categoryRepository;

    private ProjectCategory engineering;
    private UUID openProjectId;
    private UUID archivedProjectId;

    @BeforeEach
    void insertData() {
        projectRepository.deleteAll();
        categoryRepository.deleteAll();

        engineering = saveCategory("Engineering", "Tech projects", true);
        saveCategory("Legacy", "Old, disabled category", false);

        openProjectId = saveProject("Website Revamp", "Revamp the public website",
                ProjectStatus.OPEN, engineering).getId();
        archivedProjectId = saveProject("Old System", "Legacy internal system",
                ProjectStatus.ARCHIVED, engineering).getId();
    }

    private ProjectCategory saveCategory(String name, String description, boolean enabled) {
        ProjectCategory category = ProjectCategory.builder()
                .name(name)
                .description(description)
                .enabled(enabled)
                .build();
        return categoryRepository.save(category);
    }

    private Project saveProject(String name, String description, ProjectStatus status, ProjectCategory category) {
        Project project = Project.builder()
                .name(name)
                .description(description)
                .projectStatus(status)
                .category(category)
                .build();
        return projectRepository.save(project);
    }

    // ---------------------------------------------------------------
    // findAll / findAll(filtered by status)
    // ---------------------------------------------------------------

    @Test
    void findAll_returnsAllSeededProjects() {
        List<ProjectResponse> result = projectService.findAll();

        assertThat(result).hasSize(2);
        assertThat(result).extracting(response -> response.getName())
                .containsExactlyInAnyOrder("Website Revamp", "Old System");
    }

    @Test
    void findAll_filteredByStatus_returnsOnlyMatchingProjects() {
        ProjectsFilteredByStatusRequest request = ProjectsFilteredByStatusRequest.builder()
                .projectStatus(List.of(ProjectStatus.OPEN))
                .build();

        List<ProjectResponse> result = projectService.findAll(request);

        assertThat(result).hasSize(1);
        assertEquals("Website Revamp", result.get(0).getName());
    }

    // ---------------------------------------------------------------
    // findOne - happy path + errors
    // ---------------------------------------------------------------

    @Test
    void findOne_returnsProjectById() {
        RequestById id = RequestById.builder().objectId(openProjectId).build();

        ProjectResponse result = projectService.findOne(id);

        assertNotNull(result);
        assertEquals("Website Revamp", result.getName());
        assertEquals("OPEN", result.getProjectStatus());
        assertEquals("Engineering", result.getCategory());
    }

    @Test
    void findOne_notFound_throwsProjectNotFoundException() {
        RequestById id = RequestById.builder().objectId(UUID.randomUUID()).build();

        assertThrows(ProjectNotFoundException.class, () -> projectService.findOne(id));
    }

    @Test
    void findOne_nullId_throwsIllegalArgumentException() {
        RequestById id = RequestById.builder().objectId(null).build();

        assertThrows(IllegalArgumentException.class, () -> projectService.findOne(id));
    }

    // ---------------------------------------------------------------
    // createOne - happy path + errors
    // ---------------------------------------------------------------

    @Test
    void createOne_success_createsProjectWithOpenStatusAndNoStartDate() {
        CreateProjectRequest request = CreateProjectRequest.builder()
                .name("Mobile App")
                .description("New mobile app")
                .categoryName("Engineering")
                .build();

        ProjectResponse result = projectService.createOne(request);

        assertNotNull(result.getId());
        assertEquals("Mobile App", result.getName());
        assertEquals("OPEN", result.getProjectStatus());
        assertNull(result.getStartedAt());
        assertEquals("Engineering", result.getCategory());
        assertTrue(projectRepository.existsById(result.getId()));
    }

    @Test
    void createOne_blankName_throwsIllegalArgumentException() {
        CreateProjectRequest request = CreateProjectRequest.builder()
                .name(" ")
                .categoryName("Engineering")
                .build();

        assertThrows(IllegalArgumentException.class, () -> projectService.createOne(request));
    }

    @Test
    void createOne_blankCategory_throwsIllegalArgumentException() {
        CreateProjectRequest request = CreateProjectRequest.builder()
                .name("Mobile App")
                .categoryName(" ")
                .build();

        assertThrows(IllegalArgumentException.class, () -> projectService.createOne(request));
    }

    @Test
    void createOne_categoryNotFound_throwsCategoryNotFoundException() {
        CreateProjectRequest request = CreateProjectRequest.builder()
                .name("Mobile App")
                .categoryName("Nonexistent")
                .build();

        assertThrows(CategoryNotFoundException.class, () -> projectService.createOne(request));
    }

    @Test
    void createOne_categoryDisabled_throwsCategoryDisabledException() {
        CreateProjectRequest request = CreateProjectRequest.builder()
                .name("Mobile App")
                .categoryName("Legacy")
                .build();

        assertThrows(CategoryDisabledException.class, () -> projectService.createOne(request));
    }

    // ---------------------------------------------------------------
    // updateOne - happy path (incl. partial update) + errors
    // ---------------------------------------------------------------

    @Test
    void updateOne_partialUpdate_onlyChangesProvidedFields() {
        UpdateProjectRequest request = UpdateProjectRequest.builder()
                .id(openProjectId)
                .name("Website Revamp v2")
                .build();

        ProjectResponse result = projectService.updateOne(request);

        assertEquals("Website Revamp v2", result.getName());
        // untouched fields must survive the partial update, not get wiped to null
        assertEquals("Revamp the public website", result.getDescription());
        assertEquals("OPEN", result.getProjectStatus());
        assertEquals("Engineering", result.getCategory());
    }

    @Test
    void updateOne_changesCategoryWhenDifferentNameProvided() {
        ProjectCategory design = saveCategory("Design", "Design projects", true);

        UpdateProjectRequest request = UpdateProjectRequest.builder()
                .id(openProjectId)
                .categoryName("Design")
                .build();

        ProjectResponse result = projectService.updateOne(request);

        assertEquals("Design", result.getCategory());
        assertEquals(design.getName(), result.getCategory());
    }

    @Test
    void updateOne_notFound_throwsProjectNotFoundException() {
        UpdateProjectRequest request = UpdateProjectRequest.builder()
                .id(UUID.randomUUID())
                .name("Doesn't matter")
                .build();

        assertThrows(ProjectNotFoundException.class, () -> projectService.updateOne(request));
    }

    // ---------------------------------------------------------------
    // startProject - happy path + errors
    // ---------------------------------------------------------------

    @Test
    void startProject_setsStartedAt() {
        RequestById id = RequestById.builder().objectId(openProjectId).build();

        ProjectResponse result = projectService.startProject(id);

        assertNotNull(result.getStartedAt());
    }

    @Test
    void startProject_notFound_throwsProjectNotFoundException() {
        RequestById id = RequestById.builder().objectId(UUID.randomUUID()).build();

        assertThrows(ProjectNotFoundException.class, () -> projectService.startProject(id));
    }

    // ---------------------------------------------------------------
    // archiveOne - happy path + errors
    // ---------------------------------------------------------------

    @Test
    void archiveOne_setsStatusArchived() {
        RequestById id = RequestById.builder().objectId(openProjectId).build();

        ProjectResponse result = projectService.archiveOne(id);

        assertEquals("ARCHIVED", result.getProjectStatus());
    }

    @Test
    void archiveOne_alreadyArchived_throwsIllegalArgumentException() {
        RequestById id = RequestById.builder().objectId(archivedProjectId).build();

        assertThrows(IllegalArgumentException.class, () -> projectService.archiveOne(id));
    }

    @Test
    void archiveOne_notFound_throwsProjectNotFoundException() {
        RequestById id = RequestById.builder().objectId(UUID.randomUUID()).build();

        assertThrows(ProjectNotFoundException.class, () -> projectService.archiveOne(id));
    }

    // ---------------------------------------------------------------
    // asignCategory - happy path + errors
    // ---------------------------------------------------------------

    @Test
    void asignCategory_success_changesProjectCategory() {
        ProjectCategory design = saveCategory("Design", "Design projects", true);

        ProjectAsignCategoryRequest request = ProjectAsignCategoryRequest.builder()
                .projectId(openProjectId)
                .categoryName("Design")
                .build();

        ProjectResponse result = projectService.asignCategory(request);

        assertEquals(design.getName(), result.getCategory());
    }

    @Test
    void asignCategory_nullProjectId_throwsIllegalArgumentException() {
        ProjectAsignCategoryRequest request = ProjectAsignCategoryRequest.builder()
                .projectId(null)
                .categoryName("Engineering")
                .build();

        assertThrows(IllegalArgumentException.class, () -> projectService.asignCategory(request));
    }

    @Test
    void asignCategory_nullCategoryName_throwsIllegalArgumentException() {
        ProjectAsignCategoryRequest request = ProjectAsignCategoryRequest.builder()
                .projectId(openProjectId)
                .categoryName(null)
                .build();

        assertThrows(IllegalArgumentException.class, () -> projectService.asignCategory(request));
    }

    @Test
    void asignCategory_projectNotFound_throwsProjectNotFoundException() {
        ProjectAsignCategoryRequest request = ProjectAsignCategoryRequest.builder()
                .projectId(UUID.randomUUID())
                .categoryName("Engineering")
                .build();

        assertThrows(ProjectNotFoundException.class, () -> projectService.asignCategory(request));
    }

    @Test
    void asignCategory_categoryNotFound_throwsCategoryNotFoundException() {
        ProjectAsignCategoryRequest request = ProjectAsignCategoryRequest.builder()
                .projectId(openProjectId)
                .categoryName("Nonexistent")
                .build();

        assertThrows(CategoryNotFoundException.class, () -> projectService.asignCategory(request));
    }

}
