package fr.univlyon1.m1if.m1if03.classes;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * Réservations créées par les utilisateurs.
 * Une réservation comporte :<br>
 * - une date de début et une date de fin<br>
 * - une liste d'ids de joueurs<br>
 * - une liste de commentaires<br>
 * - un booléen (complète / en recherche de joueurs).<br>
 * Pour la créer, il faut indiquer l'id de l'utilisateur qui l'a créée.
 */
public class Resa {
    private static final int MAX_PLAYERS = 2;
    private final int hash;
    private String title;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private final List<User> players;
    private final List<String> comments;

    /**
     * Création d'une réservation.
     * @param title Texte indiqué dans lea réservation
     * @param creator Login de l'utilisateur créateur
     * @param startDate Date et heure de début de la réservation
     * @param endDate Date et heure de fin de la réservation
     */
    public Resa(String title, User creator, LocalDateTime startDate, LocalDateTime endDate) {
        this.title = title;
        // Pour permettre de les distinguer les instances
        this.hash = Objects.hash(title, creator, (new Date()).getTime());
        this.startDate = startDate;
        this.endDate = endDate;
        this.players = new ArrayList<>();
        this.comments = new ArrayList<>();
        this.addPlayer(creator);
    }

    /**
     * Création d'une réservation.
     * @param title Texte indiqué dans lea réservation
     * @param creator Login de l'utilisateur créateur
     * @param startDate Date et heure de début de la réservation
     * @param duration Durée de la réservation
     */
    public Resa(String title, User creator, LocalDateTime startDate, Duration duration) {
        this(title, creator, startDate, startDate.plus(duration));
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isCompleted() {
        return this.players.size() >= MAX_PLAYERS;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public Duration getDuration() {
        return Duration.between(startDate, endDate);
    }

    public void setDuration(Duration duration) {
        this.endDate = startDate.plus(duration);
    }

    public List<User> getPlayers() {
        return players;
    }

    public User getPlayer(int index) {
        return players.get(index);
    }

    public boolean hasPlayer(User player) {
        return players.contains(player);
    }

    public boolean addPlayer(User player) {
        if(hasPlayer(player) || isCompleted()) {
            return false;
        }
        return players.add(player);
    }

    public boolean removePlayer(User player) {
        return players.remove(player);
    }

    public List<String> getComments() {
        return comments;
    }

    public String getComment(int index) {
        return comments.get(index);
    }

    public int addComment(String comment) {
        comments.add(comment);
        return comments.size();
    }

    public boolean removeComment(int index) {
        if (index >= 0 && index < comments.size()) {
            return comments.remove(comments.get(index));
        }
        return false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)  {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Resa resa = (Resa) o;
        return this.hashCode() == resa.hashCode();
    }

    @Override
    public int hashCode() {
        return this.hash;
    }
}
