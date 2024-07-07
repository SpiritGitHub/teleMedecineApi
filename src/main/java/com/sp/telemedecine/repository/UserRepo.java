package com.sp.telemedecine.repository;


import com.sp.telemedecine.models.User;
import com.sp.telemedecine.models.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface UserRepo extends JpaRepository<User, Long> {

    User findByUserRole(UserRole userRole);

    Optional<User> findByEmail(String email);

    boolean existsByUserRole(UserRole userRole);
    boolean existsByEmail(String email);
    boolean existsByPseudo(String pseudo);
}
