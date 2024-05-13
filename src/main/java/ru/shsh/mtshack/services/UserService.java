package ru.shsh.mtshack.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.shsh.mtshack.models.enams.CurrencyType;
import ru.shsh.mtshack.models.enams.Role;
import ru.shsh.mtshack.models.entitys.Account;
import ru.shsh.mtshack.models.entitys.SavingsAccount;
import ru.shsh.mtshack.models.entitys.User;
import ru.shsh.mtshack.models.repositorys.UserRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AccountService  accountService;
    @Transactional
    public boolean createUser(User user) {
        String login = user.getUsername();
        if (userRepository.findByPhoneNumber(user.getUsername()) != null) {
            return false;
        }
        user.setActive(true);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.getRoles().add(Role.ROLE_USER);

        Account account = new Account();
        account.setUser(user);
        account.setBalance(10000);
        account.setName("Основной счет");
        account.setCurrencyType(CurrencyType.RUBLE);

        accountService.createAccount(account);
        userRepository.save(user);
        log.info("Saving new User with login: {}", login);
        return true;
    }

    @Transactional
    public boolean createAdmin(User user) {
        String phoneNumber = user.getPhoneNumber();
        if (userRepository.findByPhoneNumber(phoneNumber) != null) {
            log.warn("Admin with phone number {} already exists", phoneNumber);
            return false;
        }
        user.setActive(true);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.getRoles().add(Role.ROLE_ADMIN);
        userRepository.save(user);
        log.info("Saving new Admin with phone number: {}", phoneNumber);
        return true;
    }

    public User getUserByPhoneNumber(String phoneNumber) {
        return userRepository.findByPhoneNumber(phoneNumber);
    }

    @Transactional
    public boolean transferToUser(String senderPhoneNumber, String recipientPhoneNumber, Double amount) {
        User sender = getUserByPhoneNumber(senderPhoneNumber);
        User recipient = getUserByPhoneNumber(recipientPhoneNumber);

        if (sender == null || recipient == null) {
            log.warn("Sender or recipient not found");
            return false;
        }

        Account senderAccount = findMainAccount(sender);
        Account recipientAccount = findMainAccount(recipient);

        if (senderAccount == null || recipientAccount == null) {
            log.warn("Sender or recipient main account not found");
            return false;
        }

        if (senderAccount.getBalance() < amount) {
            log.warn("Insufficient funds in the sender's account");
            return false;
        }

        senderAccount.setBalance(senderAccount.getBalance() - amount);
        recipientAccount.setBalance(recipientAccount.getBalance() + amount);

        log.info("Transfer from {} to {} successful", senderPhoneNumber, recipientPhoneNumber);
        return true;

    }
    @Transactional
    public boolean transferToAccount(Account accountSender, Account accountRecipient, Double amount){

        if (accountRecipient == null || accountSender == null) {
            log.warn("Sender or recipient main account not found");
            return false;
        }
        if (accountSender.getBalance() < amount) {
            log.warn("Insufficient funds in the sender's account");
            return false;
        }
        accountRecipient.setBalance(accountSender.getBalance() + amount);
        accountSender.setBalance(accountSender.getBalance() - amount);

        return true;
    }
    public boolean payment(String phoneNumber, Double amount){
        User sender = getUserByPhoneNumber(phoneNumber);
        if (sender == null) {
            log.warn("Sender not found");
            return false;
        }

        Account senderAccount = findMainAccount(sender);
        if (senderAccount == null) {
            log.warn("Sender or recipient main account not found");
            return false;
        }

        if (senderAccount.getBalance() < amount) {
            log.warn("Insufficient funds in the sender's account");
            return false;
        }
        senderAccount.setBalance(senderAccount.getBalance() - amount);
        userRepository.save(sender);
        return true;
    }




    public Double getAccountBalance(User user){
        return user.getAccounts().get(0).getBalance();
    }

    public String getUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            return authentication.getName();
        }
        return null;
    }
    private void createMainAccount(User user) {
        Account account = new Account();
        account.setUser(user);
        account.setBalance(10000);
        account.setName("Main Account");
        account.setCurrencyType(CurrencyType.RUBLE);
        user.getAccounts().add(account); // Добавим созданный счет в список счетов пользователя

    }


    private Account findMainAccount(User user) {
        List<Account> mainAccounts = user.getAccounts();
        for (Account account : mainAccounts) {
            if (!(account instanceof SavingsAccount)) {
                return account;
            }
        }
        return null;
    }

    public double getAccountBalanceByPhoneNumber(String phoneNumber) {
        User user = getUserByPhoneNumber(phoneNumber);
        if (user != null) {
            Account mainAccount = findMainAccount(user);
            if (mainAccount != null) {
                return mainAccount.getBalance();
            } else {
                log.warn("Main account not found for user with phone number: {}", phoneNumber);
            }
        } else {
            log.warn("User not found with phone number: {}", phoneNumber);
        }
        return 0.0; // Возвращаем 0.0 в случае, если не удалось найти баланс
    }

}
