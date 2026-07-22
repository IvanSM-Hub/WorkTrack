package com.worktrack.services;

import static org.mockito.Mockito.when;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

import com.worktrack.dtos.ProjectCategoryResponse;
import com.worktrack.entities.ProjectCategory;
import com.worktrack.repositories.ProjectCategoryRepository;
import com.worktrack.services.implementations.ProjectCategoryServiceImpl;

@ExtendWith(MockitoExtension.class)
public class ProjectCategoryServiceImplTest {

    @Mock
    private ProjectCategoryRepository categoryRepository;

    @InjectMocks
    private ProjectCategoryServiceImpl categoryService;

    @Test
    void findAll_returnsAllCategoriesMapped() {

        String categoryName = "Engineering"; 
        String categoryDescription = "Tech projects"; 

        ProjectCategory category = ProjectCategory.builder()
                .name(categoryName)
                .description(categoryDescription)
                .enabled(true)
                .build();
        UUID categoryId = UUID.randomUUID();
        ReflectionTestUtils.setField(category, "id", categoryId);

        when(categoryRepository.findAll()).thenReturn(List.of(category));

        List<ProjectCategoryResponse> result = categoryService.findAll();

        assertThat(result).hasSize(1);
        assertInstanceOf(ProjectCategoryResponse.class, result.get(0));
        assertThat(result.get(0).getName()).isEqualTo(categoryName);
        assertThat(result.get(0).getId()).isEqualTo(category.getId());

    }

}
