package org.academiadecodigo.hexallents.party.server;

import org.academiadecodigo.bootcamp.Prompt;
import org.academiadecodigo.bootcamp.scanners.menu.MenuInputScanner;
import org.academiadecodigo.bootcamp.scanners.string.StringInputScanner;
import org.academiadecodigo.hexallents.party.messages.Messages;
import org.academiadecodigo.hexallents.party.server.games.CardsAgainstHumanity;
import org.academiadecodigo.hexallents.party.server.games.FastestAnswer;
import org.academiadecodigo.hexallents.party.server.games.Game;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {

    private ServerSocket serverSocket;
    private Map<String, PlayerWorker> playerWorkerMap;
    private static final int MAX_PLAYERS = 2;
    private final int PORT_NUMBER = 7070;
    public static final int ROUNDS = 6;
    private ExecutorService executor;
    private boolean gameRunning;
    private String answer = "";
    private Game game;

    public Server() {

        try {
            serverSocket = new ServerSocket(PORT_NUMBER);
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }

        executor = Executors.newFixedThreadPool(MAX_PLAYERS);

        playerWorkerMap = new HashMap<>();
    }


    public static void main(String[] args) {

        Server server = new Server();
        List<String> players = server.getPlayerNames();
        Score score = new Score(players);
        Game game = new FastestAnswer(score, server, ROUNDS);
        server.setGame(game);

        try {
            server.listen();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Game game2 = new CardsAgainstHumanity(score, server, ROUNDS);

        server.sendAll(Messages.clearScreen().toString());
        Messages.gameMessage();
        game.load();
        server.sendAll(Messages.clearScreen().toString());
        try {
            game.start();
            game2.start();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void listen() throws IOException {

        Socket socket = serverSocket.accept();
        PlayerWorker playerWorker = new PlayerWorker(socket);

        playerWorker.send("What is your name?");
        String name = playerWorker.read();
        playerWorker.setName(name);

        playerWorkerMap.put(playerWorker.name, playerWorker);
        executor.submit(playerWorker);

        if (playerWorkerMap.size() < MAX_PLAYERS) {
            sendAll("There are " + playerWorkerMap.size() + " players. Wait for more players");
            listen();
        }

    }

    public void endGame() {
        try {
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        executor.shutdown();
    }

    private void makeThreadsWait() {
        synchronized (this) {
            while (!gameRunning) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            notifyAll();
        }

    }

    public void setGame(Game game) {
        this.game = game;
    }

    public void sendAll(String string) {

        for (PlayerWorker p : playerWorkerMap.values()) {

            p.send(string);
        }
    }

    public void sendMenuPrompt(MenuInputScanner menuInputScanner, String playerName) {
        String menuPrompt;

        PlayerWorker p = playerWorkerMap.get(playerName);

        menuPrompt = p.useMenuPrompt(menuInputScanner);

        setAnswer(menuPrompt);
    }

    public void sendStringPrompt(StringInputScanner stringInputScanner, String playerName) {

        PlayerWorker p = playerWorkerMap.get(playerName);

        String stringPrompt = p.useStringPrompt(stringInputScanner);

        setAnswer(stringPrompt);

    }


    public List<String> getPlayerNames() {

        ArrayList<String> list = new ArrayList<>();

        list.addAll(playerWorkerMap.keySet());

        return list;
    }

    public void setGameRunning(boolean gameRunning) {
        this.gameRunning = gameRunning;
    }

    public void setAnswer(String answer) {
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
        private Prompt prompt;

        private PlayerWorker(Socket socket) {

            this.socket = socket;
            try {
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream());
                prompt = new Prompt(socket.getInputStream(), new PrintStream(socket.getOutputStream()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        @Override
        public void run() {
            makeThreadsWait();
            System.out.println("GAME STARTED");
            inGame();
        }

        private void inGame() {

            StringBuilder userInput = new StringBuilder();
            System.out.println("INGAME");

            while (true) {
                userInput.append(name + ":" + read());
                System.out.println("NO INGAME: " + userInput.toString());
                setAnswer(userInput.toString());
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

        /**
         * @param stringInputScanner
         * @return the string input from the user
         */

        public String useStringPrompt(StringInputScanner stringInputScanner) {
            return name + ": " + prompt.getUserInput(stringInputScanner);
        }

        /**
         * @param menuInputScanner
         * @return the int which corresponds to the chosen option
         */
        public String useMenuPrompt(MenuInputScanner menuInputScanner) {
            return name + ": " + prompt.getUserInput(menuInputScanner);
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