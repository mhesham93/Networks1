package com.towson.networks;



public class Main {

    public static void main(String[] args) {
        SocketServer ss = new SocketServer("localhost",12331);
        ss.startServer();
        WebRequestManager wrm = new WebRequestManager("www.example.com",80,"localhost",12331);

        wrm.Process();
        
    }
}
