package fr.univlyon1.m1if.m1if03.resas_api.service;

import fr.univlyon1.m1if.m1if03.resas_api.model.Comment;
import fr.univlyon1.m1if.m1if03.resas_api.model.Reservation;
import fr.univlyon1.m1if.m1if03.resas_api.dao.ReservationDao;
import fr.univlyon1.m1if.m1if03.resas_api.exception.CompleteReservationException;
import fr.univlyon1.m1if.m1if03.resas_api.exception.DeletedReservationException;
import fr.univlyon1.m1if.m1if03.resas_api.exception.PlayerAlreadyRegisteredException;
import fr.univlyon1.m1if.m1if03.resas_api.exception.PlayerNotRegisteredException;
import fr.univlyon1.m1if.m1if03.resas_api.connection.ConnectionManager;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.naming.InvalidNameException;
import javax.naming.NameNotFoundException;
import java.util.ArrayList;
import java.util.List;

/**
 * Méthodes de service du contrôleur d'opérations métier sur les réservations.
 */
@Service
public class ReservationOperationService {

    @Autowired
    private ReservationDao reservationDao;
    @Autowired
    private ConnectionManager connectionManager;

    @SuppressWarnings("unchecked")
    public void registerUserToReservation(int reservationId, HttpServletRequest request) throws
            InvalidNameException, NameNotFoundException, CompleteReservationException, PlayerAlreadyRegisteredException, DeletedReservationException {
        Reservation reservation = reservationDao.findOne(reservationId);
        if(reservation.isCompleted()) {
            throw new CompleteReservationException("La réservation " + reservationId + " est déjà complète.");
        }
        String userId = connectionManager.getUser(request);

        if(!reservation.addPlayer(userId)) {
            throw new PlayerAlreadyRegisteredException("Vous êtes déjà inscrit.e à la réservation " + reservationId + ".");
        }

        // Ajout de la réservation dans la session pour retrouver plus facilement les playedReservations
        if(request.getSession().getAttribute("playedReservations") == null) {
            request.getSession().setAttribute("playedReservations", new ArrayList<Integer>());
        }
        ((List<Integer>) request.getSession().getAttribute("playedReservations")).add(reservationId);
    }

    @SuppressWarnings("unchecked")
    public void unregisterUserFromReservation(int reservationId, HttpServletRequest request) throws
            InvalidNameException, NameNotFoundException, PlayerNotRegisteredException, DeletedReservationException {
        Reservation reservation = reservationDao.findOne(reservationId);
        String userId = connectionManager.getUser(request);
        if(!reservation.removePlayer(userId)) {
            throw new PlayerNotRegisteredException("Vous n'êtes pas inscrit.e à la réservation " + reservationId + ".");
        }

        ((List<Integer>) request.getSession().getAttribute("playedReservations")).remove(reservationId);
    }

    public void addComment(int reservationId, Comment temp, HttpServletRequest request) throws
            InvalidNameException, NameNotFoundException, DeletedReservationException {
        Reservation reservation = reservationDao.findOne(reservationId);
        String userId = connectionManager.getUser(request);
        Comment comment = new Comment(userId, temp.content());
        reservation.addComment(comment);
    }
}
