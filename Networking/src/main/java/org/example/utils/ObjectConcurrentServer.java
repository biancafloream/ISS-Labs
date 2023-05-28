package org.example.utils;

import org.example.IService;
import org.example.objectproto.ClientObjectWorker;

import java.net.Socket;

public class ObjectConcurrentServer extends AbsConcurrentServer {
    private IService server;
    public ObjectConcurrentServer(int port, IService server) {
        super(port);
        this.server = server;
        System.out.println("ObjectConcurrentServer");
    }

    @Override
    protected Thread createWorker(Socket client) {
        ClientObjectWorker worker=new ClientObjectWorker(server, client);
        return new Thread(worker);
    }

}
