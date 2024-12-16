package org.example.eiscuno.model.player;


import org.example.eiscuno.model.card.Card;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {


        private Player player;

        @BeforeEach
        void setUp() {
            // Inicializamos un jugador antes de cada prueba
            player = new Player("HUMAN_PLAYER");
        }

        @Test
        void testGetCurrentVisibleCards_NoCards() {
            // Prueba cuando no hay cartas en la mano
            Card[] visibleCards = player.getCurrentVisibleCards(0);
            assertEquals(0, visibleCards.length, "Retorna un array vacio cuando no hay cartas presentes");
        }
    }
