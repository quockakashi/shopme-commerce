package com.shopme.admin.category;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CategoryRestController {
    private CategoryService service;

    public CategoryRestController(CategoryService service) {
        this.service = service;
    }

    @PostMapping("/categories/check_name_alias_unique")
    public String checkNameAndAliasUnique(Integer id, String name, String alias) throws CategoryNotFoundException {
        int check = service.checkNameAndaAliasUnique(id, name, alias);
        if(check == 1)
            return "OK";
        else if(check == 0)
            return "Duplicated Name";
        else
            return "Duplicated Alias";

    }
}
