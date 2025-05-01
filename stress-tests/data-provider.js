export function forHttp() {
    return {
        customerId: "AAABBB",
        createdAt: "2025-04-28T22:47:00+02:00",
        paymentMethod: "CREDIT_CARD",
        articles: createArticlesForHttp()
    };
}

export function forGrpc() {
    return {
       customer_id: "AAABBB",
       created_at: "2025-04-28T22:47:00+02:00",
       payment_method: "CREDIT_CARD",
       articles: createArticlesForGrpc()
   };
}

function createArticlesForHttp() {
    const articles = [];
    for(let i = 0; i < 1000; i++) {
        articles.push({
            id: "" + i,
            quantity: 1,
            unitPrice: 10.5
        });
    }
}

function createArticlesForGrpc() {
    const articles = [];
    for(let i = 0; i < 1000; i++) {
        articles.push({
            id: "" + i,
            quantity: 1,
            unit_price: 10.5
        });
    }
}