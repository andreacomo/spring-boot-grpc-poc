package it.codingjam.springbootgrpc.grpc;

import io.grpc.stub.StreamObserver;
import it.codingjam.springbootgrpc.services.OrderService;
import it.codingjam.springbootgrpc.services.dtos.OrderDTO;
import it.condingjam.springbootgrpc.proto.orders.OrderRequest;
import it.condingjam.springbootgrpc.proto.orders.OrderResponse;
import it.condingjam.springbootgrpc.proto.orders.OrderServiceGrpc;
import jakarta.annotation.PreDestroy;
import org.springframework.grpc.server.service.GrpcService;

import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@GrpcService
public class RemoteOrderService extends OrderServiceGrpc.OrderServiceImplBase {

    private final ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor();

    private final OrderService orderService;

    public RemoteOrderService(OrderService orderService) {
        this.orderService = orderService;
    }

    @Override
    public void placeOrder(OrderRequest request, StreamObserver<OrderResponse> responseObserver) {
        executor.submit(() -> {
            try {
                UUID orderId = orderService.createOrder(OrderDTO.from(request));
                responseObserver.onNext(OrderResponse
                        .newBuilder()
                        .setId(orderId.toString())
                        .build()
                );
                responseObserver.onCompleted();
            } catch (InterruptedException e) {
                responseObserver.onError(e);
            }
        });
    }

    @PreDestroy
    void close() throws InterruptedException {
        executor.shutdown();
        executor.awaitTermination(10, TimeUnit.SECONDS);
    }
}
