package ru.shsh.mtshack.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.shsh.mtshack.models.entitys.Account;
import ru.shsh.mtshack.models.entitys.SavingsAccount;
import ru.shsh.mtshack.models.repositorys.AccountRepository;
import ru.shsh.mtshack.models.repositorys.SavingAccountRepository;

@Service
@RequiredArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;
    private final SavingAccountRepository savingAccountRepository;
    public Account createAccount(Account account) {
        accountRepository.save(account);
        return account;
    }

    public void createSavingsAccount(SavingsAccount savingAccount) {
        savingAccountRepository.save(savingAccount);
    }
}
