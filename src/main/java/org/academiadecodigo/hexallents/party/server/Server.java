package org.academiadecodigo.hexallents.party.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {

    private ServerSocket serverSocket;
    private Set<PlayerWorker> playerWorkerSet;
    private static final int MAX_PLAYERS = 2;
    private final int PORT_NUMBER = 7070;
    private ExecutorService executor;
    private boolean gameRunning;
    private String answer = "";

    public Server() {

        try {
            serverSocket = new ServerSocket(PORT_NUMBER);
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }

        executor = Executors.newFixedThreadPool(MAX_PLAYERS);

        playerWorkerSet = new HashSet<>();
    }


    public static void main(String[] args) {

        Server server = new Server();

        try {
            server.listen();
        } catch (IOException e) {
            e.printStackTrace();
        }

        List<String> players = server.getPlayerNames();

        Score score = new Score(players);
        Game game = new FastestAnswer(score, server);
        game.setRounds(5);
        game.load();
        try {
            game.start();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void listen() throws IOException {

        Socket socket = serverSocket.accept();
        PlayerWorker playerWorker = new PlayerWorker(socket);

        playerWorkerSet.add(playerWorker);

        
        playerWorker.send("What is your name?");
        String name = playerWorker.read();
        playerWorker.setName(name);
        executor.submit(playerWorker);

        if (playerWorkerSet.size() < MAX_PLAYERS) {
            sendAll("There are " + playerWorkerSet.size() + " players. Wait for more players");
            listen();
        }

    }

    public void endGame(){

        try {
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        executor.shutdown();
    }

    public void sendAll(String string) {

        for (PlayerWorker p : playerWorkerSet) {

            p.send(string);
        }
    }

    public List<String> getPlayerNames() {

        ArrayList<String> list = new ArrayList<>();

        for (PlayerWorker p : playerWorkerSet) {
            list.add(p.toString());
        }

        return list;
    }

    public void setGameRunning(boolean gameRunning) {
        this.gameRunning = gameRunning;
    }

    public void setAnswer(String answer){
        System.out.println("NO SET: " + answer);
        this.answer = answer;
    }

    public String getAnswer() {
        String sent = answer;
        answer = "";
        return sent;
    }

    private class PlayerWorker implements Runnable {

        private final Socket socket;
        private BufferedReader in;
        private PrintWriter out;
        private String name;

        private PlayerWorker(Socket socket) {

            this.socket = socket;
            try {
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        @Override
        public void run() {
            
            StringBuilder input = new StringBuilder();

            //Before game starts
            while (true) {
/*
                input.append(name + ": " + read());
                sendAll(input.toString());
                input.delete(0, input.length());
*/

                if (gameRunning) {
                    //sendAll("PRESS START TO BEGIN ANSWERING");
                    inGame();
                }
            }
        }

        private void inGame() {

            StringBuilder userInput = new StringBuilder();
            System.out.println("INGAME");

            while (true) {
                userInput.append(name + ":" + read());
                System.out.println("NO INGAME: " + userInput.toString());
                setAnswer(userInput.toString());
                //notify();
                userInput.delete(0, userInput.length());
            }
        }

        private void closeSocket() {
            try {
                out.println("The end!");
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private void send(String string) {

            out.println(string);
            out.flush();
        }

        private String read() {

            try {
                return in.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return "";
        }

        @Override
        public String toString() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}