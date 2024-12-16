package org.example.eiscuno.model.gameState;

import org.example.eiscuno.controller.GameUnoController;
import org.example.eiscuno.model.game.GameUno;

public interface GameState {
    void nexTurn(GameUnoController gameUnoController);
}
