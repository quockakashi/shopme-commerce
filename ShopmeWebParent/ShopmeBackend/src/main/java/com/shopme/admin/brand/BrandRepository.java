package com.shopme.admin.brand;

import com.shopme.common.entity.Brand;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BrandRepository extends JpaRepository<Brand, Integer> {

    @Query("SELECT b " +
            "FROM  Brand  b " +
            "WHERE CONCAT(b.id, ' ', b.name) LIKE %?1%")
    public Page<Brand> findAll(String keyword, Pageable pageable);
}
