package fr.univlyon1.m1if.m1if03.servlets;

import fr.univlyon1.m1if.m1if03.classes.User;

import fr.univlyon1.m1if.m1if03.daos.UserDao;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import javax.naming.NameAlreadyBoundException;
import javax.naming.NameNotFoundException;
import java.io.IOException;

/**
 * Cette servlet initialise les objets communs à toute l'application,
 * récupère les infos de l'utilisateur pour les placer dans sa session
 * et affiche l'interface du chat (sans modifier l'URL).
 * &Agrave; noter le fait que l'URL à laquelle elle répond ("/resas") n'est pas le nom de la servlet.
 */
@WebServlet(name = "Connect", value = "/resas")
public class Connect extends HttpServlet {
    // Variables communes pour toute l'application (remplacent la BD).
    // Elles seront stockées dans le contexte applicatif pour pouvoir être accédées par tous les objets de l'application :

    // Map d'objets User
    private UserDao users;


    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        this.users = (UserDao) config.getServletContext().getAttribute("users");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("doPost method called");

        // Print all parameters
        System.out.println("All parameters:");
        request.getParameterMap().forEach((key, value) ->
                System.out.println(key + ": " + String.join(", ", value))
        );

        // Print request details
        System.out.println("Request method: " + request.getMethod());
        System.out.println("Request URI: " + request.getRequestURI());
        System.out.println("Content type: " + request.getContentType());
        System.out.println("Character encoding: " + request.getCharacterEncoding());

        String operation = request.getParameter("operation");
        System.out.println("Operation: " + operation);

        if ("connect".equals(operation)) {
            System.out.println("Handling connect operation");
            handleConnect(request, response);
        } else {
            System.out.println("Invalid operation: " + operation);
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Operation not supported: " + operation);
        }
    }


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String operation = request.getParameter("operation");

        if ("disconnect".equals(operation)) {
            handleDisconnect(request, response);
        } else {
            response.sendRedirect("index.html");
        }
    }

    private void handleConnect(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String login = request.getParameter("login");
        String name = request.getParameter("name");
        User user = new User(login, name);

        HttpSession session = request.getSession();
        session.setAttribute("user", user);

        try {
            users.add(user);
            request.getRequestDispatcher("interface.jsp").forward(request, response);
        } catch (NameAlreadyBoundException e) {
            response.sendError(HttpServletResponse.SC_CONFLICT);
        }
    }

    private void handleDisconnect(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(false);
        if (session != null) {
            User user = (User) session.getAttribute("user");
            if (user != null) {
                try {
                    users.deleteById(user.getLogin());
                } catch (NameNotFoundException ignored) {}
            }
            session.invalidate();
        }
        response.sendRedirect("index.html");
    }
}
