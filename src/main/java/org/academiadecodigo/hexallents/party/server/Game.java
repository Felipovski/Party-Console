package org.academiadecodigo.hexallents.party.server;

public interface Game {

    void load();

    void start() throws InterruptedException;

    void setRounds(int rounds);
}
