package ru.shsh.mtshack.models.repositorys;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.shsh.mtshack.models.entitys.Account;
import ru.shsh.mtshack.models.entitys.User;

import java.util.List;

public interface AccountRepository extends JpaRepository<Account, Long> {
    List<Account> findByUser(User user);
}
