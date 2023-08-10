package com.shopme.common.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "brands")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Brand {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 45)
    private String name;

    @Column(length = 45)
    private String logo;

    @ManyToMany
    @JoinTable(name = "brands_categories",
    joinColumns = @JoinColumn(name = "brand_id"),
    inverseJoinColumns = @JoinColumn(name = "category_id"))
    @EqualsAndHashCode.Exclude
    private Set<Category> categories = new HashSet<>();

    public Brand(String name) {
        this.name = name;
    }

    @Transient
    public String getImagePath() {
        if(id == null || logo == null || logo.equals(""))
            return "/images/default_image.jpg";
        else {
            return "/brand_logos/" + id + "/" + logo;
        }
    }
    public void addCategories(List<Category> categories) {
        this.categories.addAll(categories);
    }
    @Override
    public String toString() {
        return "id: " + id + ", name: " + name;
    }
}
