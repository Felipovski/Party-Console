package org.academiadecodigo.hexallents.party.server.games;

/**
 *
 */

public interface Game {

    void load();

    void start() throws InterruptedException;

}