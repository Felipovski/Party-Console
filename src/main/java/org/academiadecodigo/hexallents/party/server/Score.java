package org.academiadecodigo.hexallents.party.server;

import java.util.List;

/**
 * Handles player's score
 */
public class Score {
    private List<String> players;
    private int[] scores;

    public Score(List<String> players) {
        this.players = players;
        scores = new int[this.players.size()];
        fillScores();
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

    private void fillScores(){
        for (int i =0; i < scores.length; i++) {
            scores[i] = 100;
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

    public String winner(){
        int maxValue = 0;
        int maxIndex = 0;

        for (int i=0; i < scores.length; i++) {
            if (scores[i] > maxValue ){
               maxValue = scores[i];
               maxIndex = i;
            }
        }

        return "THE WINNER IS " + players.get(maxIndex) + " WITH " +maxValue + " POINTS!\n";
    }
}
