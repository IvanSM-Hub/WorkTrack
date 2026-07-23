package com.worktrack.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.worktrack.dtos.ProjectCategoryEnabledResponse;
import com.worktrack.dtos.ProjectCategoryResponse;
import com.worktrack.dtos.RequestById;
import com.worktrack.entities.ProjectCategory;
import com.worktrack.repositories.ProjectCategoryRepository;
import com.worktrack.services.implementations.ProjectCategoryServiceImpl;

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

    @Test
    void findAllEnabled_returnsOnlyEnabledCategories() {
        List<ProjectCategoryEnabledResponse> result = categoryService.findAllEnabled();
        
        assertThat(result).hasSize(2);
        assertThat(result).extracting(response -> response.getName())
        .containsExactlyInAnyOrder("Engineering", "Marketing");
    }
    
    @Test
    void findAll_returnsAllSeededCategories() {
        List<ProjectCategoryResponse> result = categoryService.findAll();

        assertThat(result).hasSize(3);
        assertThat(result).extracting(response -> response.getName())
        .containsExactlyInAnyOrder("Engineering", "Marketing", "Legacy");
    }
    
    @Test
    void findOne_returnsCategoryById() {
        RequestById id = RequestById.builder()
        .objectId(marketingId)
        .build();

        ProjectCategoryResponse result = categoryService.findOne(id);

        assertNotNull(result);
        assertEquals(marketingId, result.getId());
        assertEquals("Marketing", result.getName());
        assertEquals("Marketing projects", result.getDescription());
        assertEquals(true, result.isEnabled());
    }

}
