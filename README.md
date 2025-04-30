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

## Docker container tests

Build the image with:
```shell
mvn spring-boot:build-image
```

the image produced is `spring-boot-grpc-poc:0.0.1-SNAPSHOT`

Run it:

```shell
docker run --cpus=1 -p "8080:8080" -p "9090:9090" -e JAVA_OPTS="-XX:MaxDirectMemorySize=500M" spring-boot-grpc-poc:0.0.1-SNAPSHOT
```

> Please note that `-XX:MaxDirectMemorySize` is required by Netty to allocate direct buffer memory (off heap)

### Results

**gRCP**
```shell
  █ TOTAL RESULTS 

    checks_total.......................: 118352  1778.412763/s
    checks_succeeded...................: 100.00% 118352 out of 118352
    checks_failed......................: 0.00%   0 out of 118352

    ✓ status is OK

    EXECUTION
    iteration_duration.....................: avg=3.88s min=139.66µs med=3.43s max=21.68s p(90)=6.87s p(95)=8.26s
    iterations.............................: 119112 1789.83288/s
    vus....................................: 4      min=0        max=8000
    vus_max................................: 8000   min=4628     max=8000

    NETWORK
    data_received..........................: 26 MB  393 kB/s
    data_sent..............................: 35 MB  524 kB/s

    GRPC
    grpc_req_duration......................: avg=1.22s min=200.58ms med=1.25s max=7.15s  p(90)=2s    p(95)=2.27s
```

with average usage of **400Mb** (docker stats) -> thanks to Netty?

**REST**

```shell
  █ TOTAL RESULTS 

    checks_total.......................: 85798   1322.844362/s
    checks_succeeded...................: 100.00% 85798 out of 85798
    checks_failed......................: 0.00%   0 out of 85798

    ✓ status is 200

    HTTP
    http_req_duration.......................................................: avg=4.35s min=518.24ms med=4.59s max=10.8s p(90)=6.2s p(95)=6.7s
      { expected_response:true }............................................: avg=4.35s min=518.24ms med=4.59s max=10.8s p(90)=6.2s p(95)=6.7s
    http_req_failed.........................................................: 0.00% 0 out of 85798
    http_reqs...............................................................: 85798 1322.844362/s

    EXECUTION
    iteration_duration......................................................: avg=5.35s min=1.51s    med=5.59s max=11.8s p(90)=7.2s p(95)=7.7s
    iterations..............................................................: 85798 1322.844362/s
    vus.....................................................................: 602   min=0          max=8000
    vus_max.................................................................: 8000  min=5610       max=8000

    NETWORK
    data_received...........................................................: 15 MB 225 kB/s
    data_sent...............................................................: 20 MB 311 kB/s
```

with average usage of **2.3Gb** (docker stats) -> due to Tomcat?

## Variants
* Branch `grpc-on-servlet` tries to use gRPC on Tomcat instead on Netty

**gRPC**
Just run with
```shell
docker run --cpus=1 -p "8080:8080"  spring-boot-grpc-poc:0.0.1-SNAPSHOT
```

```shell
  █ TOTAL RESULTS 

    checks_total.......................: 50689   775.897827/s
    checks_succeeded...................: 100.00% 50689 out of 50689
    checks_failed......................: 0.00%   0 out of 50689

    ✓ status is OK

    EXECUTION
    iteration_duration.....................: avg=9.21s min=21.48ms  med=9.31s max=41.53s p(90)=12.69s p(95)=13.91s
    iterations.............................: 50796 777.535678/s
    vus....................................: 3160  min=0        max=8000
    vus_max................................: 8000  min=4971     max=8000

    NETWORK
    data_received..........................: 11 MB 172 kB/s
    data_sent..............................: 14 MB 219 kB/s

    GRPC
    grpc_req_duration......................: avg=5.31s min=296.43ms med=5.31s max=10.9s  p(90)=7.91s  p(95)=8.78s 
```

with average usage of **4.2Gb** (docker stats) -> due to Tomcat??!!