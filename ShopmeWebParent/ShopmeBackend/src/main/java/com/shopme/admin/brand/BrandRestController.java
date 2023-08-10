package com.shopme.admin.brand;

import com.shopme.common.entity.Category;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Comparator;
import java.util.SortedSet;
import java.util.TreeSet;

@RestController
@AllArgsConstructor
public class BrandRestController {
    private BrandService service;

    @GetMapping("/brands/categories-list")
    private SortedSet<CategoryDTO> getCategoriesByBrand(@RequestParam("brand-id") Integer brandId) throws BrandNotFoundRestException {
        try {
            var categories =  service.listCategoriesByBrandId(brandId);
            var sorted = new TreeSet<CategoryDTO>(new Comparator<CategoryDTO>() {
                @Override
                public int compare(CategoryDTO o1, CategoryDTO o2) {
                    return o1.getName().compareTo(o2.getName());
                }
            });
            for (var category : categories) {
                sorted.add(new CategoryDTO(category));
            }
            return sorted;
        } catch (BrandNotFoundException e) {
            throw new BrandNotFoundRestException();
        }
    }
}
