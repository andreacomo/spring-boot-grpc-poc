import http from 'k6/http';
import { sleep, check } from 'k6';
import { textSummary } from 'https://jslib.k6.io/k6-summary/0.1.0/index.js';
import { htmlReport } from "https://raw.githubusercontent.com/benc-uk/k6-reporter/main/dist/bundle.js";
import { csvSummaryForHttpReport } from './csv-formatter.js';
import { forHttp } from './data-provider.js';

export const options = {
  stages: [
    { duration: '10s', target: 7000 },
    { duration: '50s', target: 7000 },
    { duration: '1s', target: 0 },
  ],
};

const body = JSON.stringify(forHttp(1000))

export default () => {
  let res = http.post('http://localhost:8080/orders', body, {headers: { 'Content-Type': 'application/json' }});
  check(res, { "status is 200": (res) => res.status === 200 });
  sleep(1);
}

export function handleSummary(data) {

  return {
    stdout: textSummary(data, { indent: ' ', enableColors: true }),
    'http_result.json': JSON.stringify(data),
    'http_result.csv': csvSummaryForHttpReport(data),
    'http_result.html': htmlReport(data)
  };
}