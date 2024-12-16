package org.example.eiscuno.model.gameState;

import org.example.eiscuno.controller.GameUnoController;

public class PlayerTurnState implements GameState{
    @Override
    public void nexTurn(GameUnoController gameUnoController) {
        gameUnoController.setState(new MachineTurnState());
    }
}
