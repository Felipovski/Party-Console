package org.academiadecodigo.hexallents.party;

import java.io.*;
import java.util.LinkedList;
import java.util.List;

public class FastestAnswer implements Game {

    private Score score;
    private Server server;
    private int rounds;
    private String[] questions;
    private String[] answers;

    public FastestAnswer(Score score, Server server) {
        this.score = score;
        this.server = server;
        rounds = 5;
    }


    @Override
    public void load() {
        File document = new File("resources/fastest-answers/FastestAnswers.txt");
        int numberOfFileLines = 88;

        questions = new String[rounds];
        answers = new String[rounds];

        List<Integer> usedQuestionIndex = new LinkedList<>();

        for (int i = 0; i < rounds; i++) {

            int questionNumber = getQuestionNumber(numberOfFileLines);

            while (usedQuestionIndex.contains(questionNumber)) {
                questionNumber = getQuestionNumber(numberOfFileLines);
            }

            usedQuestionIndex.add(questionNumber);

            try {
                BufferedReader in = new BufferedReader(new FileReader(document));
                questions[i] = getQuestion(questionNumber, in);
                answers[i] = in.readLine();
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }


        }
    }

    private String getQuestion(int questionIndex, BufferedReader in) throws IOException {
        while (questionIndex > 0) {
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
    public void start() throws InterruptedException {

        server.setGameRunning(true);
        StringBuilder answer = new StringBuilder();

        /*if(!server.playersReady()){
            start();
            return;
        }*/

        for (int i = 0; i < questions.length; i++) {
            server.sendAll(score.toString());

            server.sendAll("\nQuestion " + (i+1) + ": " + questions[i]);

            while (true) {
                //Thread.sleep(1000);

                answer.append(server.getAnswer());

                System.out.println(answer.toString());


//                wait();
                if (answer.toString().equals("")){
                    answer.delete(0, answer.length());
                    continue;
                }
                System.out.println(answer.substring(answer.indexOf(":")+1, answer.length()));
                System.out.println(answer.substring(0,answer.indexOf(":")));

                if (answers[i].equals(answer.substring(answer.indexOf(":")+1, answer.length()))) {
                    score.changePoints(answer.substring(0, answer.indexOf(":")), 10);
                    answer.delete(0, answer.length());
                    break;
                    }

                score.changePoints(answer.substring(0,answer.indexOf(":")), -5);
                answer.delete(0, answer.length());

            }

        }
        server.sendAll("Bye, sucker");
        server.endGame();
    }


    @Override
    public void setRounds(int rounds) {
        this.rounds = rounds;
    }
}
