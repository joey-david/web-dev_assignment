package fr.univlyon1.m1if.m1if03.filters;

import fr.univlyon1.m1if.m1if03.classes.User;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter(filterName = "Authorization", urlPatterns = {"/user.jsp", "/userlist.jsp"})
public class Authorization extends HttpFilter {

    @Override
    protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        System.out.println("Dans le filtre");
        HttpSession session = request.getSession(false);
        if (session != null) {
            User currentUser = (User) session.getAttribute("user");
            String requestedLogin = request.getParameter("user");
            System.out.println("Current user: " + (currentUser != null ? currentUser.getLogin() : "null"));
            System.out.println("Requested login: " + requestedLogin);

            if (currentUser != null) {
                if ("/user.jsp".equals(request.getServletPath())) {
                    if (requestedLogin != null && !currentUser.getLogin().equals(requestedLogin)) {
                        response.sendError(HttpServletResponse.SC_FORBIDDEN, "Vous n'avez pas accès à cet utilisateur.");
                        return;
                    }
                } else if ("/userlist.jsp".equals(request.getServletPath()) && "POST".equalsIgnoreCase(request.getMethod())) {
                    String loginToModify = request.getParameter("login");
                    if (!currentUser.getLogin().equals(loginToModify)) {
                        response.sendError(HttpServletResponse.SC_FORBIDDEN, "Vous n'avez pas le droit de modifier cet utilisateur.");
                        return;
                    }
                }
            } else {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Vous devez être connecté pour effectuer cette action.");
                return;
            }
        } else {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Session invalide. Veuillez vous reconnecter.");
            return;
        }

        chain.doFilter(request, response);
    }
}