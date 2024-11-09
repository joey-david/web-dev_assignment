package fr.univlyon1.m1if.m1if03.resas_users;

import fr.univlyon1.m1if.m1if03.resas_users.daos.UserDao;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.context.annotation.ApplicationScope;

@SpringBootApplication
public class UsersSpringBootApplication {

	public static void main(String[] args) {
		SpringApplication.run(UsersSpringBootApplication.class, args);
	}

	@Bean
	@ApplicationScope
	public UserDao userDao() {
		return new UserDao();
	}
}
