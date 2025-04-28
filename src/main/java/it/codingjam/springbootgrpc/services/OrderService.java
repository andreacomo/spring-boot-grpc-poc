package it.codingjam.springbootgrpc.services;

import it.codingjam.springbootgrpc.services.dtos.OrderDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class OrderService {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderService.class);

    private final Random random = new Random();

    public UUID createOrder(OrderDTO order) throws InterruptedException {
        int timeout = random.nextInt(500 - 200) + 200;
        LOGGER.info("Creating order in {}ms", timeout);
        TimeUnit.MILLISECONDS.sleep(timeout);
        return UUID.randomUUID();
    }
}
