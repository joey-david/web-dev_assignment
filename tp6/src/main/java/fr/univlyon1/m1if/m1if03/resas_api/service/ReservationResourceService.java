package fr.univlyon1.m1if.m1if03.resas_api.service;

import fr.univlyon1.m1if.m1if03.resas_api.model.Reservation;
import fr.univlyon1.m1if.m1if03.resas_api.dao.ReservationDao;
import fr.univlyon1.m1if.m1if03.resas_api.exception.DeletedReservationException;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.naming.InvalidNameException;
import javax.naming.NameNotFoundException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

/**
 * Méthodes de service du contrôleur de ressources des réservations.
 */
@Service
public class ReservationResourceService {

    @Autowired
    private ReservationDao reservationDao;

    public List<Reservation> getAllReservations() {
        return reservationDao.findAll().stream().toList();
    }

    @SuppressWarnings("unchecked")
    public URI createReservation(Reservation reservation, HttpSession session) {
        int reservationId = Integer.parseInt(reservationDao.add(reservation).toString());

        // Ajout de la réservation dans la session pour retrouver plus facilement les ownedReservations
        if(session.getAttribute("ownedReservations") == null) {
            session.setAttribute("ownedReservations", new ArrayList<Integer>());
        }
        ((List<Integer>) session.getAttribute("ownedReservations")).add(reservationId);

        return URI.create("reservations/" + reservationId);
    }

    public Reservation getReservation(int id) throws InvalidNameException, NameNotFoundException, DeletedReservationException {
        return reservationDao.findOne(id);
    }

    public List<String> findPlayersByReservationId(int reservationId) throws InvalidNameException, NameNotFoundException, DeletedReservationException {
        return reservationDao.findOne(reservationId).getPlayers();
    }

    public void updateReservation(int id, Reservation reservation) throws InvalidNameException {
        reservationDao.update(id, reservation);
    }

    @SuppressWarnings("unchecked")
    public void deleteReservation(int id, HttpSession session) throws InvalidNameException, NameNotFoundException, DeletedReservationException {
        reservationDao.deleteById(id);

        ((List<Integer>) session.getAttribute("ownedReservations")).remove(id);
    }
}
