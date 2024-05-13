package ru.shsh.mtshack.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.shsh.mtshack.services.UserService;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class PaymentsAndTransfersController {

    private final UserService userService;

    @GetMapping("/paymentsAndTransfers")
    public String getPage() {
        return "paymentsPage";
    }

    @PostMapping("/payForService")
    public String payForService(@RequestParam("service") String service,
                                @RequestParam("amount") Double amount) {

        return "redirect:/paymentsAndTransfers";
    }

    @PostMapping("/transferToUser")
    public String transferToUser(@RequestParam("phoneNumber") String phoneNumber,
                                 @RequestParam("transferAmount") Double transferAmount,
                                 Principal principal,
                                 Model model) {
        boolean success = userService.transferToUser(principal.getName(), phoneNumber, transferAmount);
        if (success) {
            model.addAttribute("successMessage", "Transfer successful");
        } else {
            model.addAttribute("errorMessage", "Transfer failed");
        }
        return "redirect:/paymentsAndTransfers";
    }
}
