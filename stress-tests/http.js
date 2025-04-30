import http from 'k6/http';
import { sleep, check } from 'k6';

export const options = {
  stages: [
    { duration: '10s', target: 8000 },
    { duration: '50s', target: 8000 },
    { duration: '1s', target: 0 },
  ],
};

function createArticles() {
    const articles = [];
    for(let i = 0; i < 1000; i++) {
        articles.push({
            id: "" + i,
            quantity: 1,
            unit_price: 10.5
        });
    }
}

const body = JSON.stringify({
  customerId: "AAABBB",
  createdAt: "2025-04-28T22:47:00+02:00",
  paymentMethod: "CREDIT_CARD",
  articles: createArticles()
})

export default () => {
  let res = http.post('http://localhost:8080/orders', body, {headers: { 'Content-Type': 'application/json' }});
  check(res, { "status is 200": (res) => res.status === 200 });
  sleep(1);
}
