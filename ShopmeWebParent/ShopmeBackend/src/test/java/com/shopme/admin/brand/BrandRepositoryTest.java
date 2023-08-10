package com.shopme.admin.brand;

import com.shopme.admin.brand.BrandRepository;
import com.shopme.common.entity.Brand;
import com.shopme.common.entity.Category;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.util.List;

import static org.assertj.core.api.Assertions.as;
import static org.assertj.core.api.Assertions.assertThat;
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
public class BrandRepositoryTest {
    @Autowired
    private BrandRepository repo;

    @Test
    public void testCreateBrands() {
        Brand brand_1 = new Brand("Acer");
        brand_1.addCategories(List.of(new Category(4)));
        Brand brand_2 = new Brand("Apple");
        brand_2.addCategories(List.of(new Category(8), new Category(29)));
        Brand brand_3 = new Brand("Samsung");
        brand_3.addCategories(List.of(new Category(6)));

        List<Brand> brands = repo.saveAll(List.of(brand_1, brand_2, brand_3));

        assertThat(brands.size()).isEqualTo(3);
    }

    @Test void changeBrandName() {
        Brand brand = repo.findById(3).get();

        brand.setName("Samsung Electronic");
        brand = repo.save(brand);
        assertThat(brand.getName()).isEqualTo("Samsung Electronic");
    }

    @Test void getBrands() {
        var list = repo.findAll();
        System.out.println(list);

        assertThat(list.size()).isGreaterThan(0);
    }
}
