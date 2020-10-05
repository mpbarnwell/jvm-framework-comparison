package com.bumble.devel.armeria;

import com.bumble.devel.armeria.service.GreetingService;
import com.linecorp.armeria.common.HttpResponse;
import com.linecorp.armeria.server.Server;
import com.linecorp.armeria.server.ServerBuilder;
import com.linecorp.armeria.server.docs.DocService;
import com.linecorp.armeria.server.grpc.GrpcService;

import java.util.concurrent.CompletableFuture;

public class ArmeriaServer {

    public static void main(String[] args) throws Exception {
        new ArmeriaServer();
    }

    public ArmeriaServer() throws Exception {

        ServerBuilder sb = Server.builder();
        sb.http(8080);

        // Add a simple 'Hello, world!' service.
        sb.service("/", (ctx, req) -> HttpResponse.of("Hello, world!"));

        sb.service(GrpcService.builder()
                .addService(new GreetingService())
                .build());

        sb.serviceUnder("/docs", new DocService());

        Server server = sb.build();
        CompletableFuture<Void> future = server.start();

        // Wait until the server is ready.
        future.join();
    }

}
