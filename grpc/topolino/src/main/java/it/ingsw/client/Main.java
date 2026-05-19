package it.ingsw.client;

import com.google.cloud.location.ListLocationsRequest;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import it.ingsw.proto.*;

public class Main {
    public static void main(String[] args) {
        ManagedChannel channel = ManagedChannelBuilder
                .forAddress("localhost", 50010)
                .usePlaintext()
                .build();

        CalculatorGrpc.CalculatorBlockingStub stub =
                CalculatorGrpc.newBlockingStub(channel);

        OperationRequest request = OperationRequest.newBuilder()
                .setOpA(1)
                .setOpB(2)
                .build();
        OperationResponse or = stub.add(request);
        System.out.println("Risultato addizione:" + or.getRes());

        RubricaGrpc.RubricaBlockingStub rubricaStub = RubricaGrpc.newBlockingStub(channel);


        ContactList lr = rubricaStub.getAll(null);
        System.out.println("LA MIA RUBRICA:" + lr);


        channel.shutdown();
    }
}