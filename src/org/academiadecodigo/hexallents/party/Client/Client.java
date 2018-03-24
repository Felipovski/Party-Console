package org.academiadecodigo.hexallents.party.Client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class Client {

    private Socket socket;
    private PrintWriter serverOutput; // envia mensagem para o servidor
    private BufferedReader serverIn; // recebe mensagem do servidor
    private ExecutorService singleExecutor;

    public void chat() throws IOException {


        socket = new Socket("192.168.7.249", 9000);
        serverOutput = new PrintWriter(socket.getOutputStream(), true);


        singleExecutor = Executors.newSingleThreadExecutor();
        singleExecutor.submit(new Listen());

        while(!singleExecutor.isShutdown()) {
            String message = writeSomething();
            serverOutput.println(message);
            if(message.equals("exit")){
                socket.close();
                singleExecutor.shutdownNow();
                return;
            }
        }


    }

    private String writeSomething() {
        Scanner scanner = new Scanner(System.in);
        return scanner.nextLine();
    }

    private class Listen implements Runnable {

        @Override
        public void run() {

            try {
                while (!singleExecutor.isShutdown()) {

                    serverIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    String message = serverIn.readLine();
                    System.out.println(message);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
