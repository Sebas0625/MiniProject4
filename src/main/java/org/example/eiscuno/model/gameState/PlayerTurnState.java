package org.example.eiscuno.model.gameState;

import org.example.eiscuno.controller.GameUnoController;
/**
 * Represents the player's turn state in the UNO game.
 * Implements the {@code GameState} interface to define the behavior when it's the player's turn.
 */
public class PlayerTurnState implements GameState{
    /**
     * Advances the game to the next turn, changing the state to {@code MachineTurnState}.
     * This method is called when the player's turn is over, and the game should proceed
     * to the machine's turn.
     *
     * @param gameUnoController The {@code GameUnoController} responsible for managing the game state.
     */
    @Override
    public void nexTurn(GameUnoController gameUnoController) {
        gameUnoController.setState(new MachineTurnState());
    }
}
