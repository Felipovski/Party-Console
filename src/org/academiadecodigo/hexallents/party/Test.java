package org.academiadecodigo.hexallents.party;

import java.util.LinkedList;
import java.util.List;

public class Test {


    public static void main(String[] args) {

        Server server = new Server();
        List<String> player = new LinkedList<>();
        Score score = new Score(player);



        FastestAnswer fastestAnswer = new FastestAnswer(player, score, server);

        fastestAnswer.load();
    }

}
