package ru.shsh.mtshack.models.entitys;

import javax.persistence.*;
import lombok.Getter;
import lombok.Setter;
import ru.shsh.mtshack.models.enams.CurrencyType;

import java.time.ZonedDateTime;

@Entity
@Getter
@Setter
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private double balance;
    private String name;
    private CurrencyType currencyType;

    private ZonedDateTime dateOfCreate;

    private boolean opened;

    @PrePersist
    private void init(){
        dateOfCreate = ZonedDateTime.now();
    }

}
