package com.shopme.admin.product;

import com.shopme.common.entity.Product;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@AllArgsConstructor
public class ProductService {
    public static int NUMBER_PRODUCTS_PER_PAGE = 4;
    private ProductRepository repo;

    public Page<Product> listByPage(int pageNum, String sortField, String sortDir,
                                    String keyword, Integer categoryId, Integer productId) {

        Sort sort = Sort.by(sortField);
        sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();

        Pageable pageable = PageRequest.of(pageNum - 1, NUMBER_PRODUCTS_PER_PAGE, sort);
        if(productId != null)
            return repo.findById(productId, pageable);

        if(keyword == null && categoryId == 0)
            return repo.findAll(pageable);
        else if(categoryId == 0 && keyword != null)
            return repo.findAll(keyword, pageable);
        else {
            return repo.findAll(keyword, categoryId, pageable);
        }
    }

    public Product save(Product product) {
        if(product.getCreatedTime() == null) {
            product.setCreatedTime(new Date());
        }
        product.setUpdatedTime(new Date());

        if(product.getAlias() == null) {
            product.setAlias(product.getName());
        }
        product.setAlias(product.getAlias().replaceAll(" ", "_"));

        return repo.save(product);
    }

    public Product findById(Integer id) throws ProductNotFoundException {
        Product product = repo.findById(id).orElse(null);
        if(product == null)
            throw new ProductNotFoundException("Not found product with id " + id);
        return product;
    }

    public String checkUnique(Integer id, String name, String alias) throws ProductNotFoundException {
        if(id != null) {
            var existing = findById(id);
            if(existing.getName().equals(name) && existing.getAlias().equals(alias))
                return "OK";
            else if(existing.getName().equals(name)) {
                if(repo.findByAlias(alias) != null)
                    return "DuplicatedAlias";
            } else if(existing.getAlias().equals(alias)) {
                if(repo.findByName(name) != null)
                    return "DuplicatedName";
            }
        }

        if(repo.findByName(name) != null)
            return "DuplicatedName";
        if(repo.findByAlias(alias) != null)
            return "DuplicatedAlias";

        return "OK";
    }

    public ProductDTO setEnabled(Integer id, boolean enabled) {
            try {
                var product = findById(id);
                if(product.isEnabled() != enabled) {
                    product.setEnabled(enabled);
                    product = repo.save(product);
                }
                return new ProductDTO(product);
            } catch (ProductNotFoundException e) {
                return null;
            }
    }

    public void delete(Integer id) throws ProductNotFoundException {
        var product = findById(id);
        repo.delete(product);
    }
}
