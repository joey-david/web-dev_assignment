package fr.univlyon1.m1if.m1if03.resas_users.filters;

import fr.univlyon1.m1if.m1if03.resas_users.classes.User;
import fr.univlyon1.m1if.m1if03.resas_users.services.ConnectionManager;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Filtre d'authentification.
 * Laisse passer les URLs "/" et "/index.html", et le traitement du formulaire de la page <code>index.html</code>
 * Autorise également l'accès aux clients ayant déjà une session existante.
 */
@Component
public class AuthenticationFilter implements Filter {
    @Autowired
    private ConnectionManager connectionManager;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        // Permet de retrouver la fin de l'URL (après l'URL du contexte) -> indépendant de l'URL de déploiement
        String url = httpRequest.getRequestURI().replace(httpRequest.getContextPath(), "");

        boolean isPublicPath = url.equals("/")
                || url.equals("/index.html")
                || url.equals("/users/connect")
                || url.equals("/users/hello")
                || url.startsWith("/css/");

        // Laisse passer les URLs ne nécessitant pas d'authentification et les requêtes par des utilisateurs authentifiés
        if(isPublicPath || connectionManager.isConnected(httpRequest)) {
            chain.doFilter(request, response);
            return;
        }

        // Bloque les autres requêtes
        httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Vous devez vous connecter pour accéder au site.");
    }
}
