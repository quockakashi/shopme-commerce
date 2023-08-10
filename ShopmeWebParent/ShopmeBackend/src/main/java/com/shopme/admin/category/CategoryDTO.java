package com.shopme.admin.category;

import com.shopme.common.entity.Category;
import lombok.Data;

@Data
public class CategoryDTO {
    private Integer id;
    private String name;

    public CategoryDTO(Category category) {
        this.id = category.getId();
        this.name = category.getName();
    }
}
