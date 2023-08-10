package com.shopme.common.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.awt.*;
import java.util.*;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
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

    @Column(length = 512, nullable = false)
    private String shortDescription;

    @Column(length = 4096, nullable = false)
    private String fullDescription;

    @Column(nullable = false)
    private Date createdTime;

    @Column(nullable = false)
    private Date updatedTime;

    private float cost;
    private float price;

    private float discountPercent;

    private boolean enabled;

    private boolean inStock;

    @ManyToOne
    @JoinColumn(name = "brand_id")
    private Brand brand;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @Column(nullable = true)
    private String mainImage;

    @OneToMany(mappedBy = "product", cascade = {CascadeType.ALL}, orphanRemoval = true)
    @EqualsAndHashCode.Exclude
    private Set<ProductImage> images = new HashSet<>();

    @OneToMany(mappedBy = "product", cascade = {CascadeType.ALL}, orphanRemoval = true)
    @EqualsAndHashCode.Exclude
    private Set<ProductDetail> details = new HashSet<>();

    private float length;
    private float width;
    private float height;
    private float weight;

    public Product(Integer id, String name, String alias) {
        this.id = id;
        this.name = name;
        this.alias = alias;
    }

    public void addExtraImage(ProductImage image) {
        this.images.add(image);
    }

    public void addExtraImages(Collection<ProductImage> images) {
        this.images.addAll(images);
    }

    public void addDetail(ProductDetail detail) {
        this.details.add(detail);
    }

    public boolean containsImage(String imageName) {
        Iterator<ProductImage> it = images.iterator();
        for(; it.hasNext();) {
            var image = it.next();
            if(image.getName().equals(imageName))
                return true;
        }

        return false;
    }

    @Transient
    public String getMainImagePath() {
        if(id == null || mainImage == null || mainImage.equals(""))
            return "/images/default_image.jpg";
        else return "/product_images/" + id + "/" + mainImage;
    }
}
