package com.shopme.admin.brand;

import com.shopme.common.entity.Brand;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class BrandService {
    public static final int NUMBER_BRANDS_PER_PAGES = 4;

    private BrandRepository repo;

    public Page<Brand> listByPage(int pageNum, String sortField,
                                  String sortDir, String keyword) {

        Sort sort = Sort.by(sortField);
        sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();

        Pageable pageable = PageRequest.of(pageNum - 1, NUMBER_BRANDS_PER_PAGES, sort);

        if(keyword == null) {
            return repo.findAll(pageable);
        } else {
            return repo.findAll(keyword, pageable);
        }
    }

    public Brand findBrandById(Integer id) throws BrandNotFoundException{
        var brand = repo.findById(id).orElse(null);

        if (brand == null)
            throw new BrandNotFoundException("Not found brand with ID " + id);
        return brand;
    }

    public Brand save(Brand brand) {
        return repo.save(brand);
    }
}
