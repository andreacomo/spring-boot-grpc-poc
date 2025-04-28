package it.codingjam.springbootgrpc.rest;

import it.codingjam.springbootgrpc.services.OrderService;
import it.codingjam.springbootgrpc.services.dtos.OrderDTO;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/orders")
public class OrderResource {

    private final OrderService orderService;

    public OrderResource(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public OrderId createOrder(@RequestBody OrderDTO order) throws InterruptedException {
        UUID uuid = orderService.createOrder(order);
        return new OrderId(uuid.toString());
    }

    public record OrderId(String value) {}
}
