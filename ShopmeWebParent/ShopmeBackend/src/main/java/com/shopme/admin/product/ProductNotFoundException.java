package com.shopme.admin.product;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class ProductNotFoundException extends Exception{
    public ProductNotFoundException(String message) {
        super(message);
    }
}
