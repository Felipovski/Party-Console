package org.academiadecodigo.hexallents.party;

import org.academiadecodigo.hexallents.party.messages.Messages;
import org.academiadecodigo.hexallents.party.server.Score;
import org.academiadecodigo.hexallents.party.server.Server;
import org.academiadecodigo.hexallents.party.server.games.CardsAgainstHumanity;
import org.academiadecodigo.hexallents.party.server.games.FastestAnswer;
import org.academiadecodigo.hexallents.party.server.games.Game;
import java.io.IOException;
import java.util.List;

public class Main {

    public static void main(String[] args) {

        Server server = new Server();

        try {
            server.listen();
        } catch (IOException e) {
            e.printStackTrace();
        }

        List<String> players = server.getPlayerNames();

        Score score = new Score(players);
        Game game = new FastestAnswer(score, server, Server.ROUNDS );
        Game game2 = new CardsAgainstHumanity(score, server, Server.ROUNDS);

        server.sendAll(Messages.clearScreen().toString());
        server.sendAll(Messages.gameMessage().toString());
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        game.load();
        server.sendAll(Messages.clearScreen().toString());
        try {
            game.start();
            game2.start();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
