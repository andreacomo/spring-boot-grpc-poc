import { Client, StatusOK } from 'k6/net/grpc';
import { check, sleep } from 'k6';

const client = new Client();
client.load(['definitions'], '../../src/main/proto/order.proto');

export const options = {
    stages: [
        { duration: '10s', target: 3000 },
        { duration: '50s', target: 3000 },
        { duration: '1s', target: 0 },
    ]
};

const data = {
    customer_id: "AAABBB",
    created_at: "2025-04-28T22:47:00+02:00",
    payment_method: "CREDIT_CARD",
    articles: [{
        id: "11",
        quantity: 1,
        unit_price: 10.5
    }]
};

export default () => {
    client.connect('localhost:9090', { plaintext: true });
  
    const response = client.invoke('OrderService/PlaceOrder', data);
  
    check(response, {
      'status is OK': (r) => r && r.status === StatusOK,
    });
  
    client.close();
    sleep(0.5);
};