package fr.univlyon1.m1if.m1if03.servlets;

import fr.univlyon1.m1if.m1if03.classes.User;

import fr.univlyon1.m1if.m1if03.daos.Dao;
import fr.univlyon1.m1if.m1if03.daos.UserDao;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import javax.naming.NameAlreadyBoundException;
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
    private final Dao<User> users = new UserDao();

    @Override
    public void init(ServletConfig config) throws ServletException {
        // Cette instruction doit toujours être au début de la méthode init() pour pouvoir accéder à l'objet config.
        super.init(config);
        //Récupère le contexte applicatif et y place les variables globales
        ServletContext context = config.getServletContext();
        context.setAttribute("users", users);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Récupère l'User dans l'attribut de session et le place dans la map (qui est un attribut de contexte)
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        try {
            users.add(user);
            // Utilise un RequestDispatcher pour "transférer" la requête à un autre objet, en interne du serveur.
            // Ceci n'est pas une redirection HTTP ; le client n'est pas informé de cette redirection.
            // Note :
            //     il existe deux méthodes pour transférer une requête (et une réponse) à l'aide d'un RequestDispatcher : include et forward
            //     voir les différences ici : https://docs.oracle.com/javaee/6/tutorial/doc/bnagi.html
            request.getRequestDispatcher("interface.jsp").forward(request, response);
        } catch (NameAlreadyBoundException e) {
            response.sendError(HttpServletResponse.SC_CONFLICT);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // Ceci est une redirection HTTP ; le client est informé qu'il doit requêter une autre ressource
        response.sendRedirect("index.html");
    }
}
