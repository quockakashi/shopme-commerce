package com.shopme.admin.user;

import com.shopme.common.entity.Role;
import com.shopme.common.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Rollback;

import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
public class UserRepositoryTest {
    private UserRepository repo;
    private TestEntityManager entityManager;

    @Autowired
    public UserRepositoryTest(UserRepository repo, TestEntityManager entityManager) {
        this.repo = repo;
        this.entityManager = entityManager;
    }

    @Test
    public void testCreateFirstUser() {
        User user = new User("quocng777@gmail.com", "helloworld", "Quoc", "Nguyen");

        User temp = repo.save(user);

        assertThat(temp.getId()).isGreaterThan(0);
    }

    @Test
    public void updateUser() {
        var user = repo.findById(1).get();

        var adminRole = new Role();
        adminRole.setId(1);
        user.addRole(adminRole);

        var temp = repo.save(user);
        assertThat(temp.getId()).isSameAs(1);
    }

    @Test
    public void createUserWithTwoRoles() {
        var user = new User("trandinh12@outlook.com", "helloworld", "Dinh", "Tran");
        user.addRole(new Role(3));
        user.addRole(new Role(5));
        var temp = repo.save(user);
        assertThat(temp.getId()).isSameAs(2);
    }

    @Test
    public void getUserByEmail() {
        String email = "quocng777@gmail.com";
        var user = repo.findUserByEmail(email);

        System.out.println(user);
        assertThat(user).isNotNull();
    }

    @Test
    public void disableUser() {
        repo.updateEnabledStatus(15, false);

        var user = repo.findById(15).orElse(null);

        assertThat(user.getEnabled()).isFalse();
    }

    @Test
    public void testGetUserPhotoPath() {
        var user = repo.findById(15).get();

        System.out.println(user.getPhotosImagePath());
    }

    @Test
    public void testListFirstPage() {
        int pageNumber = 0; // this the first page
        int pageSize = 4; // 4 elements per page
        Pageable pageable = PageRequest.of(pageNumber, pageSize);

        Page<User> page = repo.findAll(pageable);
        var listUsers = page.getContent();
        System.out.println(listUsers);
        assertThat(listUsers.size() == page.getSize()).isTrue();

    }


    @Test
    public void testFindUserWithSearching() {
        String keyword = "Pham";
        var page = repo.findAll(keyword, PageRequest.of(0, 4, Sort.by("id").ascending()));
        var users = page.getContent();
        System.out.println(users);
        assertThat(users.size()).isGreaterThan(0);
    }

}


