package com.shopme.admin.product;

import com.shopme.common.entity.Product;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.parameters.P;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
public class ProductServiceTest {

    @MockBean
    private ProductRepository repo;

    @InjectMocks
    private ProductService service;

    @Test
    public void checkUniqueProductTest() throws ProductNotFoundException {
        String name = "Samsung Galaxy Z Fold5";
        String alias = "Z Fold5";
        Product product = new Product(1, "Samsung Galaxy Z Fold5", "Z Fold5");
        Mockito.when(repo.findById(2)).thenReturn(Optional.of(new Product(2, "a", "b")));
        Mockito.when(repo.findByName(name)).thenReturn(product);
        Mockito.when(repo.findByAlias(alias)).thenReturn(product);

        assertThat(service.checkUnique(2, name, alias)).isEqualTo("DuplicatedName");
    }
}
