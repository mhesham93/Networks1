package com.towson.networks;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPServer implements Runnable {

    Thread currentThread;
    private int port;
    private ServerSocket serverSocket;
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;

    TCPServer(String Url,int Port) {
        port = Port;
    }

    @Override
    public void run() {
        try{
            serverSocket = new ServerSocket(port);
            clientSocket = serverSocket.accept();
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                if (".".equals(inputLine)) {
                    break;
                }
                out.println(inputLine);
            }
        }catch(IOException ex)
        {
            System.out.println(ex.getMessage());
        }
    }

    public void start() {
        System.out.println("TCP Server Thread started");
        if (currentThread == null) {
            currentThread = new Thread(this, "TCPServer");
            currentThread.start();
        }
    }

    public void stop(){
        try {
            in.close();
            out.close();
            clientSocket.close();
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("TCP Server Thread stopped");
        System.out.println("TCP Server stopped");
    }

}
