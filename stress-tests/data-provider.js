export function forHttp(articlesCount) {
    return {
        customerId: "AAABBB",
        createdAt: "2025-04-28T22:47:00+02:00",
        paymentMethod: "CREDIT_CARD",
        articles: createArticles(articlesCount, function(i) {
            return {
                id: "" + i,
                quantity: 1,
                unitPrice: 10.5
           }
        })
    };
}

export function forGrpc(articlesCount) {
    return {
       customer_id: "AAABBB",
       created_at: "2025-04-28T22:47:00+02:00",
       payment_method: "CREDIT_CARD",
       articles: createArticles(articlesCount, function(i) {
            return {
                id: "" + i,
                quantity: 1,
                unit_price: 10.5
           }
       })
   };
}

function createArticles(articlesCount, creator) {
    const articles = [];
    for(let i = 0; i < articlesCount; i++) {
        articles.push(creator(i));
    }
}
