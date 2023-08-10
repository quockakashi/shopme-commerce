package com.shopme.admin.product;

import com.shopme.admin.FileUploadUtil;
import com.shopme.admin.brand.BrandService;
import com.shopme.admin.category.CategoryService;
import com.shopme.common.entity.Category;
import com.shopme.common.entity.Product;
import com.shopme.common.entity.ProductDetail;
import com.shopme.common.entity.ProductImage;
import lombok.AllArgsConstructor;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
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

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;

@Controller
@AllArgsConstructor
@Slf4j
public class ProductController {
    private ProductService productService;
    private BrandService brandService;
    private CategoryService categoryService;

    @GetMapping("/products")
    public String showFirstPage() {
        return "redirect:/products/page/1";
    }

    @GetMapping("/products/page/{pageNum}")
    String showPage(@PathVariable(name = "pageNum") int pageNum,
                    String sortField, String sortDir, Integer categoryId,
                    String keyword, Model model,
                    @RequestParam(name = "product-id", required = false) Integer productId) {
        if(sortField == null) {
            sortField = "name";
            sortDir = "asc";
        }

        if(categoryId == null) {
            categoryId = 0;
        }

        String reservedSort = (sortDir.equals("asc") ? "desc" : "asc");
        Page<Product> page = productService.listByPage(pageNum, sortField, sortDir, keyword, categoryId, productId);
        var products = page.getContent();

        model.addAttribute("categoryId", categoryId);
        model.addAttribute("categories", categoryService.listCategoriesDTO());
        model.addAttribute("title", "Products");
        model.addAttribute("products", products);
        model.addAttribute("totalElements", page.getTotalElements());
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("numProducts", products.size());
        model.addAttribute("currentPage", pageNum);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("sortField", sortField);
        model.addAttribute("reversedSort", reservedSort);
        model.addAttribute("keyword", keyword);

        return "products/products";

    }

    @GetMapping("products/new")
    public String showProductForm(Model model) {
        Product product = new Product();
        product.setEnabled(true);

        model.addAttribute("title", "New Product");
        model.addAttribute("product", product);
        model.addAttribute("brands", brandService.listSortByName());
        model.addAttribute("numExtraImage", 0);


        return "products/product_form";
    }

    @PostMapping("products/save")
    public String saveProduct(Product product, RedirectAttributes redirectAttributes,
                              @RequestParam("mainImageFile") MultipartFile mainImageFile,
                              @RequestParam("extraImage") MultipartFile[] extraImageFiles,
                              @RequestParam(value = "nameInput",required = false) String[] names,
                              @RequestParam(value = "valueInput", required = false) String[] values,
                              @RequestParam(value = "idDetail", required = false) String[] idDetails,
                              @RequestParam(value = "extraImageId", required = false) String[] imageIds,
                              @RequestParam(value = "extraImageName", required = false) String[] imageNames) throws IOException {
        setProductDetails(product, names, values, idDetails);
        setExistingImage(imageIds, imageNames, product);
        setMainImage(product, mainImageFile);
        setExtraImages(product, extraImageFiles);
        String alias = product.getAlias();
        if(product.getAlias() == null || product.getAlias().equals("")) {
            alias = product.getName();
        }
        product.setAlias(alias.replaceAll(" ", "_"));
        product = productService.save(product);
        saveUploadedImages(product, mainImageFile, extraImageFiles);
        cleanExtraImgDir(product);
        if(product.getId() > 0) {
            redirectAttributes.addFlashAttribute("message", "Product " + product.getName() + " has been saved successfully.");
            redirectAttributes.addFlashAttribute("isSuccess", true);
            redirectAttributes.addAttribute("product-id", product.getId());
        } else {
            redirectAttributes.addFlashAttribute("message", "Product " + product.getName() + " has failed in saving.");
            redirectAttributes.addFlashAttribute("isSuccess", false);
        }


        return "redirect:/products/page/1";


    }

    private void setExistingImage(String[] ids, String[] names, Product product) {
        if(ids == null || names == null || ids.length == 0 || names.length == 0)
            return;
        Set<ProductImage> images = new HashSet<>();
        for(int i = 0; i < ids.length; i++) {
            images.add(new ProductImage(Integer.valueOf(ids[i]), names[i], product));
        }

        product.setImages(images);
    }

    private void cleanExtraImgDir(Product product) throws IOException {
        Path extraDir = Paths.get("../product_images/" + product.getId() + "/extras");
        if(Files.exists(extraDir)) {
            var imgs = Files.list(extraDir);
            imgs.forEach(img -> {
                if(!product.containsImage(img.getFileName().toString())) {
                    try {
                        Files.delete(img);
                    } catch (IOException e) {
                        System.out.println("Can't delete the file");
                    }
                }
            });
        }
     }

    private void setProductDetails(Product product, String[] names, String[] values, String[] idDetails) {
        if(names != null) {
            for(int index = 0; index < names.length; index++) {
                if(names[index] != null && !names[index].equals("")) {
                    if(values[index] != null && !values[index].equals("")) {
                        if(index < idDetails.length) {
                            product.addDetail(new ProductDetail(Integer.valueOf(idDetails[index]), names[index], values[index], product));
                        }
                        else {
                            product.addDetail(new ProductDetail(names[index], values[index], product));
                        }
                    }
                }
            }
        }
    }

    private void saveUploadedImages(Product product,MultipartFile mainImageFile, MultipartFile[] extraImageFiles) throws IOException {
        if(!mainImageFile.isEmpty()) {
            String directoryName = "../product_images/" + product.getId();
            String fileName = StringUtils.cleanPath(mainImageFile.getOriginalFilename());
            FileUploadUtil.cleanPath("../product_images");
            FileUploadUtil.saveFile(directoryName, fileName, mainImageFile);
        }

        if(extraImageFiles.length > 0) {
            String directoryName = "../product_images/" + product.getId() + "/extras";
            for (var file : extraImageFiles) {
                if (!file.isEmpty()) {
                    String fileName = StringUtils.cleanPath(file.getOriginalFilename());
                    FileUploadUtil.cleanPath(fileName);
                    FileUploadUtil.saveFile(directoryName, fileName, file);
                }
            }
        }
    }

    private void setMainImage(Product product, MultipartFile mainImage) {
        if(!mainImage.isEmpty()) {
            product.setMainImage(StringUtils.cleanPath(mainImage.getOriginalFilename()));
        }
    }

    private void setExtraImages(Product product, MultipartFile[] extraImageFiles) {
        for (var extraImage : extraImageFiles) {
            if(!extraImage.isEmpty()) {
                String fileName = StringUtils.cleanPath(extraImage.getOriginalFilename());
                if(!product.containsImage(fileName)) {
                    product.addExtraImage(new ProductImage(fileName, product));
                }
            }
        }
    }

    @GetMapping("/products/{productId}/enabled/{enabled}")
    public String enableProduct(@PathVariable("productId") Integer id,
                              @PathVariable("enabled") boolean enabled,
                              RedirectAttributes redirect) {
        var dto = productService.setEnabled(id,enabled);
        if(dto == null) {
            redirect.addFlashAttribute("message", "Not found the product with ID " + id);
            redirect.addFlashAttribute("isSuccess", false);
        }

        return "redirect:/products";
    }

    @GetMapping("/products/delete/{id}")
    public String deleteProduct(@PathVariable("id") Integer id, String sortField,
                                String sortDir, String keyword,
                                RedirectAttributes redirectAttributes,
                                int pageNum) throws IOException{
        try {
            productService.delete(id);
            redirectAttributes.addFlashAttribute("message", "Product with ID " + id + " has been removed.");
            redirectAttributes.addFlashAttribute("isSuccess", true);
            FileUploadUtil.removeDirectory("../product_images/" + id + "/extras");
            FileUploadUtil.removeDirectory("../product_images/" + id);
        } catch (ProductNotFoundException e) {
            redirectAttributes.addFlashAttribute("message", e.getMessage());
            redirectAttributes.addFlashAttribute("isSuccess", false);
        }

        redirectAttributes.addAttribute("sortField", sortField);
        redirectAttributes.addAttribute("sortDir", sortDir);
        redirectAttributes.addAttribute("keyword", keyword);

        return "redirect:/products/page/" + pageNum;
    }

    @GetMapping("/products/edit/{id}")
    public String editProduct(@PathVariable(name = "id") Integer id,
                              RedirectAttributes redirectAttributes,
                              Model model) {
        try{
            var product = productService.findById(id);
            model.addAttribute("product", product);
            model.addAttribute("brands", brandService.listSortByName());
            model.addAttribute("title", "Edit Product");
            model.addAttribute("numExtraImage", product.getImages().size());
            return "products/product_form";
        } catch (ProductNotFoundException e) {
            redirectAttributes.addFlashAttribute("message", e.getMessage());
            redirectAttributes.addFlashAttribute("isSuccess", false);
            return "redirect:/products";
        }
    }
}
