package com.shopme.admin.category;

import com.shopme.common.entity.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.*;

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

    public List<Category> findAll() {
        return repo.findAll();
    }

    int checkNameAndaAliasUnique(Integer id, String name, String alias) throws CategoryNotFoundException {
        if(id != null) {
            var category = repo.findById(id).get();
            if(category.getName().equals(name) && category.getAlias().equals(alias))
                return 1;
            else if(category.getName().equals(name)) {
                if(repo.findCategoryByAlias(alias) != null)
                    return -1;
                else return 1;
            } else if(category.getName().equals(alias)) {
                if(repo.findCategoryByName(name) != null)
                    return 0;
                else return 1;
            }
        }

        if(repo.findCategoryByName(name) != null)
            return 0;
        else if(repo.findCategoryByAlias(alias) != null)
            return -1;

        return 1;
    }

    public void delete(Category category) {
        repo.delete(category);
    }

    public Map<String, Integer> getListParent() {
        List<Category> list = repo.findRootCategories(Sort.by("name").ascending());

        Map<String, Integer> listParents = new LinkedHashMap<>();
        for(var item : list) {
            if(item.getParent() == null) { // start from roots
                listParents.put(item.getName(), item.getId());
                travelChildCategories(0, listParents, item);
            }
        }

        return listParents;
    }

    private void travelChildCategories(int level, Map<String, Integer> listParents, Category parent) {
        level = level + 1;
        for(var child : sortSubCategories(parent.getChildren())) {
            StringBuilder name = new StringBuilder(child.getName());
            for(int i = 0; i < level; i++) {
                name.insert(0, "--");
            }
            listParents.put(name.toString(), child.getId());
            travelChildCategories(level, listParents, child);
        }

    }

    private SortedSet<Category> sortSubCategories(Set<Category> children) {
        SortedSet<Category> sortedSet = new TreeSet<Category>(new Comparator<Category>() {
            @Override
            public int compare(Category o1, Category o2) {
                return o1.getName().compareTo(o2.getName());
            }
        });

        sortedSet.addAll(children);

        return sortedSet;
    }
}
