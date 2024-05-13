package ru.shsh.mtshack.services;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.shsh.mtshack.models.entitys.User;
import ru.shsh.mtshack.models.repositorys.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(CustomUserDetailService.class);

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String phoneNumber) throws UsernameNotFoundException {
        logger.info("Attempting to load user by phone number: {}", phoneNumber);

        User user = userRepository.findByPhoneNumber(phoneNumber);

        if (user == null) {
            logger.error("User with phone number {} not found", phoneNumber);
            throw new UsernameNotFoundException("User not found with phone number: " + phoneNumber);
        }

        logger.info("User details loaded successfully for phone number: {}", phoneNumber);
        return user;
    }



}

