package fr.univlyon1.m1if.m1if03.daos;

import fr.univlyon1.m1if.m1if03.classes.Resa;

import javax.naming.NameNotFoundException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ResaDao extends AbstractListDao<Resa> {
    @Override
    protected Serializable getKeyForElement(Resa element) {
        return element.hashCode();
    }

    public Resa findByTitle(String title) throws NameNotFoundException {
        for(Resa resa: this.collection) {
            if(resa !=null && resa.getTitle().equals(title)) {
                return resa;
            }
        }
        throw new NameNotFoundException(title);
    }

    public List<Resa> findByUser(String userLogin) {
        List<Resa> userReservations = new ArrayList<>();
        for(Resa resa : this.collection) {
            if(resa != null && resa.hasPlayer(userLogin)) {
                userReservations.add(resa);
            }
        }
        return userReservations;
    }

    public List<Resa> findAllReservations() {
        List<Resa> allResas = new ArrayList<>();
        for(Resa resa : this.collection) {
            if (resa != null) {
                allResas.add(resa);
            }
        }
        return allResas;
    }
}
