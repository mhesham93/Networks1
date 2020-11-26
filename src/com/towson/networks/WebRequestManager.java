package com.towson.networks;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

public class WebRequestManager {

    private InetAddress address;
    private String RemoteServer;
    private String LocalServer;
    private int RemotePort;
    private int LocalPort;
    private String RemoteServerResponse;

    public WebRequestManager(String remoteServer,int remotePort,String localServer,int localPort)
    {
        RemoteServer = remoteServer;
        LocalServer = localServer;
        RemotePort = remotePort;
        LocalPort = localPort;

    }

    public void Process(){
        GetResponseFromRemoteServer();
        PassDataToLocalWebServer();
    }


    private void GetResponseFromRemoteServer()  {
        try{
            StringBuilder sb = new StringBuilder();
            Socket socket = new Socket(RemoteServer,RemotePort);
            PrintStream out = new PrintStream( socket.getOutputStream() );
            BufferedReader in = new BufferedReader(new InputStreamReader( socket.getInputStream()));
            out.print("GET /index.html HTTP/1.0\r\n\r\n");
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

        Client c = new Client("C",LocalServer,LocalPort);
        c.SendMessage(RemoteServerResponse);

        Client c1 = new Client("C1",LocalServer,LocalPort);
        SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
        Date date = new Date(System.currentTimeMillis());

        String message = String.format("SYSTEM TIME IS %s",formatter.format(date));
        c1.SendMessage(message);
//

    }


}
