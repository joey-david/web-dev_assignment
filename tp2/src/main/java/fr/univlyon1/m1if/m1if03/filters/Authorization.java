package fr.univlyon1.m1if.m1if03.filters;

import fr.univlyon1.m1if.m1if03.classes.User;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter(filterName = "Authorization", urlPatterns = {"/userlist.jsp", "/user.jsp"})
public class Authorization extends HttpFilter {

    @Override
    protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpSession session = request.getSession(false);
        if (session != null) {
            User currentUser = (User) session.getAttribute("user");
            String requestedLogin = request.getParameter("user");

            if (currentUser != null) {
                if ("POST".equalsIgnoreCase(request.getMethod()) && "modify".equals(request.getParameter("operation"))) {
                    String loginParam = request.getParameter("login");

                    if (currentUser.getLogin().equals(loginParam)) {
                        chain.doFilter(request, response);
                    } else {
                        response.sendError(HttpServletResponse.SC_FORBIDDEN, "Vous n'avez pas le droit de modifier cet utilisateur.");
                        return;
                    }
                }

                if ("GET".equalsIgnoreCase(request.getMethod()) && requestedLogin != null) {
                    if (currentUser.getLogin().equals(requestedLogin)) {
                        chain.doFilter(request, response);
                    } else {
                        response.sendError(HttpServletResponse.SC_FORBIDDEN, "Vous n'avez pas accès à cet utilisateur.");
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