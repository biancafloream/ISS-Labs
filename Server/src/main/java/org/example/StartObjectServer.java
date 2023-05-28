package org.example;

import org.example.utils.AbstractServer;
import org.example.utils.ObjectConcurrentServer;
import org.example.utils.ServerException;

import java.io.IOException;
import java.util.Properties;

public class StartObjectServer {
    private static int defaultPort = 57864;

    public StartObjectServer() {
    }

    public static void main(String[] args) {
        Properties serverProps = new Properties();

        RepoBooks repoBooks = new RepoBooks();
        RepoBorrowing repoBorrowings = new RepoBorrowing();
        RepoReader repoReaders = new RepoReader();
        RepoTerminal repoTerminals = new RepoTerminal();

        IService service = new ServiceImpl(repoBooks, repoBorrowings, repoReaders, repoTerminals ,"127.0.0.1" , defaultPort);

        System.out.println("Starting server on port: " + defaultPort);
        AbstractServer server = new ObjectConcurrentServer(defaultPort, service);

        try {
            server.start();

        } catch (ServerException var8) {
            System.err.println("Error starting the server" + var8.getMessage());
        }

    }
}
