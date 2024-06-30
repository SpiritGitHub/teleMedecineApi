package com.sp.Medecine.repository;

import com.sp.Medecine.models.User;
import com.sp.Medecine.models.UserRole;
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
