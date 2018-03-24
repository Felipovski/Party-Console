package org.academiadecodigo.hexallents.party.server;

import org.academiadecodigo.hexallents.party.server.games.FastestAnswer;

import java.util.LinkedList;
import java.util.List;

public class Test {


    public static void main(String[] args) {

        Server server = new Server();
        List<String> player = new LinkedList<>();
        Score score = new Score(player);



        FastestAnswer fastestAnswer = new FastestAnswer(score, server, Server.ROUNDS);

        fastestAnswer.load();

    }

}
