package com.shopme.admin.category;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
}
