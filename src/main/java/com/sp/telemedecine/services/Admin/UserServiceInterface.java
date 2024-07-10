package com.sp.telemedecine.services.Admin;

import com.sp.telemedecine.models.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserServiceInterface {
    UserDetailsService userDetailsService();
    List<User> getAllUsers();
    void deleteUser(Long id);
    User getUserById(Long id);
}
