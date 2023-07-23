package com.shopme.admin.user.controller;

import com.shopme.admin.FileUploadUtil;
import com.shopme.admin.export.UserCsvExporter;
import com.shopme.admin.export.UserExcelExporter;
import com.shopme.admin.export.UserExporterAbstract;
import com.shopme.admin.export.UserPdfExporter;
import com.shopme.admin.user.UserNotFoundException;
import com.shopme.admin.user.UserService;
import com.shopme.common.entity.User;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
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
public class UserController {

    private UserService service;

    @Autowired
    public UserController(UserService service) {
        this.service = service;
    }

    @GetMapping("/users")
    public String listFirstPage(Model model, Integer id){
        return listByPage(1, model, "firstName", "asc", null, id);
    }

    @GetMapping("/users/page/{pageNum}")
    public String listByPage(@PathVariable(name = "pageNum") int pageNum, Model model, String sortField, String sortDir, String keyword, Integer id)  {
        if(id == null) {
            var page = service.listByPage(pageNum, sortField, sortDir, keyword);
            int startCount = (pageNum - 1) * UserService.NUM_USERS_PER_PAGE + 1;
            int endCount = startCount + UserService.NUM_USERS_PER_PAGE - 1;
            long totalElement = page.getTotalElements();
            if(endCount > totalElement) {
                endCount = (int) totalElement;
            }

            String reversedSort = sortDir.equals("asc") ? "desc" : "asc";

            model.addAttribute("users", page.getContent());
            model.addAttribute("totalElement", totalElement);
            model.addAttribute("totalPage", Math.ceil(totalElement * 1.0 / UserService.NUM_USERS_PER_PAGE));
            model.addAttribute("currentPage", pageNum);
            model.addAttribute("startCount", startCount);
            model.addAttribute("endCount", endCount);
            model.addAttribute("sortField", sortField);
            model.addAttribute("sortDir", sortDir);
            model.addAttribute("reversedSort", reversedSort);
            model.addAttribute("keyword", keyword);
        } else {
            try {
                model.addAttribute("users", List.of(service.findById(id)));
                model.addAttribute("totalElement", 1);
                model.addAttribute("totalPage", 1);
                model.addAttribute("currentPage", 1);
                model.addAttribute("startCount", 1);
                model.addAttribute("endCount", 1);
                model.addAttribute("keyword", keyword);
            } catch (UserNotFoundException e) {
                model.addAttribute("message", e.getMessage());
                model.addAttribute("isSuccess", false);
            }
        }

        return "users/users";
    }

    @GetMapping("/users/new")
    public String newUser(Model model) {
        var listRoles = service.listRoles();
        var user = new User();
        user.setEnabled(true);
        model.addAttribute("user", user);
        model.addAttribute("listRoles", listRoles);
        model.addAttribute("title", "Create new user");

        return "users/user-form";
    }

    @PostMapping("/users/save")
    public String saveUser(User user, RedirectAttributes redirectAttributes, @RequestParam(name = "image") MultipartFile multipartFile, String photo) throws IOException {
        User savedUser = null;
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

        redirectAttributes.addFlashAttribute("message", "The user has been saved successfully.");
        redirectAttributes.addFlashAttribute("isSuccess", true);

        String tailRequest = savedUser == null ? "" : "?id=" + savedUser.getId();
        return "redirect:/users" + tailRequest;
    }

    @GetMapping("/users/edit/{id}")
    public String editUser(@PathVariable(name = "id") Integer id, Model model, RedirectAttributes redirectAttributes) {
        var listRoles = service.listRoles();
        model.addAttribute("listRoles", listRoles);
        model.addAttribute("title", "Edit user");
        try {
            var user = service.findById(id);
            model.addAttribute("user", user);
            return "users/user-form";
        } catch (UserNotFoundException e) {
            redirectAttributes.addFlashAttribute("message", e.getMessage());
            redirectAttributes.addFlashAttribute("success", false);
            return "redirect:/users";
        }
    }

    @GetMapping("/users/delete/{id}")
    public String deleteUser(@PathVariable(name = "id") Integer id, RedirectAttributes redirectAttributes) {
        try {
            var user = service.findById(id); // to check that the user is existing
            service.deleteUser(id);
            redirectAttributes.addFlashAttribute("message", "The user has been removed successfully.");
            redirectAttributes.addFlashAttribute("isSuccess", true);

        } catch (UserNotFoundException e) {
            redirectAttributes.addFlashAttribute("message", e.getMessage());
            redirectAttributes.addFlashAttribute("isSuccess", false);
        }
        return "redirect:/users";
    }

    @GetMapping("/users/{id}/enabled/{status}")
    public String updateEnabledStatus(@PathVariable("id") Integer id, @PathVariable("status") boolean status, RedirectAttributes redirectAttributes, int pageNum, String sortField, String sortDir) {
        try {
            var user = service.findById(id); // to check that the user is existing
            if(user.getEnabled() != status) {
                service.updateEnabledStatus(id, status);
                if(status) {
                    redirectAttributes.addFlashAttribute("message", "User id " + id + " has been enabled.");
                    redirectAttributes.addFlashAttribute("isSuccess", true);
                } else {
                    redirectAttributes.addFlashAttribute("message", "User id " + id + " has been disabled.");
                    redirectAttributes.addFlashAttribute("isSuccess", true);

                }
            }
        } catch (UserNotFoundException e) {
            redirectAttributes.addFlashAttribute("message", e.getMessage());
            redirectAttributes.addFlashAttribute("isSuccess", false);

        }
        return "redirect:/users/page/" + pageNum + "?sortField=" + sortField + "&sortDir=" + sortDir;
    }

    @GetMapping("/users/export/{fileType}")
    public void exportCsvAllUser(@PathVariable(name = "fileType") String fileType, HttpServletResponse response) throws IOException {
        var usersList = service.listAll();
        UserExporterAbstract exporter = null;
        switch (fileType) {
            case "csv" -> exporter = new UserCsvExporter();
            case "excel" -> exporter = new UserExcelExporter();
            case "pdf" -> exporter = new UserPdfExporter();
            default -> {
                return;
            }
        }
        exporter.export(usersList, response);
    }


}
