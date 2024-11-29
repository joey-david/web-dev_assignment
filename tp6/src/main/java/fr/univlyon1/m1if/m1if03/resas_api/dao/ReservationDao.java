package fr.univlyon1.m1if.m1if03.resas_api.dao;

import fr.univlyon1.m1if.m1if03.resas_api.model.Reservation;
import fr.univlyon1.m1if.m1if03.resas_api.exception.DeletedReservationException;

import javax.naming.InvalidNameException;
import javax.naming.NameNotFoundException;
import java.io.Serializable;
import java.util.List;

/**
 * DAO responsable de la gestion des réservations.
 */
public class ReservationDao extends AbstractListDao<Reservation> {

    /**
     * Rajoute à la méthode l'envoi d'une exception pour les réservations précédemment supprimées.
     * @param id La clé de l'élément cherché
     * @return une instance non nulle de la classe <code>Reservation</code>
     * @throws InvalidNameException <i>cf.</i> doc superclasse
     * @throws NameNotFoundException <i>cf.</i> doc superclasse
     * @throws DeletedReservationException <i>cf.</i> doc superclasse
     */
    @Override
    public Reservation findOne(Serializable id) throws InvalidNameException, NameNotFoundException, DeletedReservationException {
        Reservation r = super.findOne(id);
        if(r == null) {
            throw new DeletedReservationException(id.toString());
        }
        return r;
    }

    public List<Integer> findByOwner(String ownerId) {
        return this
                .findAll().stream()
                .filter(r -> r.getOwnerId().equals(ownerId))
                .map(this::getId)
                .map(Serializable::toString)
                .map(Integer::parseInt)
                .toList();
    }

    public List<Integer> findByPlayer(String playerId) {
        return this
                .findAll().stream()
                .filter(r -> r.hasPlayer(playerId))
                .map(this::getId)
                .map(Serializable::toString)
                .map(Integer::parseInt)
                .toList();
    }
}
