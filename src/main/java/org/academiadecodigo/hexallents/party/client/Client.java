package org.academiadecodigo.hexallents.party.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * Client for the game
 */
public class Client {

    private Socket socket;
    private PrintWriter serverOutput;
    private BufferedReader serverIn;
    private ExecutorService singleExecutor;


    /**
     * Asks host ip address and creates socket connection
     *
     * @throws IOException
     */
    public void chat() throws IOException {


        System.out.println("Insert host ip address");
        String ip = answerReader();

        socket = new Socket(ip, 7070);
        serverOutput = new PrintWriter(socket.getOutputStream(), true);


        singleExecutor = Executors.newSingleThreadExecutor();
        singleExecutor.submit(new Listen());

        while(!singleExecutor.isShutdown()) {
            String message = answerReader();
            serverOutput.println(message);
            if(message.equals("exit")){
                socket.close();
                singleExecutor.shutdownNow();
                return;
            }
        }


    }

    private String answerReader() {
        Scanner scanner = new Scanner(System.in);
        return scanner.nextLine();
    }

    private class Listen implements Runnable {

        @Override
        public void run() {

            String message = "";
            StringBuilder initialMessage = new StringBuilder();
            try {
                while (!singleExecutor.isShutdown()) {

                    serverIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                    while ((message = serverIn.readLine()) != null) {
                        initialMessage.append(message);
                        System.out.println(message);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
