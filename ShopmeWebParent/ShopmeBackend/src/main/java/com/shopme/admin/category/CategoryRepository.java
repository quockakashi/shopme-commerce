package com.shopme.admin.category;

import com.shopme.common.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {

    @Query("SELECT c " +
            "FROM  Category c " +
            "WHERE CONCAT(c.id, ' ', c.name, ' ', c.alias) LIKE %?1%")
    public Page<Category> findAll(String keyword, Pageable pageable);

    public Category findCategoryByAlias(String alias);

    public Category findCategoryByName(String name);

    @Query("SELECT c " +
            "FROM Category c " +
            "WHERE c.parent = null ")
    public List<Category> findRootCategories(Sort sort);
}
