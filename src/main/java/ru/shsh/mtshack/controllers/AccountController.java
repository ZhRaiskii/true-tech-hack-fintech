package ru.shsh.mtshack.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.shsh.mtshack.models.entitys.Account;
import ru.shsh.mtshack.models.enams.CurrencyType;
import ru.shsh.mtshack.models.entitys.SavingsAccount;
import ru.shsh.mtshack.models.entitys.User;
import ru.shsh.mtshack.models.repositorys.AccountRepository;
import ru.shsh.mtshack.models.repositorys.SavingAccountRepository;
import ru.shsh.mtshack.models.repositorys.UserRepository;
import ru.shsh.mtshack.services.AccountService;


import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;
    private final AccountRepository accountRepository;
    private final SavingAccountRepository savingAccountRepository;
    private final UserRepository userRepository;
    @PostMapping("/accountsPage/createSavingsAccount")
    public String createAccount(@RequestParam("accountName") String accountName,
                                @RequestParam("accountCurrency") String accountCurrency,
                                @RequestParam("username") String username) {

        SavingsAccount savingAccount = new SavingsAccount();
        savingAccount.setName("Сберегательный счет");
        savingAccount.setCurrencyType(CurrencyType.valueOf(accountCurrency));
        savingAccount.setUser(userRepository.findByPhoneNumber(username));
        savingAccount.setBalance(0);
        accountService.createSavingsAccount(savingAccount);

        return "redirect:/accountsPage";
    }

    @GetMapping("/accountsPage")
    public String page(Model model, Principal principal) {
        User user = userRepository.findByPhoneNumber(principal.getName());
        List<Account> accounts = accountRepository.findByUser(user);
        List<SavingsAccount> savingsAccounts = savingAccountRepository.findByUser(user);

        if (savingsAccounts != null) {
            model.addAttribute("savingsAccounts", savingsAccounts);
        }
        model.addAttribute("accounts", accounts);
        return "accountsPage";
    }


}
