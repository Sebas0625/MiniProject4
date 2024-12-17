package org.example.eiscuno.model.gameState;

import org.example.eiscuno.controller.GameUnoController;
import org.example.eiscuno.model.game.GameUno;

/**
 * Interface representing a state of the Uno game.
 * Each state defines how the game transitions to the next turn.
 */
public interface GameState {

    /**
     * Defines the action for transitioning to the next turn.
     *
     * @param gameUnoController the controller responsible for managing the game state and transitions
     */
    void nexTurn(GameUnoController gameUnoController);
}
