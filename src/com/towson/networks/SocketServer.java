package com.towson.networks;

public class SocketServer {
    private String url;
    private int port;
    private TCPServer tcpServer;
    private UDPServer udpServer;
    public SocketServer(String Url,int Port)
    {
        port = Port;
        url = Url;
        tcpServer = new TCPServer(url,port);
        udpServer = new UDPServer(port);
    }

    public void startServer(){
        tcpServer.start();
        udpServer.start();

    }

    public void stopServer() {
       tcpServer.stop();
       udpServer.stop();
    }
}
