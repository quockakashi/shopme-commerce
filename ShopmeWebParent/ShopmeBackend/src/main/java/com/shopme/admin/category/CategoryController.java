package com.shopme.admin.category;

import com.shopme.admin.FileUploadUtil;
import com.shopme.common.entity.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.method.P;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Controller
public class CategoryController {
    private CategoryService service;

    @Autowired
    public CategoryController(CategoryService service) {
        this.service = service;
    }

    @GetMapping("/categories")
    public String showFirstCategoriesPage() {
        return "redirect:/categories/page/1";
    }


    @GetMapping("/categories/page/{pageNum}")
    public String showCategoryPage(@PathVariable(name = "pageNum") int pageNum, Model model, String sortField, String sortDir, String keyword) {

        if(sortField == null) {
            sortField = "name";
            sortDir = "asc";
        }

        var page = service.listByPage(pageNum, sortField, sortDir, keyword);
        String reversedSort = (sortDir.equals("asc") ? "desc" : "asc");

        model.addAttribute("categories", page.getContent());
        model.addAttribute("numCategoriesInPage", page.getContent().size());
        model.addAttribute("totalPage", page.getTotalPages());
        model.addAttribute("totalCategories", page.getTotalElements());
        model.addAttribute("currentPage", pageNum);
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reversedSort", reversedSort);
        model.addAttribute("keyword", keyword);


        return "/categories/categories";
    }

    @GetMapping("/categories/{id}/enabled/{enabled}")
    public String enableCategory(@PathVariable(name = "id") Integer id, @PathVariable(name = "enabled") boolean enabled,
                                 int pageNum, String sortField, String sortDir,
                                 String keyword, RedirectAttributes redirectAttributes) {

        try{
            var category = service.findCategoryById(id);
            if(category.isEnabled() != enabled) {
                category.setEnabled(enabled);
                String message = "The category ID " + id + " " + category.getName() + " has been";
                if(enabled) {
                    message += " enabled,";
                } else
                    message += " disabled.";
                service.save(category);
                redirectAttributes.addFlashAttribute("message", message);
                redirectAttributes.addFlashAttribute("isSuccess", true);
            }
        } catch (CategoryNotFoundException e) {
            redirectAttributes.addFlashAttribute("message", e.getMessage());
            redirectAttributes.addFlashAttribute("isSuccess", false);
        }

        redirectAttributes.addAttribute("sortField", sortField);
        redirectAttributes.addAttribute("sortDir", sortDir);
        redirectAttributes.addAttribute("keyword", keyword);


        return "redirect:/categories/page/" + pageNum;
    }

    @GetMapping("/categories/new")
    public String showCategoryForm(Model model) {
        Category category = new Category();
        category.setEnabled(true);

        Map<String, Integer> listParents = getListParent();

        model.addAttribute("category", category);
        model.addAttribute("title", "Shopme - Create new category");
        model.addAttribute("listParents", listParents);
        return "categories/category_form";
    }

    private Map<String, Integer> getListParent() {
        List<Category> list = service.findAll();

        Map<String, Integer> listParents = new LinkedHashMap<>();
        for(var item : list) {
            if(item.getParent() == null) { // start from roots
                listParents.put(item.getName(), item.getId());
                travelChildCategory(0, listParents, item);
            }
        }

        return listParents;
    }

    private void travelChildCategory(int level, Map<String, Integer> listParents, Category parent) {
        level = level + 1;
        for(var child : parent.getChildren()) {
            StringBuilder name = new StringBuilder(child.getName());
            for(int i = 0; i < level; i++) {
                name.insert(0, "--");
            }
            listParents.put(name.toString(), child.getId());
            travelChildCategory(level, listParents, child);
        }

    }

    @PostMapping("/categories/save")
    public String saveCategory(Category category, @RequestParam("image_input") MultipartFile file, RedirectAttributes redirectAttributes) throws IOException {
        Category savedCategory;
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        if(!fileName.equals("")) {
            category.setImage(fileName);
            savedCategory = service.save(category);
            FileUploadUtil.saveFile("category_photos/" + savedCategory.getId(), fileName, file);
        } else {
            savedCategory = service.save(category);
        }

        redirectAttributes.addFlashAttribute("message", "The categories " + savedCategory.getName() + " saved successfully");
        redirectAttributes.addFlashAttribute("isSuccess", true);
        redirectAttributes.addAttribute("keyword", savedCategory.getName() + ' ' + savedCategory.getAlias());

        return "redirect:/categories/page/1";
    }

    @GetMapping("/categories/delete/{id}")
    public String deleteCategory(@PathVariable(name = "id") Integer id, int pageNum, String sortField, String sortDir, String keyword, RedirectAttributes redirectAttributes) {
        try {
            var category = service.findCategoryById(id);
            service.delete(category);
            redirectAttributes.addFlashAttribute("message", "Delete category " + category.getName() + " successfully.");
            redirectAttributes.addFlashAttribute("isSuccess", true);
            redirectAttributes.addAttribute("sortField", sortField);
            redirectAttributes.addAttribute("sortDir", sortDir);
            redirectAttributes.addAttribute("keyword", keyword);
        } catch (CategoryNotFoundException e) {
            redirectAttributes.addFlashAttribute("message", e.getMessage());
            redirectAttributes.addFlashAttribute("isSuccess", false);
        }
        return "redirect:/categories/page/" + pageNum;
    }

    @GetMapping("/categories/edit/{id}")
    public String editCategory(@PathVariable(name = "id") Integer id, Model model, RedirectAttributes redirectAttributes) {
        try {
            var category = service.findCategoryById(id);

            Map<String, Integer> listParents = getListParent();
            model.addAttribute("category", category);
            model.addAttribute("listParents", listParents);


        } catch (CategoryNotFoundException e) {
            redirectAttributes.addFlashAttribute("message", e.getMessage());
            redirectAttributes.addFlashAttribute("isSuccess", false);
            return "redirect:/categories";
        }
        return "categories/category_form";
    }
}
