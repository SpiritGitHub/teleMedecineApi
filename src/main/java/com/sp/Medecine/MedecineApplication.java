package com.sp.Medecine;

import com.sp.Medecine.models.User;
import com.sp.Medecine.models.UserRole;
import com.sp.Medecine.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
@EnableScheduling
public class MedecineApplication implements CommandLineRunner {

	@Autowired
	private UserRepo userRepository;

	public static void main(String[] args) {
		SpringApplication.run(MedecineApplication.class, args);
	}

	public void run(String... args) throws Exception {
		User adminAccount = userRepository.findByUserRole(UserRole.ADMIN);
		if (null == adminAccount){
			User user = new User();

			user.setPseudo("ADMIN");
			user.setEmail("admin@spirit.com");
			user.setUserRole(UserRole.ADMIN);
			user.setPassword(new BCryptPasswordEncoder().encode("admin"));
			userRepository.save(user);
		}
	}

}
