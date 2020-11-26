package com.towson.networks;


import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;


public class UDPServer implements Runnable {

    private DatagramSocket dataGramSocket;
    private Thread currentThread;
    private int Port;

    public UDPServer(int port)
    {
        Port = port;
        // Step 1 : Create a socket to listen at port 1234
        try {
            dataGramSocket = new DatagramSocket(Port);
        } catch (SocketException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void run() {
        try{

            byte[] receive = new byte[65535];
            byte[] sent = new byte [65535];
            DatagramPacket DpReceive = null;
            while (true)
            {
                DpReceive = new DatagramPacket(receive, receive.length);
                dataGramSocket.receive(DpReceive);
                String receivedData = data(receive).toString();
                String ClientName = receivedData.substring(0,2);
                System.out.println("Server Received from " + ClientName + receivedData.substring(2,data(receive).length()));
                if (ClientName.equals("C1") && receivedData.contains("SYSTEM TIME IS"))
                {
                    String response = "TIME ACK";
                    sent = response.getBytes();
                    DatagramPacket dpSend = new DatagramPacket(sent,sent.length,DpReceive.getAddress(),DpReceive.getPort());
                    dataGramSocket.send(dpSend);
                    System.out.println("Server sent back to " + ClientName + ": " + response);
                }
                if (receivedData.equals("."))
                {
                    System.out.println("Client closed connection.");
                    break;
                }

                receive = new byte[65535];
                sent = new byte[65535];

            }
        }
        catch(Exception ex)
        {
          System.out.println(ex.getMessage());
        }
    }

    private static StringBuilder data(byte[] a)
    {
        if (a == null)
            return null;
        StringBuilder ret = new StringBuilder();
        int i = 0;
        while (a[i] != 0)
        {
            ret.append((char) a[i]);
            i++;
        }
        return ret;
    }

    public void start(){
        System.out.println("UDP Server Thread started");
        if (currentThread == null) {
            currentThread = new Thread(this, "UDPServer");
            currentThread.start();
        }
    }

    public void stop(){
        dataGramSocket.close();
        System.out.println("UDP Server Thread stopped");
        System.out.println("UDP Server stopped");
    }
}
