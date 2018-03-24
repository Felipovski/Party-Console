package org.academiadecodigo.hexallents.party.Server;

public interface Game {

    void load();

    void start() throws InterruptedException;

    void setRounds(int rounds);
}
