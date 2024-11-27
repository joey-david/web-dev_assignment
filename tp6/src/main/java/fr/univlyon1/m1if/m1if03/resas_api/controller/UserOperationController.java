package fr.univlyon1.m1if.m1if03.resas_api.controller;

import fr.univlyon1.m1if.m1if03.resas_api.model.User;
import fr.univlyon1.m1if.m1if03.resas_api.service.UserOperationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.naming.NameNotFoundException;

/**
 * Aiguillage des requêtes liées aux opérations sur les utilisateurs.
 */
@RestController
@RequestMapping("/users")
public class UserOperationController {

    @Autowired
    private UserOperationService userOperationService;

    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestBody User user, HttpServletRequest request, HttpServletResponse response) {
        try {
            userOperationService.login(user, request, response);
            return ResponseEntity.noContent().build();
        } catch (MatchException e) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Erreur de correspondance des noms pour l'utilisateur " + user.getLogin() + "."
            );
        } catch (NameNotFoundException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Utilisateur " + user.getLogin() + " inconnu."
            );
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request) {
        userOperationService.logout(request);
        return ResponseEntity.noContent().build();
    }
}
