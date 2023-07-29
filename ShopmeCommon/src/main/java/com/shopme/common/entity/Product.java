package com.shopme.common.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Data
@Entity
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true, nullable = false, length = 256)
    private String name;

    @Column(unique = true, nullable = false, length = 256)
    private String alias;

    @Column(name = "short_description", length = 512, nullable = false)
    private String shortDescription;

    @Column(name = "full_description", length = 4096, nullable = false)
    private String fullDescription;

    @Column(name = "created_time", nullable = false)
    private Date createdTime;

    @Column(name = "updated_time", nullable = false)
    private Date updatedTime;

    private float cost;
    private float price;

    @Column(name = "discount_percent")
    private float discountPercent;

    private boolean enabled;

    @Column(name = "in_stock")
    private boolean inStock;

    @ManyToOne
    @JoinColumn(name = "brand_id")
    private Brand brand;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    private float length;
    private float width;
    private float height;
    private float weight;
}
