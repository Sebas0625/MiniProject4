package org.example.eiscuno.model.game;


import org.example.eiscuno.model.player.Player;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


class GameUnoTest {
    @Test
    void startGame() {
        var humanPlayer = new Player("HUMAN_PLAYER");
        var machinePlayer = new Player("MACHINE_PLAYER");
        assertEquals(humanPlayer.getCardsPlayer().size(), machinePlayer.getCardsPlayer().size(), "El numero de cartas de ambos jugadores es el mismo");
    }

}