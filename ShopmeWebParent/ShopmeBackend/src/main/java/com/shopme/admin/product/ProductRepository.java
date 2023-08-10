package com.shopme.admin.product;

import com.shopme.common.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {

    @Query("SELECT p " +
            "FROM Product p " +
            "WHERE CONCAT(p.id, ' ', p.name, ' ', p.alias, ' ', p.brand.name, ' ', p.category.name) LIKE %?1% " +
            "AND p.category.id = ?2")
    public Page<Product> findAll(String keyword, Integer categoryId, Pageable pageable);

    @Query("SELECT p " +
            "FROM Product p " +
            "WHERE CONCAT(p.id, ' ', p.name, ' ', p.alias, ' ', p.brand.name, ' ', p.category.name) LIKE %?1%")
    public Page<Product> findAll(String keyword, Pageable pageable);
    @Query("SELECT p " +
            "FROM Product p " +
            "WHERE p.id = ?1")
    public Page<Product> findById(Integer id, Pageable pageable);

    public Product findByName(String name);

    public Product findByAlias(String alias);
}
