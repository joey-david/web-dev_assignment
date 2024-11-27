package fr.univlyon1.m1if.m1if03.resas_api;

import fr.univlyon1.m1if.m1if03.resas_api.dao.ReservationDao;
import fr.univlyon1.m1if.m1if03.resas_api.dao.UserDao;
import fr.univlyon1.m1if.m1if03.resas_api.connection.ConnectionManager;
import fr.univlyon1.m1if.m1if03.resas_api.connection.HttpSessionConnectionManager;
import fr.univlyon1.m1if.m1if03.resas_api.util.ResasJwtTokenProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.context.annotation.ApplicationScope;

/**
 * Classe de démarrage de l'application.
 */
@SpringBootApplication
public class ResasApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(ResasApiApplication.class, args);
	}

	/**
	 * Instanciation d'un DAO pour l'ensemble de l'application.
	 * @return un DAO d'utilisateurs
	 */
	@Bean
	@ApplicationScope
	public UserDao userDao() {
		return new UserDao();
	}

	/**
	 * Instanciation d'un DAO pour l'ensemble de l'application.
	 * @return un DAO d'utilisateurs
	 */
	@Bean
	@ApplicationScope
	public ReservationDao reservationDao() {
		return new ReservationDao();
	}

	/**
	 * Instanciation d'un ConnectionManager pour l'ensemble de l'application.
	 * @return une classe implémentant l'interface <code>ConnectionManager</code>
	 */
	@Bean
	@ApplicationScope
	public ConnectionManager connectionManager() {
		return new HttpSessionConnectionManager();
	}

	@Bean
	@ApplicationScope
	public ResasJwtTokenProvider jwtTokenProvider() {
		return new ResasJwtTokenProvider();
	}
}
