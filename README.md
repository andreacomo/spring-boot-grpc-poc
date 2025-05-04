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


### Summary Results
Tests executed with
```shell
docker run --cpus=1 -m1.5Gb -p "8080:8080" -p "9090:9090" -e JAVA_OPTS="-XX:MaxDirectMemorySize=100M" -e SPRING_THREADS_VIRTUAL_ENABLED=[true|false] spring-boot-grpc-poc:0.0.1-SNAPSHOT
```
> Please note that:
> * `-XX:MaxDirectMemorySize` is required by Netty to allocate direct buffer memory (off heap)
> * `SPRING_THREADS_VIRTUAL_ENABLED` is used to enable/disable virtual threads on Tomcat
> * each test is executed for **one minute** with the container **fresh started**

| vus  | type                | avg (ms)           | min (ms)   | med (ms)           | max (ms)    | p(90) (ms)         | p(95) (ms)         | count (requests) | rate (requests/sec) | memory (Mb) |
|------|---------------------|--------------------|------------|--------------------|-------------|--------------------|--------------------|------------------|---------------------|-------------|
| 1000 | REST (blocking)     | 714.7025547509654  | 202.273    | 736.871            | 947.814     | 871.9782           | 889.8036           | 32867            | 526.7442547742739   | 275         |
| 1000 | REST (non blocking) | 351.0700914427292  | 200.647    | 350.051            | 589.363     | 470.8716           | 486.4961           | 41567            | 665.9832248876203   | 326         |
| 1000 | gRPC                | 351.5182599118609  | 200.612625 | 352.169083         | 581.142333  | 471.0427586        | 485.7633418        | 41525            | 665.5853382665775   | 180         |
|      |                     |                    |            |                    |             |                    |                    |                  |                     |             |
| 2000 | REST (blocking)     | 2282.200907110713  | 202.16     | 2483.441           | 2729.242    | 2627.367           | 2647.7985          | 34891            | 543.828601359997    | 280         |
| 2000 | REST (non blocking) | 352.6173010656885  | 200.61     | 352.828            | 660.244     | 472.821            | 487.67939999999993 | 83045            | 1330.3495398299394  | 430         |
| 2000 | gRPC                | 355.86861497457335 | 200.736083 | 355.8920835        | 683.522     | 476.43875390000005 | 490.72143355       | 82630            | 1324.222761929315   | 205         |
|      |                     |                    |            |                    |             |                    |                    |                  |                     |             |
| 3000 | REST (blocking)     | 3826.572995431539  | 219.086    | 4218.081           | 4472.951    | 4367.7483999999995 | 4390.812800000001  | 36117            | 548.1946285972664   | 306         |
| 3000 | REST (non blocking) | 352.59869560855043 | 200.563    | 352.627            | 624.248     | 472.8658           | 487.887            | 124583           | 1994.466728812015   | 550         |
| 3000 | gRPC                | 379.6421016323721  | 200.72625  | 373.8226045        | 1189.103917 | 497.57718800000004 | 559.081823         | 120766           | 1933.4599732591885  | 235         |
|      |                     |                    |            |                    |             |                    |                    |                  |                     |             |
| 4000 | REST (blocking)     | 5420.913514566001  | 800.751    | 5986.8785          | 6238.533    | 6139.5928          | 6162.15365         | 36798            | 543.3415915408101   | 350         |
| 4000 | REST (non blocking) | 363.6338862069337  | 200.556    | 362.5955           | 898.804     | 484.0477           | 498.2177           | 164764           | 2641.097014150092   | 840         |
| 4000 | gRPC                | 481.85526106307026 | 200.848208 | 423.978209         | 1522.583167 | 857.6090166000001  | 1008.0793547999999 | 143070           | 2242.061777883373   | 270         |
|      |                     |                    |            |                    |             |                    |                    |                  |                     |             |
| 5000 | REST (blocking)     | 7017.818045475263  | 1337.959   | 7732.356           | 7990.709    | 7887.5252          | 7910.6502          | 37295            | 537.868207708477    | 380         |
| 5000 | REST (non blocking) | 749.6652583641677  | 200.663    | 522.755            | 3686.14     | 1431.5525000000005 | 2061.3662499999987 | 160506           | 2570.8792132692515  | 895         |
| 5000 | gRPC                | 753.8113438916109  | 202.449417 | 697.856479         | 1991.370167 | 1191.6519165       | 1311.643083        | 137885           | 2208.7129187399964  | 305         |
|      |                     |                    |            |                    |             |                    |                    |                  |                     |             |
| 6000 | REST (blocking)     | 8335.748249274759  | 922.62     | 9465.067           | 9761.778    | 9630.9874          | 9656.9362          | 38953            | 548.3219775142803   | 405         |
| 6000 | REST (non blocking) | 1511.0606228801373 | 200.679    | 1566.6055000000001 | 3634.943    | 3077.6335000000004 | 3214.1992499999997 | 138216           | 2178.110796494757   | 900         |
| 6000 | gRPC                | 1108.9839999059966 | 207.325375 | 1054.023375        | 2168.357167 | 1723.799125        | 1887.297           | 128085           | 2017.5011709492683  | 350         |
|      |                     |                    |            |                    |             |                    |                    |                  |                     |             |
| 7000 | REST (blocking)     | 9819.940348718483  | 877.085    | 11222.493999999999 | 12070.309   | 11598.8665         | 11686.0193         | 39952            | 547.330460478602    | 440         |
| 7000 | REST (non blocking) | 3753.84656411112   | 571.078    | 2822.0035          | 9265.986    | 7791.3674          | 8492.29585         | 83792            | 1327.5870261323382  | 1000        |
| 7000 | gRPC                | 1684.1909574021865 | 213.290584 | 1644.8751665       | 3806.348791 | 2440.4313920000004 | 2632.2106958       | 109822           | 1729.2383305227556  | 375         |

### Other specific results
#### gRCP
Run it (**without** memory limit -> 11Gb):

```shell
docker run --cpus=1 -p "9090:9090" -e JAVA_OPTS="-XX:MaxDirectMemorySize=100M" spring-boot-grpc-poc:0.0.1-SNAPSHOT
```

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

with average usage of **400Mb** (docker stats) -> thanks to Netty

---

Run it (**with** memory limit -> 1.5Gb):

```shell
docker run --cpus=1 -m=1.5Gb -p "9090:9090" -e JAVA_OPTS="-XX:MaxDirectMemorySize=200M" spring-boot-grpc-poc:0.0.1-SNAPSHOT
```

```shell
  █ TOTAL RESULTS 

    checks_total.......................: 106668  1489.092406/s
    checks_succeeded...................: 100.00% 106668 out of 106668
    checks_failed......................: 0.00%   0 out of 106668

    ✓ status is OK

    EXECUTION
    iteration_duration.....................: avg=4.27s min=187.5µs  med=4.18s max=36.92s p(90)=6.66s p(95)=7.74s
    iterations.............................: 108084 1508.859861/s
    vus....................................: 1      min=0         max=8000
    vus_max................................: 8000   min=4706      max=8000

    NETWORK
    data_received..........................: 24 MB  329 kB/s
    data_sent..............................: 31 MB  436 kB/s

    GRPC
    grpc_req_duration......................: avg=1.66s min=201.95ms med=1.59s max=7.57s  p(90)=2.67s p(95)=2.98s
```

with average usage of **400Mb** (docker stats) -> thanks to Netty

#### REST with **Virtual Threads** on Tomcat
Run it (**without** memory limit -> 11Gb):

```shell
docker run --cpus=1 -p "8080:8080" -p "9090:9090" spring-boot-grpc-poc:0.0.1-SNAPSHOT
```

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

with average usage of **2.3Gb** (docker stats) -> due to **Virtual thread memory allocation**?
That's strange because Virtual thread are also used in gRPC version (gRPC service delegates to a virtual thread)

---

Run it (**with** memory limit -> 1.5Gb):

```shell
docker run --cpus=1 -m=1.5Gb -p "8080:8080" -p "9090:9090" spring-boot-grpc-poc:0.0.1-SNAPSHOT
```

```shell
  █ TOTAL RESULTS 

    checks_total.......................: 40210   628.911586/s
    checks_succeeded...................: 100.00% 40210 out of 40210
    checks_failed......................: 0.00%   0 out of 40210

    ✓ status is 200

    HTTP
    http_req_duration.......................................................: avg=10.46s min=691.29ms med=4.63s max=31.09s p(90)=29.18s p(95)=29.76s
      { expected_response:true }............................................: avg=10.46s min=691.29ms med=4.63s max=31.09s p(90)=29.18s p(95)=29.76s
    http_req_failed.........................................................: 0.00%  0 out of 40210
    http_reqs...............................................................: 40210  628.911586/s

    EXECUTION
    iteration_duration......................................................: avg=11.46s min=1.69s    med=5.63s max=32.09s p(90)=30.19s p(95)=30.76s
    iterations..............................................................: 40210  628.911586/s
    vus.....................................................................: 992    min=0          max=8000
    vus_max.................................................................: 8000   min=5229       max=8000

    NETWORK
    data_received...........................................................: 6.8 MB 107 kB/s
    data_sent...............................................................: 9.4 MB 148 kB/s
```

with average usage of **1.0Gb** (docker stats) -> thanks to GC?

#### REST without **Virtual Threads** on Tomcat
Run it (**without** memory limit -> 11Gb):

```shell
docker run --cpus=1 -p "8080:8080" -p "9090:9090" -e SPRING_THREADS_VIRTUAL_ENABLED=false spring-boot-grpc-poc:0.0.1-SNAPSHOT
```

```shell
  █ TOTAL RESULTS 

    checks_total.......................: 40689   544.845634/s
    checks_succeeded...................: 100.00% 40689 out of 40689
    checks_failed......................: 0.00%   0 out of 40689

    ✓ status is 200

    HTTP
    http_req_duration.......................................................: avg=11.28s min=1.06s med=12.95s max=13.24s p(90)=13.12s p(95)=13.15s
      { expected_response:true }............................................: avg=11.28s min=1.06s med=12.95s max=13.24s p(90)=13.12s p(95)=13.15s
    http_req_failed.........................................................: 0.00%  0 out of 40689
    http_reqs...............................................................: 40689  544.845634/s

    EXECUTION
    iteration_duration......................................................: avg=12.28s min=2.06s med=13.95s max=14.24s p(90)=14.12s p(95)=14.15s
    iterations..............................................................: 40689  544.845634/s
    vus.....................................................................: 34     min=0          max=8000
    vus_max.................................................................: 8000   min=5350       max=8000

    NETWORK
    data_received...........................................................: 6.9 MB 93 kB/s
    data_sent...............................................................: 9.6 MB 128 kB/s
```

with average usage of **450Mb** (docker stats)

---

Run it (**with** memory limit -> 1.5Gb):

```shell
docker run --cpus=1 -m=1.5Gb -p "8080:8080" -e SPRING_THREADS_VIRTUAL_ENABLED=false spring-boot-grpc-poc:0.0.1-SNAPSHOT
```

```shell
  █ TOTAL RESULTS 

    checks_total.......................: 41160   551.568373/s
    checks_succeeded...................: 100.00% 41160 out of 41160
    checks_failed......................: 0.00%   0 out of 41160

    ✓ status is 200

    HTTP
    http_req_duration.......................................................: avg=11.14s min=880.15ms med=12.97s max=13.28s p(90)=13.14s p(95)=13.17s
      { expected_response:true }............................................: avg=11.14s min=880.15ms med=12.97s max=13.28s p(90)=13.14s p(95)=13.17s
    http_req_failed.........................................................: 0.00%  0 out of 41160
    http_reqs...............................................................: 41160  551.568373/s

    EXECUTION
    iteration_duration......................................................: avg=12.14s min=1.88s    med=13.97s max=14.28s p(90)=14.14s p(95)=14.17s
    iterations..............................................................: 41160  551.568373/s
    vus.....................................................................: 20     min=0          max=8000
    vus_max.................................................................: 8000   min=5298       max=8000

    NETWORK
    data_received...........................................................: 7.0 MB 94 kB/s
    data_sent...............................................................: 9.7 MB 130 kB/s
```

with average usage of **450Mb** (docker stats) -> same as without limits

## Variants
* Branch `grpc-on-servlet` tries to use gRPC on Tomcat instead on Netty

**gRPC**
Just run with
```shell
docker run --cpus=1 -p "8080:8080" spring-boot-grpc-poc:0.0.1-SNAPSHOT
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