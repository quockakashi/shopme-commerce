package com.shopme.admin.category;

import com.shopme.common.entity.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {
    public static final int NUMBER_OF_ITEMS_PER_PAGE = 4;
    private CategoryRepository repo;

    @Autowired
    public CategoryService(CategoryRepository repo) {
        this.repo = repo;
    }

    public List<Category> listAll() {
        return repo.findAll();
    }

    public Page<Category> listByPage(int pageNum, String sortField, String sortDir, String keyword) {
        Sort sort = Sort.by(sortField);
        sort = (sortDir.equals("asc") ? sort.ascending() : sort.descending());

        Pageable pageable = PageRequest.of(pageNum - 1, NUMBER_OF_ITEMS_PER_PAGE, sort);

        if(keyword == null)
            return repo.findAll(pageable);
        return repo.findAll(keyword, pageable);
    }

    public Category findCategoryById(Integer id) throws CategoryNotFoundException {
        var category = repo.findById(id).orElse(null);

        if(category == null) {
            throw new CategoryNotFoundException("Not found category with ID " + id);
        }
        return category;
    }

    public Category save(Category category) {
        return repo.save(category);
    }
}
