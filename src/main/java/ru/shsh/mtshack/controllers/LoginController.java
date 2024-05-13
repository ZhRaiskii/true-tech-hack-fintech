package ru.shsh.mtshack.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import ru.shsh.mtshack.models.entitys.Account;
import ru.shsh.mtshack.models.entitys.User;
import ru.shsh.mtshack.services.AccountService;
import ru.shsh.mtshack.services.UserService;

@Controller
@RequiredArgsConstructor
public class LoginController {
    private final UserService userService;
    private final AccountService accountService;
    @GetMapping("/login")
    public String login(){
        return "loginPage";
    }


    @GetMapping("/registration")
    public String registration(){

        return "registrationPage";
    }
    @PostMapping("/registration")
    public String createUser(User user, Model model) {
        if (!userService.createUser(user)) {
            model.addAttribute("errorMessage", "Пользователь с Username : " +
                    user.getUsername() + " уже существует!");
            return "redirect:/registration";
        }

        return "redirect:/login";
    }

}
