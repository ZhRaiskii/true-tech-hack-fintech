package ru.shsh.mtshack.models.repositorys;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.shsh.mtshack.models.entitys.Account;
import ru.shsh.mtshack.models.entitys.User;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByPhoneNumber(String phoneNumber);


}