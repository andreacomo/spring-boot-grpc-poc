package it.codingjam.springbootgrpc.config;

import io.grpc.protobuf.services.ProtoReflectionService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.grpc.server.exception.GrpcExceptionHandler;

@Configuration
public class GrpcConfig {

    /**
     * Needed by Postman discovery mechanism
     * @return
     */
    @Bean
    ProtoReflectionService v1AlphaProtoReflectionService() {
        return (ProtoReflectionService) ProtoReflectionService.newInstance();
    }
}
