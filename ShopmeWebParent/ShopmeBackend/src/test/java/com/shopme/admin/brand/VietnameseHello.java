package com.shopme.admin.brand;

import org.springframework.stereotype.Component;

@Component
public class VietnameseHello implements Hello{
    @Override
    public String print() {
        return getClass().toString();
    }
}
