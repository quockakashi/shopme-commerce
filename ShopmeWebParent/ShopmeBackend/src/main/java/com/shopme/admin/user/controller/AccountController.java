package com.shopme.admin.user.controller;


import com.shopme.admin.FileUploadUtil;
import com.shopme.admin.security.ShopmeUserDetail;
import com.shopme.admin.user.UserService;
import com.shopme.common.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;

@Controller
public class AccountController {
    private UserService service;

    @Autowired
    public AccountController(UserService service) {
        this.service = service;
    }

    @GetMapping("/account")
    public String viewDetail(@AuthenticationPrincipal ShopmeUserDetail loggedUser, Model model) {
        var user = service.getByEmail(loggedUser.getUsername());

        model.addAttribute("user", user);

        return "users/account-form";

    }

    @PostMapping ("/account/update")
    public String updateAccount(User user, RedirectAttributes redirectAttributes, @RequestParam(name = "image") MultipartFile multipartFile, String photo) throws IOException {
        User savedUser = service.getByEmail(user.getEmail());
        user.setRoles(savedUser.getRoles());
        if(multipartFile != null) {
            String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
            if(!fileName.isEmpty()) {
                user.setPhoto(fileName);
                savedUser = service.save(user);
                FileUploadUtil.saveFile("user-photos/" + user.getId(), fileName, multipartFile);
            } else {
                if(photo != null) {
                    user.setPhoto(photo);
                }
                savedUser = service.save(user);
            }
        }

        redirectAttributes.addFlashAttribute("message", "Your account has been updated successfully.");
        redirectAttributes.addFlashAttribute("isSuccess", true);

        String tailRequest = savedUser == null ? "" : "?id=" + savedUser.getId();
        return "redirect:/account";
    }
}
