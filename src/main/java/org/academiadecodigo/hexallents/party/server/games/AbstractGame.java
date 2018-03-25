package org.academiadecodigo.hexallents.party.server.games;

import org.academiadecodigo.hexallents.party.server.Score;
import org.academiadecodigo.hexallents.party.server.Server;

/**
 * A generic game witch gives common properties to specific games
 * @see Game
 */
public abstract class AbstractGame implements Game {

    protected Score score;
    protected Server server;
    protected int rounds;

    public AbstractGame(Score score, Server server, int rounds){
        this.score = score;
        this.server = server;
        this. rounds = rounds;
    }
}
