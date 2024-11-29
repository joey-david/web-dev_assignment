package fr.univlyon1.m1if.m1if03.resas_api.service;

import fr.univlyon1.m1if.m1if03.resas_api.model.User;
import fr.univlyon1.m1if.m1if03.resas_api.dao.UserDao;
import fr.univlyon1.m1if.m1if03.resas_api.connection.ConnectionManager;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.naming.NameNotFoundException;

/**
 * Méthodes de service du contrôleur d'opérations sur les utilisateurs.
  */
@Service
public class UserOperationService {

    @Autowired
    private ConnectionManager connectionManager;
    @Autowired
    private UserDao userDao;

    public void login(User user, HttpServletRequest request, HttpServletResponse response) throws NameNotFoundException, MatchException {
        User userFromDao = userDao.findOne(user.getLogin());
        // On se sert du nom comme d'un mot de passe. Bof...
        if(!user.getName().equals(userFromDao.getName())) {
            throw new MatchException("Le nom doit correspondre à celui dans le DAO.", null);
        }
        connectionManager.connect(request, response, userFromDao);
    }

    public void logout(HttpServletRequest request) {
        connectionManager.disconnect(request);
    }
}
