package it.codingjam.springbootgrpc.services.dtos;

import it.condingjam.springbootgrpc.proto.orders.OrderedArticle;

public record ArticleDTO(
        String id,
        int quantity,
        float unitPrice
) {

    public static ArticleDTO from(OrderedArticle src) {
        return new ArticleDTO(src.getId(), src.getQuantity(), src.getUnitPrice());
    }
}
