import { Client, StatusOK } from 'k6/net/grpc';
import { check, sleep } from 'k6';
import { textSummary } from 'https://jslib.k6.io/k6-summary/0.1.0/index.js';
import { htmlReport } from "https://raw.githubusercontent.com/benc-uk/k6-reporter/main/dist/bundle.js";
import { csvSummaryForGrpcReport } from './csv-formatter.js';
import { forGrpc } from './data-provider.js';

const client = new Client();
client.load(['definitions'], '../../src/main/proto/order.proto');

export const options = {
    stages: [
        { duration: '10s', target: 8000 },
        { duration: '50s', target: 8000 },
        { duration: '1s', target: 0 },
    ]
};

const data = forGrpc();

export default () => {
    client.connect('localhost:9090', { plaintext: true });
  
    const response = client.invoke('OrderService/PlaceOrder', data);
  
    check(response, {
      'status is OK': (r) => r && r.status === StatusOK,
    });
  
    client.close();
    sleep(1);
};

export function handleSummary(data) {

  return {
    stdout: textSummary(data, { indent: ' ', enableColors: true }),
    'grpc_result.json': JSON.stringify(data),
    'grpc_result.csv': csvSummaryForGrpcReport(data),
    'grpc_result.html': htmlReport(data)
  };
}