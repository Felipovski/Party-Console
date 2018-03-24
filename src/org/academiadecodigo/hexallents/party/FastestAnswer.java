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

            System.out.printf("i: " + i);
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

            for (String s : questions) {
                System.out.println(s);
            }

            for (String s : answers) {
                System.out.println(s);
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
        for (int i = 0; i < questions.length; i++) {
            server.sendAll("Question " + i + ": " + questions[i]);
            System.out.println(questions[i]);
            System.out.println(answers[i]);

            while (true) {
                String answer = server.getAnswer();
                Thread.sleep(1000);
                System.out.println(answer);

                String name = "";
                String playerAnswer;
                String[] nameAnswer;

//                wait();
                if (answer.equals("")){
                    continue;
                }

                nameAnswer = answer.split( ":");
                name = nameAnswer[0];
                playerAnswer = nameAnswer[1];


                if (answers[i].equals(playerAnswer)) {
                    score.changePoints(name, 10);
                    System.out.println("GOT SOME POINTS");
                    break;
                }

                score.changePoints(name, -5);
            }

        }
        System.out.println("OUT OF GAME");
    }

    @Override
    public void setRounds(int rounds) {
        this.rounds = rounds;
    }
}
