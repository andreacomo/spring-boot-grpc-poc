package it.codingjam.springbootgrpc.grpc.errors;

import io.grpc.Status;
import io.grpc.StatusException;
import org.springframework.grpc.server.exception.GrpcExceptionHandler;
import org.springframework.stereotype.Component;

@Component
public class ExceptionHandler implements GrpcExceptionHandler {

    @Override
    public StatusException handleException(Throwable exception) {
        return switch (exception) {
            case IllegalArgumentException ignored -> Status.FAILED_PRECONDITION
                    .withDescription(exception.getMessage())
                    .asException();
            default -> Status.INTERNAL
                    .withDescription(exception.getMessage())
                    .asException();
        };
    }
}
