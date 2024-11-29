package fr.univlyon1.m1if.m1if03.resas_api.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.annotation.Nonnull;

/**
 * Réservations créées par les utilisateurs.
 * Une réservation comporte :<br>
 * - une date de début et une date de fin<br>
 * - une liste d'IDs de joueurs<br>
 * - une liste de commentaires<br>
 * - un booléen (complète / en recherche de joueurs)<br>
 * Pour la créer, il faut indiquer l'ID de l'utilisateur qui l'a créée.
 */
public class Reservation {
    private static final int MAX_PLAYERS = 2;
    private final int hash;
    private String courtId;
    private final String ownerId;
    @JsonFormat(
            shape = JsonFormat.Shape.STRING,
            pattern = "dd-MM-yyyy hh:mm:ss")
    private LocalDateTime startTime;
    @JsonFormat(
            shape = JsonFormat.Shape.STRING,
            pattern = "dd-MM-yyyy hh:mm:ss")
    private LocalDateTime endTime;
    private final List<Comment> comments = new ArrayList<>();
    private final List<String> players = new ArrayList<>();

    /**
     * Création d'une réservation.
     * @param courtId Nom du court indiqué dans la réservation
     * @param creatorId Login de l'utilisateur créateur
     * @param startTime Date et heure de début de la réservation
     * @param endTime Date et heure de fin de la réservation
     */
    public Reservation(String courtId, String creatorId, LocalDateTime startTime, LocalDateTime endTime) {
        this.courtId = courtId;
        // Pour permettre de distinguer les instances
        this.hash = Objects.hash(courtId, creatorId, (new Date()).getTime());
        this.startTime = startTime;
        this.endTime = endTime;
        this.ownerId = creatorId;
        this.addPlayer(creatorId);
    }

//    public int getId() {
//        return hash;
//    }

    public String getOwnerId() {
        return ownerId;
    }

    public String getCourtId() {
        return courtId;
    }

    public void setCourtId(String courtId) {
        this.courtId = courtId;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public int addComment(Comment comment) {
        comments.add(comment);
        return comments.size();
    }

    public List<String> getPlayers() {
        return players;
    }

    public boolean hasPlayer(@Nonnull String player) {
        return players.contains(Objects.requireNonNull(player));
    }

    public boolean addPlayer(String player) {
        if(hasPlayer(player) || isCompleted()) {
            return false;
        }
        return players.add(player);
    }

    public boolean removePlayer(@Nonnull String player) {
        return players.remove(Objects.requireNonNull(player));
    }

    public boolean isCompleted() {
        return this.players.size() >= MAX_PLAYERS;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)  {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Reservation resa = (Reservation) o;
        return this.hashCode() == resa.hashCode();
    }

    @Override
    public int hashCode() {
        return this.hash;
    }
}
