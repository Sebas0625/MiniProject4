package org.example.eiscuno.model.gameState;

import org.example.eiscuno.controller.GameUnoController;

public class MachineTurnState implements GameState{
    @Override
    public void nexTurn(GameUnoController gameUnoController) {
        gameUnoController.setState(new PlayerTurnState());
    }
}
