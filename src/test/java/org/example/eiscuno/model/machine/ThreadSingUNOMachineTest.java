package org.example.eiscuno.model.machine;

import org.example.eiscuno.controller.GameUnoController;
import org.example.eiscuno.model.game.GameUno;
import org.example.eiscuno.model.player.Player;
import org.example.eiscuno.model.deck.Deck;
import org.example.eiscuno.model.table.Table;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ThreadSingUNOMachineTest {
    @Test
    void testButtonUNOPressedDefaultValue() {
        GameUnoController gameUnoController = new GameUnoController();
        GameUno gameUno = gameUnoController.getGameUno();

        ThreadSingUNOMachine threadSingUNOMachine = new ThreadSingUNOMachine(gameUno, gameUnoController);

        assertFalse(threadSingUNOMachine.getButtonUNOPressed(), "El valor inicial de buttonUNOPressed debe ser falso");
    }
}