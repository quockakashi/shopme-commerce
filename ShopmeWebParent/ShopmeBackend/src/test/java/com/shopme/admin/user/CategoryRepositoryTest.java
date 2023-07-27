package com.shopme.admin.user;

import com.mysql.cj.log.LogFactory;
import com.shopme.admin.category.CategoryRepository;
import com.shopme.common.entity.Category;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
public class CategoryRepositoryTest {

    @Autowired
    private CategoryRepository repo;

    @Test
    public void creatRootCategories() {
        Category category = new Category("Electronic");

        var savedItem = repo.save(category);

        assertThat(savedItem).isNotNull();
    }

    @Test
    public void createSubCategories() {
        Category computer = new Category(1);
        Category electronic = new Category(2);
        Category desktops = new Category("Desktops", computer);
        var laptops = new Category("Laptops", computer);
        var components = new Category("Computer components", computer);
        var memory = new Category("Memory", components);
        var cameras = new Category("Cameras", electronic);
        var smartphones = new Category("Smartphones", electronic);

        var list = repo.saveAll(List.of(desktops, laptops, components, memory, cameras, smartphones));
        assertThat(list.size()).isEqualTo(6);
    }

    @Test
    public void getChildCategories() {
        var category = repo.findById(1).get();
        System.out.println(category);
        System.out.println(category.getChildren());

    }
}
