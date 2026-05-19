package it.ingsw.server;

import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import it.ingsw.CalculatorGrpc;
import it.ingsw.OperationRequest;
import it.ingsw.OperationResponse;

/**
 * Real Subject nel pattern Remote Proxy.
 * Estende lo Skeleton generato da protoc (CalculatorGrpc.CalculatorImplBase).
 */
public class CalculatorServiceImpl extends CalculatorGrpc.CalculatorImplBase {

    @Override
    public void add(OperationRequest req, StreamObserver<OperationResponse> resp) {
        double result = req.getOperandA() + req.getOperandB();
        OperationResponse response = OperationResponse.newBuilder()
                .setResult(result)
                .setDescription(req.getOperandA() + " + " + req.getOperandB() + " = " + result)
                .build();
        resp.onNext(response);
        resp.onCompleted();
    }

    @Override
    public void subtract(OperationRequest req, StreamObserver<OperationResponse> resp) {
        double result = req.getOperandA() - req.getOperandB();
        OperationResponse response = OperationResponse.newBuilder()
                .setResult(result)
                .setDescription(req.getOperandA() + " - " + req.getOperandB() + " = " + result)
                .build();
        resp.onNext(response);
        resp.onCompleted();
    }

    @Override
    public void multiply(OperationRequest req, StreamObserver<OperationResponse> resp) {
        double result = req.getOperandA() * req.getOperandB();
        OperationResponse response = OperationResponse.newBuilder()
                .setResult(result)
                .setDescription(req.getOperandA() + " * " + req.getOperandB() + " = " + result)
                .build();
        resp.onNext(response);
        resp.onCompleted();
    }

    @Override
    public void divide(OperationRequest req, StreamObserver<OperationResponse> resp) {
        if (req.getOperandB() == 0.0) {
            // Propagazione di errore tramite gRPC Status
            resp.onError(Status.INVALID_ARGUMENT
                    .withDescription("Divisione per zero non ammessa")
                    .asRuntimeException());
            return;
        }
        double result = req.getOperandA() / req.getOperandB();
        OperationResponse response = OperationResponse.newBuilder()
                .setResult(result)
                .setDescription(req.getOperandA() + " / " + req.getOperandB() + " = " + result)
                .build();
        resp.onNext(response);
        resp.onCompleted();
    }
}
