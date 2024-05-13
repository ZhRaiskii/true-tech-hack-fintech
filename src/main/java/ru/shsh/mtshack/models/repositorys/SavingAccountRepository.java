package ru.shsh.mtshack.models.repositorys;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.shsh.mtshack.models.entitys.SavingsAccount;
import ru.shsh.mtshack.models.entitys.User;

import java.util.List;

public interface SavingAccountRepository extends JpaRepository<SavingsAccount, Long> {

     List<SavingsAccount> findByUser(User user);
}
