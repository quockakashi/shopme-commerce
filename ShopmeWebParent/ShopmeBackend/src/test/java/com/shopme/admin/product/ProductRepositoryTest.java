package com.shopme.admin.product;

import com.shopme.common.entity.Brand;
import com.shopme.common.entity.Category;
import com.shopme.common.entity.Product;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(false)
@Slf4j
public class ProductRepositoryTest {

    @Autowired
    private ProductRepository repo;

    @Autowired
    private TestEntityManager manager;

    @Test
    public void createNewProduct() {
        Product product = new Product();
        var brand = manager.find(Brand.class, 3);
        var category = manager.find(Category.class, 8);

        product.setName("Insipre");
        product.setAlias("sdfasdfsadfa");

        product.setShortDescription("A good smartphone from Samsung");
        product.setFullDescription("This is a very good smartphone full description");

        product.setBrand(brand);
        product.setCategory(category);

        product.setPrice(1799);
        product.setCreatedTime(new Date());
        product.setUpdatedTime(new Date());

        product = repo.save(product);

        product.setPrice(3000);

        product = manager.find(Product.class, 2);

        System.out.println(product);

        assertThat(product.getId()).isGreaterThan(0);
        assertThat(product.getPrice()).isEqualTo(3000);
    }

    @Test
    public void automaticalSaving() {
        Product product = repo.findById(3).orElse(null);
        log.debug(product.toString());
    }
}
