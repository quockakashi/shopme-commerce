package com.shopme.admin.brand;


import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Slf4j
public class HelloService {

    @Autowired
    private Hello englishHello;


    @Test
    public void testMethod() {
        log.warn(englishHello.print());
    }
}
