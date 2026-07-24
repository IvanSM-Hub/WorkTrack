package com.worktrack.services.implementations;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.transaction.annotation.Transactional;

import com.worktrack.dtos.CreateProjectCategoryRequest;
import com.worktrack.dtos.ProjectCategoryEnabledResponse;
import com.worktrack.dtos.ProjectCategoryResponse;
import com.worktrack.dtos.RequestById;
import com.worktrack.dtos.UpdateProjectCategoryRequest;
import com.worktrack.entities.ProjectCategory;
import com.worktrack.exceptions.CategoryAlreadyExistsException;
import com.worktrack.exceptions.CategoryDisabledException;
import com.worktrack.exceptions.CategoryNotFoundException;
import com.worktrack.repositories.ProjectCategoryRepository;

@SpringBootTest
@Transactional
public class ProjectCategoryServiceImplTest {

    @Autowired
    private ProjectCategoryServiceImpl categoryService;

    @Autowired
    private ProjectCategoryRepository categoryRepository;

    private UUID marketingId;

    @BeforeEach
    public void insertData() {
        categoryRepository.deleteAll();
        saveCategory("Engineering", "Tech projects", true);
        marketingId = saveCategory("Marketing", "Marketing projects", true).getId();
        saveCategory("Legacy", "Old, disabled category", false);
    }

    private ProjectCategory saveCategory(String name, String description, boolean enabled) {
        ProjectCategory category = ProjectCategory.builder()
                .name(name)
                .description(description)
                .enabled(enabled)
                .build();
        return categoryRepository.save(category);
    }

    // ---------------------------------------------------------------
    // findAll / findAllEnabled
    // ---------------------------------------------------------------

    @Test
    void findAll_returnsAllSeededCategories() {
        List<ProjectCategoryResponse> result = categoryService.findAll();

        assertThat(result).hasSize(3);
        assertThat(result).extracting(response -> response.getName())
                .containsExactlyInAnyOrder("Engineering", "Marketing", "Legacy");
    }

    @Test
    void findAllEnabled_returnsOnlyEnabledCategories() {
        List<ProjectCategoryEnabledResponse> result = categoryService.findAllEnabled();

        assertThat(result).hasSize(2);
        assertThat(result).extracting(response -> response.getName())
                .containsExactlyInAnyOrder("Engineering", "Marketing");
    }

    // ---------------------------------------------------------------
    // findOne(RequestById) - happy path + errors
    // ---------------------------------------------------------------

    @Test
    void findOne_byId_returnsCategoryById() {
        RequestById id = RequestById.builder()
                .objectId(marketingId)
                .build();

        ProjectCategoryResponse result = categoryService.findOne(id);

        assertNotNull(result);
        assertEquals(marketingId, result.getId());
        assertEquals("Marketing", result.getName());
        assertEquals("Marketing projects", result.getDescription());
        assertTrue(result.isEnabled());
    }

    @Test
    void findOne_byId_notFound_throwsCategoryNotFoundException() {
        RequestById id = RequestById.builder()
                .objectId(UUID.randomUUID())
                .build();

        assertThrows(CategoryNotFoundException.class, () -> categoryService.findOne(id));
    }

    @Test
    void findOne_byId_nullId_throwsInvalidDataAccessApiUsageException() {
        RequestById id = RequestById.builder()
                .objectId(null)
                .build();

        assertThrows(InvalidDataAccessApiUsageException.class, () -> categoryService.findOne(id));
    }

    // ---------------------------------------------------------------
    // findOne(String) - happy path + errors
    // ---------------------------------------------------------------

    @Test
    void findOne_byName_returnsEnabledCategory() {
        ProjectCategory result = categoryService.findOne("Engineering");

        assertNotNull(result);
        assertEquals("Engineering", result.getName());
        assertTrue(result.isEnabled());
    }

    @Test
    void findOne_byName_blank_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> categoryService.findOne(""));
    }

    @Test
    void findOne_byName_notFound_throwsCategoryNotFoundException() {
        assertThrows(CategoryNotFoundException.class, () -> categoryService.findOne("Nonexistent"));
    }

    @Test
    void findOne_byName_disabled_throwsCategoryDisabledException() {
        assertThrows(CategoryDisabledException.class, () -> categoryService.findOne("Legacy"));
    }

    // ---------------------------------------------------------------
    // createOne - happy path + errors
    // ---------------------------------------------------------------

    @Test
    void createOne_success_createsAndPersistsCategory() {
        CreateProjectCategoryRequest request = CreateProjectCategoryRequest.builder()
                .name("Design")
                .description("Design projects")
                .build();

        ProjectCategoryResponse result = categoryService.createOne(request);

        assertNotNull(result.getId());
        assertEquals("Design", result.getName());
        assertEquals("Design projects", result.getDescription());
        assertTrue(categoryRepository.existsByName("Design"));
    }

    @Test
    void createOne_duplicateName_throwsCategoryAlreadyExistsException() {
        CreateProjectCategoryRequest request = CreateProjectCategoryRequest.builder()
                .name("Engineering")
                .description("Duplicate name")
                .build();

        assertThrows(CategoryAlreadyExistsException.class, () -> categoryService.createOne(request));
    }

    // ---------------------------------------------------------------
    // updateOne - happy path + errors
    // ---------------------------------------------------------------

    @Test
    void updateOne_success_updatesNameAndDescription() {
        UpdateProjectCategoryRequest request = UpdateProjectCategoryRequest.builder()
                .id(marketingId)
                .name("Growth Marketing")
                .description("Renamed")
                .build();

        ProjectCategoryResponse result = categoryService.updateOne(request);

        assertEquals("Growth Marketing", result.getName());
        assertEquals("Renamed", result.getDescription());

        ProjectCategory persisted = categoryRepository.findById(marketingId).orElseThrow();
        assertEquals("Growth Marketing", persisted.getName());
    }

    @Test
    void updateOne_notFound_throwsCategoryNotFoundException() {
        UpdateProjectCategoryRequest request = UpdateProjectCategoryRequest.builder()
                .id(UUID.randomUUID())
                .name("Doesn't matter")
                .description("Doesn't matter")
                .build();

        assertThrows(CategoryNotFoundException.class, () -> categoryService.updateOne(request));
    }

    // ---------------------------------------------------------------
    // deleteOne (soft delete) - happy path + errors
    // ---------------------------------------------------------------

    @Test
    void deleteOne_softDeletesBySettingEnabledFalse() {
        RequestById id = RequestById.builder()
                .objectId(marketingId)
                .build();

        ProjectCategoryResponse result = categoryService.deleteOne(id);

        assertFalse(result.isEnabled());

        ProjectCategory persisted = categoryRepository.findById(marketingId).orElseThrow();
        assertFalse(persisted.isEnabled());
        assertThat(categoryService.findAllEnabled()).extracting(response -> response.getName())
                .doesNotContain("Marketing");
    }

    @Test
    void deleteOne_notFound_throwsCategoryNotFoundException() {
        RequestById id = RequestById.builder()
                .objectId(UUID.randomUUID())
                .build();

        assertThrows(CategoryNotFoundException.class, () -> categoryService.deleteOne(id));
    }

    @Test
    void deleteOne_nullId_throwsIllegalArgumentException() {
        RequestById id = RequestById.builder()
                .objectId(null)
                .build();

        assertThrows(IllegalArgumentException.class, () -> categoryService.deleteOne(id));
    }

}
