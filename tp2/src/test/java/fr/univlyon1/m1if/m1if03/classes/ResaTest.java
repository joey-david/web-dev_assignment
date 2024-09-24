package fr.univlyon1.m1if.m1if03.classes;

import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class ResaTest {
    private final User toto = new User("toto", "Toto");
    private final User titi = new User("titi", "Titi");
    private final User tutu = new User("tutu", "Tutu");
    private final Resa resa = new Resa("test Resa", toto.getLogin(), LocalDateTime.of(2024, 9, 4, 14, 0, 0), Duration.ofMinutes(60));

    @Test
    void testGetTitle() {
        assertEquals(resa.getTitle(), "test Resa");
    }

    @Test
    void testSetTitle() {
        resa.setTitle("new title");
        assertEquals(resa.getTitle(), "new title");
    }

    @Test
    void testIsCompleted() {
        assertFalse(resa.isCompleted());
        resa.addPlayer(titi.getLogin());
        assertTrue(resa.isCompleted());
    }

    @Test
    void testEquals() {
        Resa resa2 = new Resa("test Resa 2", titi.getLogin(), LocalDateTime.of(2024, 9, 4, 14, 0, 0), Duration.ofMinutes(60));
        assertNotEquals(resa, resa2);
    }

    @Test
    void testHashCode() {
        try {
            Integer h = resa.hashCode();
            assertNotEquals(h, 0);
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    void testAddPlayer() {
        resa.addPlayer(titi.getLogin());
        resa.addPlayer(tutu.getLogin());
        assertTrue(resa.getPlayerLogins().contains(titi.getLogin()));
        assertFalse(resa.getPlayerLogins().contains(tutu.getLogin()));
    }
}
