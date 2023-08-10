package com.shopme.admin.brand;

import org.springframework.stereotype.Component;

@Component
public class EnglishHello implements Hello{
    @Override
    public String print() {
        return getClass().toString();
    }
}
