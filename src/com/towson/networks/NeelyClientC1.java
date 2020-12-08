package com.towson.networks;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class NeelyClientC1 {
    private int Port;
    private String Server;
    private String ClientName;
    public NeelyClientC1(String clientName,String server,int port)
    {
        Port = port;
        Server = server;
        ClientName = clientName;
    }
    protected boolean SendMessage(String message){
        try{
            DatagramSocket ds = new DatagramSocket();
            String updatedmsg = ClientName + ": "+ message;
            InetAddress ip = InetAddress.getByName(Server);
            byte buf[] = null;
            buf = updatedmsg.getBytes();
            DatagramPacket DpSend =  new DatagramPacket(buf, buf.length, ip, Port);
            ds.send(DpSend);
            System.out.println(String.format("%s: Data Sent",ClientName));
            if (ClientName == "C1")
            {
                buf = new byte[256];
                DatagramPacket responsePacket = new DatagramPacket(buf, buf.length);
                ds.receive(responsePacket);

                String received = new String(responsePacket.getData(), 0, responsePacket.getLength());

                System.out.println();
                System.out.println("C1: " + received);
            }
            return true;
        }catch(IOException ex)
        {
            System.out.println(ex.getMessage());
            return false;
        }
    }
}
