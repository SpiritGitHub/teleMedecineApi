package com.sp.telemedecine;

import com.sp.telemedecine.models.User;
import com.sp.telemedecine.models.UserRole;
import com.sp.telemedecine.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
@EnableScheduling
public class TelemedecineApplication implements CommandLineRunner {

	@Autowired
	private UserRepo userRepository;

	public static void main(String[] args) {
		SpringApplication.run(TelemedecineApplication.class, args);
	}

	public void run(String... args) throws Exception {
		User adminAccount = userRepository.findByUserRole(UserRole.ADMIN);
		if (null == adminAccount){
			User user = new User();

			user.setPseudo("ADMIN");
			user.setEmail("admin@spirit.com");
			user.setUserRole(UserRole.ADMIN);
			user.setPassword(new BCryptPasswordEncoder().encode("admin"));
			user.setActive(true);
			userRepository.save(user);
		}
	}
}
