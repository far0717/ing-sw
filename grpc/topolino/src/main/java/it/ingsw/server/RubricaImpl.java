package it.ingsw.server;

import io.grpc.stub.StreamObserver;
import it.ingsw.proto.*;
import java.util.concurrent.ConcurrentHashMap;

public class RubricaImpl extends RubricaGrpc.RubricaImplBase {

    private ConcurrentHashMap<String, Person> contacts = new ConcurrentHashMap<>();

    @Override
    public void addContact(Person request, StreamObserver<Result> responseObserver) {

        Person person = contacts.put(request.getNome(), request);
        if (person == null){
            responseObserver.onNext(null);
            responseObserver.onCompleted();
            return;
        }
        Result res = Result.newBuilder().setCreato(true).build();
        responseObserver.onNext(res);
        responseObserver.onCompleted();

    }

    @Override
    public void getContact(NomeContatto request, StreamObserver<Person> responseObserver) {
        String nome = request.getNomeContatto();

        /*
        contacts.keys().asIterator().forEachRemaining(nomeContatto ->{
            if(nomeContatto.contains(nome)){
                Person p = contacts.get(nomeContatto);
                responseObserver.onNext(p);
                responseObserver.onCompleted();
            }
        });*/

        Person res = contacts.get(nome);
        if(res == null)
            res = Person.newBuilder().build();
        responseObserver.onNext(res);
        responseObserver.onCompleted();
    }

    @Override
    public void getAll(ListRequest request, StreamObserver<ContactList> responseObserver) {
        ContactList res = ContactList.newBuilder()
                .addAllPerson(contacts.values())
                .build();
        responseObserver.onNext(res);
        responseObserver.onCompleted();
    }
}
