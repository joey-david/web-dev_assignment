package fr.univlyon1.m1if.m1if03.resas_api.exception;

/**
 * À utiliser lors d'une requête de désinscription d'un utilisateur à une réservation à laquelle il n'est pas inscrit.
 */
public class PlayerNotRegisteredException extends RuntimeException {
    public PlayerNotRegisteredException(String message) {
        super(message);
    }
}
