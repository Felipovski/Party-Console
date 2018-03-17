package org.academiadecodigo.hexallents.party;

import java.util.List;

public class Score {
    private List<String> players;
    private int[] scores = {0,0,0,0};

    public Score(List<String> players) {

        this.players = players;
    }

    public void changePoints (String player, int points) {

        int index = players.indexOf(player);
        scores[index] += points;

        if(scores[index] < 0) {
            scores[index] = 0;
        }
    }

    public String showPlayerPoints(String name){
        return name + ": " +scores[players.indexOf(name)];
    }

    @Override
    public String toString() {
        StringBuilder outputScore = new StringBuilder();
        int index = 0;

        for (String player : players){
            outputScore.append(player + ": " + scores[index] + "\t");
            index++;
        }
        return outputScore.toString();
    }
}
