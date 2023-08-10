package com.shopme.admin.product;

import com.shopme.common.entity.Product;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ProductDTO {
    private Integer id;
    private String name;

    public ProductDTO(Product product) {
        if(product != null) {
            this.id = product.getId();
            this.name = product.getName();
        }
    }
}
