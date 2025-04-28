package it.codingjam.springbootgrpc.grpc;

import io.grpc.stub.StreamObserver;
import it.condingjam.springbootgrpc.proto.hello.GreeterServiceGrpc;
import it.condingjam.springbootgrpc.proto.hello.HelloReply;
import it.condingjam.springbootgrpc.proto.hello.HelloRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.grpc.server.service.GrpcService;

@GrpcService
public class RemoteGreeterService extends GreeterServiceGrpc.GreeterServiceImplBase {

    private static final Logger LOGGER = LoggerFactory.getLogger(RemoteGreeterService.class);

    @Override
    public void sayHello(HelloRequest request, StreamObserver<HelloReply> responseObserver) {
        LOGGER.info("Hello {}", request.getName());
        if (request.getName().startsWith("error")) {
            throw new IllegalArgumentException("Bad name: " + request.getName());
        }
        if (request.getName().startsWith("internal")) {
            throw new RuntimeException("Internal error when saying hello");
        }
        HelloReply reply = HelloReply.newBuilder().setMessage("Hello ==> " + request.getName()).build();
        responseObserver.onNext(reply);
        responseObserver.onCompleted();
    }

    @Override
    public void streamHello(HelloRequest request, StreamObserver<HelloReply> responseObserver) {
        LOGGER.info("Hello streaming {}", request.getName());
        LOGGER.info("Thread info:\nGroup name: {}\nActive count: {}\nIs virtual: {}",
                Thread.currentThread().getThreadGroup().getName(),
                Thread.currentThread().getThreadGroup().activeCount(),
                Thread.currentThread().isVirtual()
                );
        Thread.ofVirtual().name("streaming")
                .start(() -> {
                    int count = 0;
                    while (count < 10) {
                        LOGGER.info("Emitting message {}. On virtual thread? {}", count, Thread.currentThread().isVirtual());
                        HelloReply reply = HelloReply.newBuilder().setMessage("Hello(" + count + ") ==> " + request.getName()).build();
                        responseObserver.onNext(reply);
                        count++;
                        try {
                            Thread.sleep(1000L);
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                            responseObserver.onError(e);
                            return;
                        }
                    }
                    responseObserver.onCompleted();
                });
        LOGGER.info("Executing body async");
    }
}
