package org.academiadecodigo.hexallents.party.server;

import java.util.List;

/**
 * Handles player's score
 */
public class Score {
    private List<String> players;
    private int[] scores = {100,100,100,100};

    public Score(List<String> players) {

        this.players = players;
    }

    /**
     * Add's points after responses
     *
     * @param player the game's players
     * @param points player's points
     */
    public void changeScore (String player, int points) {

        int index = players.indexOf(player);
        scores[index] += points;

        if(scores[index] < 0) {
            scores[index] = 0;
        }
    }

    @Override
    public String toString() {
        StringBuilder outputScore = new StringBuilder();
        int index = 0;
        for (String player : players){
            outputScore.append(player + ": " + scores[index] + "\t");
            index++;
        }
        outputScore.append("\n");
        return outputScore.toString();
    }
}
