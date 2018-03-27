package org.academiadecodigo.hexallents.party.server.games;

/**
 * Interface Game
 */

public interface Game {

    /**
     * load necessary files to initiate next game
     */
    void load();

    /**
     * starts the game
     *
     */
    void start();

}