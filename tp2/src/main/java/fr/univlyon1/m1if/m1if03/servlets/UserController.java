package fr.univlyon1.m1if.m1if03.servlets;

import fr.univlyon1.m1if.m1if03.classes.User;
import fr.univlyon1.m1if.m1if03.daos.Dao;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import javax.naming.InvalidNameException;
import javax.naming.NameNotFoundException;
import java.io.IOException;
import java.util.Collection;
import java.util.List;

@WebServlet(name = "UserController", urlPatterns = {"/users", "/user"})
public class UserController extends HttpServlet {
    private Dao<User> userDao;

    @Override
    public void init() throws ServletException {
        this.userDao = (Dao<User>) getServletContext().getAttribute("users");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = request.getServletPath();

        if ("/users".equals(path)) {
            // FIX: Do not cast to List, use Collection instead
            Collection<User> users = userDao.findAll();  // Collection, not List
            request.setAttribute("users", users);
            request.getRequestDispatcher("userlist.jsp").forward(request, response);
        } else if ("/user".equals(path)) {
            String login = request.getParameter("user");
            try {
                User user = userDao.findOne(login);
                request.setAttribute("user", user);
                request.getRequestDispatcher("user.jsp").forward(request, response);
            } catch (NameNotFoundException | InvalidNameException e) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "User not found: " + login);
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String login = request.getParameter("login");
        String name = request.getParameter("name");

        try {
            User user = userDao.findOne(login);
            user.setName(name);
            userDao.update(login, user);
            response.sendRedirect("users");
        } catch (NameNotFoundException | InvalidNameException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "User not found or invalid: " + login);
        }
    }
}