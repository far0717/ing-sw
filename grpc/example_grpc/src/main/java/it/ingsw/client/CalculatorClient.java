package it.ingsw.client;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
import it.ingsw.*;

public class CalculatorClient {

    public static void main(String[] args) throws InterruptedException {

        // Creazione del canale verso il server
        ManagedChannel channel = ManagedChannelBuilder
                .forAddress("localhost", 50051)
                .usePlaintext()
                .build();

        // Ottenimento dello Stub (= il PROXY REMOTO)
        // Da questo momento il client usa 'stub' come se fosse il servizio locale.
        CalculatorGrpc.CalculatorBlockingStub stub =
                CalculatorGrpc.newBlockingStub(channel);

        // Costruzione della richiesta
        OperationRequest request = OperationRequest
                .newBuilder()
                .setOperandA(15.0)
                .setOperandB(4.0)
                .build();

        try {


            RubricaGrpc.RubricaBlockingStub rubricaStub = RubricaGrpc.newBlockingStub(channel);
            ContactList cl = rubricaStub.getAll(null);
            System.out.println("Rubrica vuota: " + cl);

            rubricaStub.addContact(
                    Person.newBuilder()
                            .setName("Mario")
                            .setId(1)
                            .setEmail("mario.rossi@example.it")
                            .addPhones(Person.PhoneNumber.newBuilder()
                                    .setNumber("33333333322")
                                    .setType(Person.PhoneType.PHONE_TYPE_WORK)
                                    .build()
                            )
                            .build());
            System.out.println("Rubrica con Mario: " + rubricaStub.getAll(null).toString());

            NomeContatto nc = NomeContatto.newBuilder().setNomeContatto("Ma").build();
            System.out.println("Ricerca Contatto: " + rubricaStub.getContact(nc).toString());







            // Chiamata al PROXY - internamente serializza, invia, attende, deserializza
            System.out.println("=== Test operazioni remota via gRPC ===\n");

            OperationResponse addResult = stub.add(request);
            System.out.println("Somma:       " + addResult.getDescription());

            OperationResponse subResult = stub.subtract(request);
            System.out.println("Differenza:  " + subResult.getDescription());

            OperationResponse mulResult = stub.multiply(request);
            System.out.println("Prodotto:    " + mulResult.getDescription());

            OperationResponse divResult = stub.divide(request);
            System.out.println("Quoziente:   " + divResult.getDescription());

            // Test divisione per zero
            System.out.println("\n=== Test errore: divisione per zero ===");
            OperationRequest badRequest = OperationRequest.newBuilder()
                    .setOperandA(10.0)
                    .setOperandB(0.0)
                    .build();
            stub.divide(badRequest); // deve lanciare eccezione

        } catch (StatusRuntimeException e) {
            System.err.println("Errore ricevuto dal server: " + e.getStatus());
        } finally {
            channel.shutdown();
        }
    }
}

