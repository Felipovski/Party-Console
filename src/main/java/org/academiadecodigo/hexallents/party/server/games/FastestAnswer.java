package org.academiadecodigo.hexallents.party.server.games;

import org.academiadecodigo.hexallents.party.messages.Messages;
import org.academiadecodigo.hexallents.party.server.Score;
import org.academiadecodigo.hexallents.party.server.Server;

import java.io.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Class Fastest Answer game
 */
public class FastestAnswer extends AbstractGame{

    private String[] questions;
    private String[] answers;
    private long time;

    public FastestAnswer(Score score, Server server, int rounds) {
        super(score, server, rounds);
    }

    /**
     * Loads file data with question and answers
     */
    @Override
    public void load() {
        File document = new File("src/main/resources/fastest-answer/fastestAnswers.txt");

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
        for (String s : answers){
            System.out.println(s);
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


    /**
     * Starts the game
     *
     * @throws InterruptedException
     */
    @Override
    public void start() throws InterruptedException {

        server.sendAll(Messages.fastestAnswerInitialMessage().toString());
        server.setGameRunning(true);
        server.sendAll(Messages.clearScreen().toString());


        for (int i = 0; i < questions.length; i++) {

            server.sendAll(Messages.clearScreen().toString());
            server.sendAll(Messages.fastestAnswerInitialMessage().toString());
            server.sendAll("<---------------CURRENT SCORE--------------->\n\n" + score.toString());
            server.sendAll("\nQuestion " + (i+1) + ": " + questions[i]);

            time = System.currentTimeMillis();
            //Handles the answers from player
            answersHandler(i);
        }

        server.sendAll(Messages.clearScreen().toString());
        server.sendAll(Messages.fastestAnswerInitialMessage().toString());
        server.sendAll("<--------------------Final score-------------------->\n\n" + score.toString());
        server.endGame();
    }

    private void answersHandler(int index){

        StringBuilder answer = new StringBuilder();
        while (true) {
            /*try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }*/

            synchronized (this) {
                answer.append(server.getAnswer());

                System.out.println(answer.toString());

                if (System.currentTimeMillis() - time >= 15000) {
                    server.sendAll("Time's up!!!");
                    return;
                }
                System.out.println(answer.toString() + "HHHHEYEYEYEY");

                if (answer.toString().equals("")) {
                    answer.delete(0, answer.length());
                    continue;
                }

                System.out.println("NONONONONONONONO");

                System.out.println(answer.substring(answer.indexOf(":") + 1, answer.length()));
                System.out.println(answer.substring(0, answer.indexOf(":")));


                if (answers[index].equals(answer.substring(answer.indexOf(":") + 1, answer.length()))) {
                    score.changeScore(answer.substring(0, answer.indexOf(":")), 10);
                    answer.delete(0, answer.length());
                    break;
                }

                score.changeScore(answer.substring(0, answer.indexOf(":")), -5);
                answer.delete(0, answer.length());
            }

        }
    }

}
