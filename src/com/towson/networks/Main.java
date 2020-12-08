package com.towson.networks;



public class Main {

    public static void main(String[] args) {


        NeelyServerS s = new NeelyServerS("www.example.com",80,"localhost",12331);
        s.Process();
        
    }
}
