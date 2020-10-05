package com.bumble.devel.armeria.service;

import com.bumble.devel.micronaut.proto.GreeterGrpc;
import com.bumble.devel.micronaut.proto.HelloReply;
import com.bumble.devel.micronaut.proto.HelloRequest;
import io.grpc.stub.StreamObserver;

import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;

public class GreetingService extends GreeterGrpc.GreeterImplBase {

    @Override
    public void sayHello(HelloRequest req, StreamObserver<HelloReply> responseObserver) {
        var reply = HelloReply.newBuilder()
                .setMessage("Hello " + sha256(req.getName()))
                .build();
        responseObserver.onNext(reply);
        responseObserver.onCompleted();
    }

    private String sha256(String input) {
        try {
            var bytes = input.getBytes(StandardCharsets.UTF_8);
            var md = MessageDigest.getInstance("SHA-256");
            var digest = md.digest(bytes);

            return byteArrayToHex(digest);
        } catch (GeneralSecurityException e) {
            throw new RuntimeException(e); // Wrap in RTE, we don't expect this to occur in testing
        }
    }

    public static String byteArrayToHex(byte[] a) {
        var sb = new StringBuilder(a.length * 2);
        for(byte b: a)
            sb.append(String.format("%02x", b));
        return sb.toString();
    }


}
