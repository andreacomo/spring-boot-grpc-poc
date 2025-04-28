package it.codingjam.springbootgrpc.services.dtos;

import it.condingjam.springbootgrpc.proto.orders.OrderRequest;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.List;

public record OrderDTO(
        String customerId,
        OffsetDateTime createdAt,
        List<ArticleDTO> articles,
        PaymentMethod paymentMethod) {

    public static OrderDTO from(OrderRequest src) {
        return new OrderDTO(
                src.getCustomerId(),
                OffsetDateTime.ofInstant(Instant.ofEpochSecond(src.getCreatedAt().getSeconds(), src.getCreatedAt().getNanos()), ZoneId.systemDefault()),
                src.getArticlesList().stream()
                        .map(ArticleDTO::from)
                        .toList(),
                PaymentMethod.valueOf(src.getPaymentMethod().name())
        );
    }
}
