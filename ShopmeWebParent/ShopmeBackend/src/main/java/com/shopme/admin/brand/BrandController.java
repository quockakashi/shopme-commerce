package com.shopme.admin.brand;


import com.shopme.admin.FileUploadUtil;
import com.shopme.admin.category.CategoryService;
import com.shopme.common.entity.Brand;
import lombok.AllArgsConstructor;
import org.springframework.boot.Banner;
import org.springframework.data.domain.Page;
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
import java.util.List;

@Controller
@AllArgsConstructor
public class BrandController {
    private BrandService brandService;
    private CategoryService categoryService;


    @GetMapping("/brands")
    public String showFirstPage(RedirectAttributes redirectAttributes) {
        redirectAttributes.addAttribute("sortField", "name");
        redirectAttributes.addAttribute("sortDir", "asc");
        return "redirect:/brands/page/1";
    }

    @GetMapping("/brands/page/{pageNum}")
    public String showPage(@PathVariable("pageNum") int pageNum,
                           String sortField, String sortDir,
                           String keyword, Model model) {
        if(sortField == null) {
            sortField = "name";
            sortDir = "asc";
        }

        String inversionDir = sortDir.equals("asc") ? "desc" : "asc";

        Page<Brand> page = brandService.listByPage(pageNum, sortField, sortDir, keyword);
        List<Brand> brands = page.getContent();

        model.addAttribute("title", "Brands - Shopme");
        model.addAttribute("brands", brands);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalElements", page.getTotalElements());
        model.addAttribute("numBrandsInPage", brands.size());
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("keyword", keyword);
        model.addAttribute("reversedSort", inversionDir);
        model.addAttribute("currentPage", pageNum);

        return "brands/brands";
    }

    @GetMapping("brands/new")
    public String showBrandForm(Model model) {
        Brand brand = new Brand();

        model.addAttribute("brand", brand);
        model.addAttribute("categories", categoryService.getListParent());
        model.addAttribute("title", "Create new brand");
        return "/brands/brand_form";
    }

    @GetMapping("brands/edit/{id}")
    public String showEditPage(@PathVariable("id") Integer id, Model model, RedirectAttributes redirect) {

        try {
            Brand brand = brandService.findBrandById(id);
            model.addAttribute("brand", brand);
            model.addAttribute("categories", categoryService.getListParent());
        } catch (BrandNotFoundException e) {
            redirect.addFlashAttribute("message", e.getMessage());
            redirect.addFlashAttribute("isSuccess", false);
        }
        return "/brands/brand_form";
    }

    @PostMapping("brands/save")
    public String saveBrand(Brand brand, @RequestParam(name = "image_input") MultipartFile file, RedirectAttributes redirect) throws IOException {
        if(file != null) {
            String fileName = StringUtils.cleanPath(file.getOriginalFilename());
            if(!fileName.equals("")) {
                brand.setLogo(fileName);
                brand = brandService.save(brand);
                FileUploadUtil.saveFile("brand_logos/" + brand.getId() + "/", fileName, file);
            } else {
                brand = brandService.save(brand);
            }
        }

        if(brand.getId() != null) {
            redirect.addAttribute("keyword", brand.getId() + " "  + brand.getName());
            redirect.addFlashAttribute("message", brand.getName() + " has been saved successfully.");
            redirect.addFlashAttribute("isSuccess", true);
        } else {
            redirect.addFlashAttribute("message", "Saving " + brand.getName() + "brand has failed.");
            redirect.addFlashAttribute("isSuccess", false);
        }
        return "redirect:/brands/page/1";
    }

}
