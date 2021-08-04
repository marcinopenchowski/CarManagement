package com.openchowski.carmanagement.controller;

import com.openchowski.carmanagement.entity.Authority;
import com.openchowski.carmanagement.entity.User;
import com.openchowski.carmanagement.service.AuthorityService;
import com.openchowski.carmanagement.service.UserService;
import org.springframework.aop.scope.ScopedProxyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/users")
public class UserController {

    private UserService userService;
    private AuthorityService authorityService;

    @Autowired
    public UserController(UserService userService, AuthorityService authorityService) {
        this.userService = userService;
        this.authorityService = authorityService;
    }

    @GetMapping("/list")
    public String showAll(Model model,
                          @RequestParam(name = "sortField", required = false, defaultValue = "id") String sortField,
                          @RequestParam(name = "sortDir", required = false, defaultValue = "asc") String sortDir
    ){

        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");

        List<User> userList = userService.findAll(sortField, sortDir);

        model.addAttribute("users", userList);

        return "/user/list-user";
    }

    @GetMapping("/search")
    public String search(
            @RequestParam(value = "searchName", required = false) String searchName,
            Model model
    ){
        if(searchName.isBlank()){
            return "redirect:/users/list";
        }

        List<User> userList = userService.search(searchName);
        model.addAttribute("users" , userList);

        return "user/list-user";
    }

    @InitBinder
    public void initBinder(WebDataBinder dataBinder){
        StringTrimmerEditor stringTrimmerEditor = new StringTrimmerEditor(true);
        dataBinder.registerCustomEditor(String.class, stringTrimmerEditor);
    }


    @PostMapping("/save")
    public String save(
            @Valid @ModelAttribute("user") User user,
            BindingResult bindingResult,
            Model model,
            @RequestParam(value = "idChecked", required = false, defaultValue = "2") List<String> idStr
    ){

        if(bindingResult.hasErrors()){
            List<Authority> authorityList = authorityService.findAll();
            model.addAttribute("authorities", authorityList);

            return "user/add-user";
        }else {
            userService.save(user, idStr);

            return "redirect:/users/list";
        }
    }

    @PostMapping("/delete")
    public String delete(
            @RequestParam(value = "idChecked", required = false) List<String> id){
        if(id != null){
            for(String tempIdStr : id){
                int tempId = Integer.parseInt(tempIdStr);
                userService.deleteById(tempId);
            }
        }
        return "redirect:/users/list";
    }

    @GetMapping("/showAddForm")
    public String showAddForm(Model model){

        User user = new User();
        List<Authority> authorityList = authorityService.findAll();

        model.addAttribute("user", user);
        model.addAttribute("authorities", authorityList);

        return "/user/add-user";
    }

    @GetMapping("/showUpdateForm")
    public String showUpdateForm(Model model, @RequestParam("userId") int id){

        List<Authority> authorityList = authorityService.findAll();

        User user = userService.findById(id);

        model.addAttribute("user", user);
        model.addAttribute("authorities", authorityList);

        return "/user/edit-user";
    }

    @PostMapping("/update")
    public String save(
            @RequestParam(value = "userId") int userId,
            @RequestParam(value = "idChecked") List<String> idStr,
            @RequestParam(value = "username") String username
    ){

        userService.update(userId, idStr, username);

        return "redirect:/users/list";
    }

    @GetMapping("/showChangePasswordForm")
    public String showChangePasswordForm(Model model,
        @RequestParam(value = "errors", required = false) String errors
    ){

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        String currentUserName = null;

        if(principal instanceof UserDetails){
            currentUserName = ((UserDetails)principal).getUsername();
        }else{
            currentUserName = principal.toString();
        }

        User user = userService.findByUsername(currentUserName);

        model.addAttribute("user", user);
        model.addAttribute("errors", errors);

        return "user/change-password";
    }

    @PostMapping("/changePassword")
    public String changePassword(
            @RequestParam(value = "currentPassword") String currentPassword,
            @RequestParam(value = "newPassword") String newPassword
            ){

        try {
            userService.changePassword(currentPassword, newPassword);
        }catch (RuntimeException e){
            return "redirect:/users/showChangePasswordForm?errors=wrongCurrentPassword";
        }
        return "redirect:/";
    }

}
