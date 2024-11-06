package fr.univlyon1.m1if.m1if03.resas_users.filters;

import fr.univlyon1.m1if.m1if03.resas_users.classes.User;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * Filtre d'authentification.
 * Laisse passer les URLs "/" et "/index.html", et le traitement du formulaire de la page <code>index.html</code>
 * Autorise également l'accès aux clients ayant déjà une session existante.
 */
public class AuthenticationFilter extends HttpFilter {

    @Override
    protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        // Permet de retrouver la fin de l'URL (après l'URL du contexte) -> indépendant de l'URL de déploiement
        String url = request.getRequestURI().replace(request.getContextPath(), "");

        // Laisse passer les URLs ne nécessitant pas d'authentification et les requêtes par des utilisateurs authentifiés
        if(url.equals("/") || url.equals("/index.html") || url.equals("/users/connect") || request.getSession(false) != null) {
            chain.doFilter(request, response);
            return;
        }

        // Bloque les autres requêtes
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Vous devez vous connecter pour accéder au site.");
    }
}
