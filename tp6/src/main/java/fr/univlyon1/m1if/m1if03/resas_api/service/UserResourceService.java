package fr.univlyon1.m1if.m1if03.resas_api.service;

import fr.univlyon1.m1if.m1if03.resas_api.model.User;
import fr.univlyon1.m1if.m1if03.resas_api.dao.ReservationDao;
import fr.univlyon1.m1if.m1if03.resas_api.dao.UserDao;
import fr.univlyon1.m1if.m1if03.resas_api.dto.UsersResponseDto;
import fr.univlyon1.m1if.m1if03.resas_api.dto.LinkDto;
import fr.univlyon1.m1if.m1if03.resas_api.util.UrlDecomposer;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.naming.NameAlreadyBoundException;
import javax.naming.NameNotFoundException;
import java.net.URI;
import java.util.List;

/**
 * Méthodes de service du contrôleur de ressources sur les utilisateurs.
  */
@Service
public class UserResourceService {

    @Autowired
    private UserDao userDao;
    @Autowired
    private ReservationDao reservationDao;

    public List<User> getAllUsers() {
        return userDao.findAll().stream().toList();
    }

    public UsersResponseDto getAllUsersDto() {
        return new UsersResponseDto(userDao
                .findAll().stream()
                .map(User::getLogin)
                .map(s -> "users/" + s)
                .map(LinkDto::new)
                .toList()
        );
    }

    public URI createUser(User user) throws NameAlreadyBoundException {
        userDao.add(user);
        return URI.create("users/" + user.getLogin());
    }

    public List<Integer> getReservationsOwnedByUser(String login) {
        return reservationDao.findByOwner(login);
    }

    public User getUser(String login) throws NameNotFoundException {
        //TODO récupérer les infos manquantes dans la session
        return userDao.findOne(login);
    }

    public void updateUser(String login, User user) {
        userDao.update(login, user);
    }

    public void deleteUser(String login) throws NameNotFoundException {
        userDao.deleteById(login);
    }

    public String getUserName(String login) throws NameNotFoundException {
        return userDao.findOne(login).getName();
    }

    public String getReservationOwnedByUserSubProperty(String userId, int reservationIndex, HttpServletRequest request) throws IndexOutOfBoundsException {
        List<Integer> reservations = this.getReservationsOwnedByUser(userId);
        String urlEnd = UrlDecomposer.getUrlEnd(request, 4);
        return reservations.get(reservationIndex) + urlEnd;
    }

    public List<Integer> getReservationsPlayedByUser(String login) {
        return reservationDao.findByPlayer(login);
    }

    public String getReservationPlayedByUserSubProperty(String userId, int reservationIndex, HttpServletRequest request) throws IndexOutOfBoundsException {
        List<Integer> reservations = this.getReservationsPlayedByUser(userId);
        String urlEnd = UrlDecomposer.getUrlEnd(request, 4);
        return reservations.get(reservationIndex) + urlEnd;
    }
}
