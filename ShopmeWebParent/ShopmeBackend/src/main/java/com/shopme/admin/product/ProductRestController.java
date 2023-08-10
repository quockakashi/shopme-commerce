package com.shopme.admin.product;

import com.shopme.common.entity.Product;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class ProductRestController {
    private ProductService service;

    @PostMapping("/products/check-unique")
    public String checkUnique(Integer id, String name, String alias) throws ProductNotFoundException {
        return service.checkUnique(id, name, alias);
    }

    @GetMapping("/products/details/{id}")
    public Product getProductDetails(@PathVariable("id") Integer id) throws ProductNotFoundException {
        return service.findById(id);
    }
}
