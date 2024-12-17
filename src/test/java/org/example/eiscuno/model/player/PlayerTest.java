package org.example.eiscuno.model.player;



import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {


    @Test
    void testPlayerNameInitialization() {

        Player player = new Player("HUMAN_PLAYER");

        // Comprobar que el nombre del jugador se ha inicializado correctamente
        assertEquals("HUMAN_PLAYER", player.getTypePlayer(), "El nombre del jugador debería ser 'HUMAN_PLAYER'");
    }

    @Test
    void testInitialCardCount() {

        Player player = new Player("HUMAN_PLAYER");

        assertEquals(0, player.getCardsPlayer().size(), "El jugador debería tener 0 cartas al inicio");
    }
}
