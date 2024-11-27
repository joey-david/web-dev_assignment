package fr.univlyon1.m1if.m1if03.resas_api.connection;

import fr.univlyon1.m1if.m1if03.resas_api.model.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Interface standardisée des gestionnaires de connection (par session ou autre) de l'utilisateur.
 */
public interface ConnectionManager {

    /**
     * Permet à un utilisateur de se connecter.
     * Si l'utilisateur était déjà connecté, "écrase" la session existante avec la nouvelle
     * @param request la requête HTTP contenant les informations de connexion
     * @param response la réponse HTTP dans laquelle pourra être inséré un token de connexion
     * @param user les paramètres de la demande de connexion
     */
    void connect(HttpServletRequest request, HttpServletResponse response, User user);

    /**
     * Réalise la déconnexion.
     * @param request la requête contenant les informations de connexion à supprimer
     */
    void disconnect(HttpServletRequest request);

    /**
     * Indique si l'utilisateur est déjà connecté.
     * @param request la requête contenant les informations de connexion éventuelle
     * @return un booléen ...
     */
    boolean isConnected(HttpServletRequest request);

    /**
     * Renvoie l'ID de l'utilisateur connecté.
     * Génère une <code>NullPointerException</code> si l'utilisateur n'est pas connecté.
     * @param request la requête contenant les informations de connexion éventuelle
     * @return le login de l'utilisateur connecté
     */
    String getUser(HttpServletRequest request);

    /**
     * Met à jour le nom de l'utilisateur connecté.
     * @param request la requête contenant les informations de connexion éventuelle
     * @param response la réponse HTTP dans laquelle pourra être inséré un token de connexion
     * @param name le nom de l'utilisateur à modifier
     */
    void updateUser(HttpServletRequest request, HttpServletResponse response, String name);
}
