package com.bumble.devel.helidon;

import com.bumble.devel.helidon.proto.HelloReply;
import com.bumble.devel.helidon.proto.HelloRequest;
import com.bumble.devel.helidon.proto.HelloWorldProto;
import io.grpc.stub.StreamObserver;
import io.helidon.grpc.server.GrpcService;
import io.helidon.grpc.server.ServiceDescriptor;

import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;

public class Greeter implements GrpcService {

    @Override
    public void update(ServiceDescriptor.Rules rules) {
        rules.proto(HelloWorldProto.getDescriptor())
                .unary("SayHello", this::sayHello);
    }

    public void sayHello(HelloRequest request,
                         StreamObserver<HelloReply> responseObserver) {
        var message = String.format("Hello %s", sha256(request.getName()));
        var reply = HelloReply.newBuilder().setMessage(message).build();
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
