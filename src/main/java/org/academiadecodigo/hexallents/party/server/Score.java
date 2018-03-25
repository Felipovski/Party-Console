package org.academiadecodigo.hexallents.party.server;

import java.util.List;

/**
 * Handles player's score
 */
public class Score {
    private List<String> players;
    private int[] scores = {0,0,0,0};

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

    /**
     * Get's player's points
     *
     * @param name player's name
     * @return String of player's points
     */
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
