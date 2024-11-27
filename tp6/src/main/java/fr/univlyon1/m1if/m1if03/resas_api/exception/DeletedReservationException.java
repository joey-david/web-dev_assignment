package fr.univlyon1.m1if.m1if03.resas_api.exception;

/**
 * À utiliser lors d'une tentative d'accès à une réservation qui a été supprimée.
 */
public class DeletedReservationException extends RuntimeException {
    public DeletedReservationException(String message) {
        super(message);
    }
}
