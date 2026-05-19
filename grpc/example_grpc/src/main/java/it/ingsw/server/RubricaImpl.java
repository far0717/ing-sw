package it.ingsw.server;

import io.grpc.stub.StreamObserver;
import it.ingsw.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class RubricaImpl extends RubricaGrpc.RubricaImplBase {

    Map<String , Person> rubrica = new HashMap<>();

    @Override
    public void addContact(Person request, StreamObserver<Result> responseObserver) {
        Person result = rubrica.put(request.getName(), request);
        Result res = Result.newBuilder().setResult(false).setMesage("Errore").build();
        if (result!=null)
            res = Result.newBuilder().setResult(true).setMesage("Persona aggiunta con successo ai contatti").build();

        responseObserver.onNext(res);
        responseObserver.onCompleted();
    }

    @Override
    public void getContact(NomeContatto request, StreamObserver<Person> responseObserver) {
        Set<String> nomi = rubrica.keySet();
        nomi.forEach(n -> {
            if (n.contains(request.getNomeContatto())){
                responseObserver.onNext(rubrica.get(n));
                responseObserver.onCompleted();
            }
        });
        responseObserver.onNext(Person.newBuilder().build());
        responseObserver.onCompleted();
    }

    @Override
    public void getAll(ListRequest request, StreamObserver<ContactList> responseObserver) {
        ContactList res = ContactList.newBuilder().addAllListaContatti(rubrica.values()).build();

        responseObserver.onNext(res);
        responseObserver.onCompleted();
    }
}
