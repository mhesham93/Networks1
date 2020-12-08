package com.towson.networks;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class NeelyClientC {
    private int Port;
    private String Server;
    private String ClientName;
    public NeelyClientC(String clientName,String server,int port)
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

            return true;
        }catch(IOException ex)
        {
            System.out.println(ex.getMessage());
            return false;
        }
    }
}
