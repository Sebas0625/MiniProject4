package org.example.eiscuno.model.gameState;

import org.example.eiscuno.controller.GameUnoController;
/**
 * Represents the machine's turn state in the UNO game.
 * Implements the {@code GameState} interface to define the behavior when it's the machine's turn.
 */
public class MachineTurnState implements GameState{
    /**
     * Advances the game to the next turn, changing the state to {@code PlayerTurnState}.
     * This method is called when the machine's turn is over, and the game should proceed
     * to the player's turn.
     *
     * @param gameUnoController The {@code GameUnoController} responsible for managing the game state.
     */
    @Override
    public void nexTurn(GameUnoController gameUnoController) {
        gameUnoController.setState(new PlayerTurnState());
    }
}
