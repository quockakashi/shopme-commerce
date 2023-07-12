package com.shopme.admin.user;

import com.shopme.common.entity.Role;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.linesOf;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
public class RoleRepositoryTests {

    private RoleRepository repo;

    @Autowired
    public RoleRepositoryTests(RoleRepository repo) {
        this.repo = repo;
    }

    @Test
    public void testCreateFirstRole() {
        Role roleAdmin = new Role("Admin", "manage everything");
        Role savedRole = repo.save(roleAdmin);

        assertThat(savedRole.getId()).isGreaterThan(0);

    }

    @Test
    public void testCreateOthersRole() {
        Role sales = new Role("Salesperson", "manage product price," +
                    "customers, shipping, orders and sales report");
        Role editors = new Role("Editor", "manage categories, brands, products, articles and menus");
        Role shipper = new Role("Shipper", "view product, " +
                    "view orders and update order status");
        Role assistant = new Role("Assistant", "manage question and reviews");

        List<Role> list = repo.saveAll(List.of(sales, editors, shipper, assistant));
        assertThat(list.size()).isSameAs(4);
    }
}


