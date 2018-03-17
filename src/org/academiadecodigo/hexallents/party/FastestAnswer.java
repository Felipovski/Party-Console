package org.academiadecodigo.hexallents.party;

import java.io.*;
import java.util.LinkedList;
import java.util.List;

public class FastestAnswer implements Game {

    private Score score;
    private Server server;
    private List<String> players;
    private int rounds;
    private String[] questions;
    private String[] answers;


    public FastestAnswer(List<String> players, Score score, Server server) {
        this.score = score;
        this.server = server;
        this.players = players;
    }


    @Override
    public void load() {
        File document = new File("fastest-answers/FastestAnswers.txt");
        int numberOfFileLines = 88;
/*
        try {
            BufferedReader in = new BufferedReader(new FileReader(document));
            in.mark(10000);
        } catch (IOException e) {
            System.err.println("Error Accessing File");
            e.printStackTrace();
        }
*/
        questions = new String[rounds];
        answers = new String[rounds];

        List<Integer> list = new LinkedList<>();

        for ( int i = 0; i < rounds; i++) {

            int questionNumber = getQuestionNumber(numberOfFileLines);

            while (list.contains(questionNumber)) {
                questionNumber = getQuestionNumber(numberOfFileLines);
            }
            try {
                BufferedReader in = new BufferedReader(new FileReader(document));
                questions[i] = getQuestion(questionNumber, in);
                answers[i] = in.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }



        }

    }

    private String getQuestion(int questionIndex, BufferedReader in) throws IOException {
        while (questionIndex >= 0){
            in.readLine();
            questionIndex--;
        }
        return in.readLine();
    }

    private int getQuestionNumber(int numberOfFileLines) {
        int questionNumber = (int) (Math.random() * numberOfFileLines);
        if (!(questionNumber % 2 == 0)) {
            questionNumber--;
        }
        return questionNumber;
    }


    @Override
    public void start() {
        server.setGameRunning(true);
    }

    @Override
    public void setRounds(int rounds) {
        this.rounds = rounds;
    }
}
