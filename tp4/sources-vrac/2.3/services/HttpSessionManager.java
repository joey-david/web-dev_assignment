package fr.univlyon1.m1if.m1if03.resas_users.services;

import fr.univlyon1.m1if.m1if03.resas_users.classes.User;
import fr.univlyon1.m1if.m1if03.resas_users.daos.UserDao;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.naming.NameAlreadyBoundException;
import javax.naming.NameNotFoundException;

@Service
public class HttpSessionManager implements SessionManager {
    private UserDao userDao;

    // Injection des dépendances

    /**
     * Méthode destinée à l'injection du DAO par le conteneur.
     * @param userDao DAO instancié par le conteneur dans la classe de configuration (<code>UsersSpringBootApplication</code>)
     */
    @Autowired
    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    // Méthodes de service

    @Override
    public void connect(HttpServletRequest request, User user) {
        HttpSession session = request.getSession(true);
        session.setAttribute("user", user);
        try {
            userDao.add(user);
        } catch (NameAlreadyBoundException e) {
            userDao.update(user.getLogin(), user);
        }
    }

    @Override
    public void disconnect(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if(session != null && session.getAttribute("user") != null && session.getAttribute("user") instanceof User) {
            try {
                String login = ((User) session.getAttribute("user")).getLogin();
                userDao.deleteById(login);
            } catch (NameNotFoundException ignored) { }
            session.invalidate();
        }
    }

    @Override
    public boolean isConnected(HttpServletRequest request) {
        return request.getSession(false) != null;
    }

    @Override
    public String getUser(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        return ((User) session.getAttribute("user")).getLogin();
    }

    @Override
    public void updateUser(HttpServletRequest request, String name) {
        HttpSession session = request.getSession(false);
        User user = (User) session.getAttribute("user");
        user.setName(name);
        session.setAttribute("user", user);
    }
}
