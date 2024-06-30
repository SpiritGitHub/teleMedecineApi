package com.sp.Medecine.services.Admin;

import com.sp.Medecine.models.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserServiceInterface {
    UserDetailsService userDetailsService();
    List<User> getAllUsers();
    void deleteUser(Long id);
    User getUserById(Long id);
}
