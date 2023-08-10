package com.shopme.admin.brand;

import com.shopme.common.entity.Category;
import lombok.Data;

@Data
public class CategoryDTO {
    private Integer id;
    private String name;

    public CategoryDTO(Category category) {
        if(category != null) {
            this.id = category.getId();
            this.name = category.getName();
        }
    }
}
