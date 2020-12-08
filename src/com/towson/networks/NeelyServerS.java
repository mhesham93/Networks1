package com.towson.networks;

import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class NeelyServerS implements Runnable{
    Thread TCPThread;
    Thread UDPThread;
    private ServerSocket serverSocket;
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;
    private DatagramSocket dataGramSocket;
    private final String RemoteServer;
    private final String LocalServer;
    private final int RemotePort;
    private final int LocalPort;
    private String RemoteServerResponse;

    public NeelyServerS(String remoteServer,int remotePort,String localServer,int localPort){
        RemoteServer = remoteServer;
        LocalServer = localServer;
        RemotePort = remotePort;
        LocalPort = localPort;

        try {
            dataGramSocket = new DatagramSocket(localPort);
        } catch (SocketException e) {
            System.out.println(e.getMessage());
        }

        start();
    }

    public void Process(){
        GetResponseFromRemoteServer();
        PassDataToLocalWebServer();
    }

    @Override
    public void run() {
        startThread();
    }

    private void startTCP(){

    }

    private void startThread(){

        try{
            serverSocket = new ServerSocket(LocalPort);
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

    public void start() {
        System.out.println("Server Thread started");
        if (TCPThread == null) {
            TCPThread = new Thread(this, "TCPThread");
            TCPThread.start();
        }

        System.out.println("UDP Server Thread started");
        if (UDPThread == null) {
            UDPThread = new Thread(this, "UDPThread");
            UDPThread.start();
        }
    }

    private void GetResponseFromRemoteServer()  {
        try{
            StringBuilder sb = new StringBuilder();
            Socket socket = new Socket(RemoteServer,RemotePort);
            PrintStream out = new PrintStream( socket.getOutputStream() );
            BufferedReader in = new BufferedReader(new InputStreamReader( socket.getInputStream()));
            out.println("GET /index.html HTTP/1.0\r\n\r");
            out.println("Accept-Language: en-US,en;q=0.5");
            out.println("Accept-Encoding: gzip, deflate");
            out.println("User-Agent: Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:83.0) Gecko/20100101 Firefox/83.0");

            out.flush();
            String lineReader;
            while ((lineReader = in.readLine()) != null) {
                sb.append(lineReader).append("\n");
            }
            System.out.println();
            System.out.println("C: message received from Webserver W");
            in.close();
            socket.close();
            out.close();
            RemoteServerResponse = sb.toString();
            System.out.println("Socket Closed");

        }catch(IOException ex)
        {
            System.out.println(ex.getMessage());
        }

    }

    private void PassDataToLocalWebServer()  {

        NeelyClientC c = new NeelyClientC("C",LocalServer,LocalPort);
        c.SendMessage(RemoteServerResponse);

        NeelyClientC1 c1 = new NeelyClientC1("C1",LocalServer,LocalPort);
        SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
        Date date = new Date(System.currentTimeMillis());

        String message = String.format("SYSTEM TIME IS %s",formatter.format(date));
        c1.SendMessage(message);
//

    }
}
