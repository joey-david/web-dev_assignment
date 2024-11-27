package fr.univlyon1.m1if.m1if03.resas_api.controller;

import fr.univlyon1.m1if.m1if03.resas_api.model.User;
import fr.univlyon1.m1if.m1if03.resas_api.service.UserResourceService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.server.ResponseStatusException;

import javax.naming.NameAlreadyBoundException;
import javax.naming.NameNotFoundException;
import java.net.URI;
import java.util.List;

/**
 * Aiguillage des requêtes liées aux ressources des utilisateurs.
 */
@RestController
@RequestMapping("/users")
public class UserResourceController {

    @Autowired
    private UserResourceService userResourceService;

    @GetMapping(produces = {"application/json", "application/xml"})
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userResourceService.getAllUsers());
    }

    @PostMapping(consumes = {"application/json", "application/xml", "application/x-www-form-urlencoded"})
    public ResponseEntity<Void> createUser(@RequestBody User user) {
        try {
            return ResponseEntity.created(userResourceService.createUser(user)).build();
        } catch (NameAlreadyBoundException e) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Un utilisateur avec le login " + user.getLogin() + " existe déjà."
            );
        }
    }

    @GetMapping(value = "/{userId}", produces = {"application/json", "application/xml"})
    public ResponseEntity<User> getUser(@PathVariable String userId) {
        try {
            //TODO renvoyer qqch de plus complet que juste une instance de la classe User
            return ResponseEntity.ok(userResourceService.getUser(userId));
        } catch (NameNotFoundException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Utilisateur " + userId + " inconnu."
            );
        }
    }

    @PutMapping(value = "/{userId}", consumes = {"application/json", "application/xml"})
    public ResponseEntity<Void> updateUser(@PathVariable String userId, @RequestBody User user) {
        try {
            userResourceService.createUser(user);
            return ResponseEntity.created(URI.create("users/" + userId)).build();
        } catch (NameAlreadyBoundException e) {
            userResourceService.updateUser(userId, user);
            return ResponseEntity.noContent().build();
        }
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable String userId) {
        try {
            userResourceService.deleteUser(userId);
        } catch (NameNotFoundException ignored) { }
        return ResponseEntity.noContent().build();
    }

    @GetMapping(value = "/{userId}/name", produces = {"application/json", "application/xml"})
    public ResponseEntity<String> getUserName(@PathVariable String userId) {
        try {
            return ResponseEntity.ok(userResourceService.getUserName(userId));
        } catch (NameNotFoundException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Utilisateur " + userId + " inconnu."
            );
        }
    }

    @GetMapping(value = "/{userId}/ownedReservations", produces = {"application/json", "application/xml"})
    public ResponseEntity<List<String>> getUserOwnedReservations(@PathVariable String userId) {
            return ResponseEntity.ok(userResourceService
                    .getReservationsOwnedByUser(userId)
                    .stream()
                    .map(id -> "reservations/" + id)
                    .toList()
            );
    }

    @GetMapping("/{userId}/ownedReservations/{reservationIndex}/**")
    public ResponseEntity<Void> getUserOwnedReservationsSubProperty(@PathVariable String userId,
                                                                    @PathVariable int reservationIndex,
                                                                    HttpServletRequest request) {
        try {
            return ResponseEntity
                    .status(HttpStatus.FOUND)
                    .header("Link", "reservations/" + userResourceService.getReservationOwnedByUserSubProperty(userId, reservationIndex, request))
                    .build();
        } catch (IndexOutOfBoundsException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "L'utilisateur " + userId + " n'a actuellement pas de réservation avec le numéro " + reservationIndex + "."
            );
        }
    }

    @GetMapping(value = "/{userId}/registeredReservations", produces = {"application/json", "application/xml"})
    public ResponseEntity<List<String>> getUserRegisteredReservations(@PathVariable String userId) {
        return ResponseEntity.ok(userResourceService
                .getReservationsPlayedByUser(userId)
                .stream()
                .map(id -> "reservations/" + id)
                .toList()
        );
    }

    @GetMapping("/{userId}/registeredReservations/{reservationIndex}/**")
    public ResponseEntity<Void> getUserRegisteredReservationsSubProperty(@PathVariable String userId,
                                                                         @PathVariable int reservationIndex,
                                                                         HttpServletRequest request) {
        try {
            return ResponseEntity
                    .status(HttpStatus.FOUND)
                    .header("Link", "reservations/" + userResourceService.getReservationPlayedByUserSubProperty(userId, reservationIndex, request))
                    .build();
        } catch (IndexOutOfBoundsException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "L'utilisateur " + userId + " n'a actuellement pas d'inscription à une réservation avec le numéro " + reservationIndex + "."
            );
        }
    }
}
