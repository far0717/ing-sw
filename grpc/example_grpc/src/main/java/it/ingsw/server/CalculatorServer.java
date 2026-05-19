package it.ingsw.server;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import it.ingsw.Person;

import java.io.IOException;

public class CalculatorServer {

    private static final int PORT = 50051;

    public static void main(String[] args) throws IOException, InterruptedException {
        Server server = ServerBuilder
                .forPort(PORT)
                .addService(new CalculatorServiceImpl())
                .addService(new RubricaImpl())
                .build()
                .start();

        System.out.println("Server gRPC avviato sulla porta " + PORT);
        System.out.println("In ascolto di chiamate remote...");

        // Chiusura elegante all'interrupt
        Runtime.getRuntime().addShutdownHook(new Thread(server::shutdown));
        server.awaitTermination();
    }
}
