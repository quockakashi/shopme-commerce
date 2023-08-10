package com.shopme.admin.brand;

import com.shopme.admin.brand.BrandRepository;
import com.shopme.admin.brand.BrandService;
import com.shopme.admin.category.CategoryNotFoundException;
import com.shopme.admin.category.CategoryRepository;
import com.shopme.admin.category.CategoryService;
import com.shopme.common.entity.Category;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
public class BrandServiceTest {

    @MockBean
    private CategoryRepository repository;

    @InjectMocks
    private CategoryService service;

    @Test
    public void checkUniqueTest() throws CategoryNotFoundException {
        Category category = new Category(4, "Smartphones", "Smartphones");

        Mockito.when(repository.findById(4)).thenReturn(Optional.of(category));

        var res = service.findCategoryById(4);

        assertThat(res.getName()).isEqualTo("Smartphones");
    }
}
