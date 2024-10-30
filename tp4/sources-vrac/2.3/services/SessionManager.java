package fr.univlyon1.m1if.m1if03.resas_users.services;

import fr.univlyon1.m1if.m1if03.resas_users.classes.User;
import jakarta.servlet.http.HttpServletRequest;

public interface SessionManager {
    void connect(HttpServletRequest request, User user);

    void disconnect(HttpServletRequest request);

    boolean isConnected(HttpServletRequest request);

    String getUser(HttpServletRequest request);

    void updateUser(HttpServletRequest request, String name);
}
