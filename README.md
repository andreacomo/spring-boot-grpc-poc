# Spring Boot gRCP POC

Playground with gRPC and Spring Boot.

This project is focused on stress test comparison between gRPC and REST.

## Exposed services

* REST endpoint for creating an order (`it.codingjam.springbootgrpc.rest.OrderResource`)
* gRPC endpoint for creating an order (`it.codingjam.springbootgrpc.grpc.RemoteOrderService`)

## Stress test

* Tool used: [k6](https://k6.io/)
* How to install: https://grafana.com/docs/k6/latest/set-up/install-k6/
* How to launch:
  * gRPC 
    ```shell
    k6 run ./stress-tests/grcp.js 
    ```
  * HTTP
    ```shell
    k6 run ./stress-tests/http.js 
    ```
> Prepend `K6_WEB_DASHBOARD=true` to the command to show results in a web page (on http://localhost:5665)

## Variants
* Branch `grpc-on-servlet` tries to use gRPC on Tomcat instead on Netty