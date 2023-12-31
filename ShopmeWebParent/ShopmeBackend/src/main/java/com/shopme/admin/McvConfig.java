package com.shopme.admin;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;
import java.nio.file.Paths;


@Configuration
public class McvConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String dirNamePhotoUser = "user-photos";
        Path userPhotoDir = Paths.get(dirNamePhotoUser);


        String userPhotosPath = userPhotoDir.toFile().getAbsolutePath();

        registry.addResourceHandler("/" + dirNamePhotoUser + "/**")
                .addResourceLocations("file:/" + userPhotosPath + "/");

        String dirNameCategoriesImages = "category_photos";
        Path categoryPhotosDir = Paths.get(dirNameCategoriesImages);

        String categoryPhotosPath = categoryPhotosDir.toFile().getAbsolutePath();

        registry.addResourceHandler("/category_photos" + "/**")
                .addResourceLocations("file:/" + categoryPhotosPath + "/");

        String brandPhotosDirName = "brand_logos";
        Path brandPhotosDir = Paths.get(brandPhotosDirName);

        String brandPhotosPath = brandPhotosDir.toFile().getAbsolutePath();

        registry.addResourceHandler("/" + brandPhotosDirName + "/**")
                .addResourceLocations("file:/" + brandPhotosPath + "/");

        String productImagesDirName = "../product_images";
        Path productImagesPath = Paths.get(productImagesDirName);

        String path = productImagesPath.toFile().getAbsolutePath();
        registry.addResourceHandler("/product_images/**")
                .addResourceLocations("file:/" + path + "/");
    }
}
