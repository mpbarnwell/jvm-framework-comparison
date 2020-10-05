package com.bumble.devel.quarkus

import com.bumble.devel.quarkus.proto.GreeterGrpc
import com.bumble.devel.quarkus.proto.HelloReply
import com.bumble.devel.quarkus.proto.HelloRequest
import io.grpc.stub.StreamObserver
import java.nio.charset.StandardCharsets
import java.security.GeneralSecurityException
import java.security.MessageDigest
import javax.inject.Singleton

@Singleton
class HelloService : GreeterGrpc.GreeterImplBase() {
    override fun sayHello(request: HelloRequest, responseObserver: StreamObserver<HelloReply>) {
        val message = "Hello ${sha256(request.name)}"
        responseObserver.onNext(HelloReply.newBuilder().setMessage(message).build())
        responseObserver.onCompleted()
    }

    private fun sha256(input: String): String? {
        return try {
            val bytes = input.toByteArray(StandardCharsets.UTF_8)
            val md = MessageDigest.getInstance("SHA-256")
            val digest = md.digest(bytes)
            byteArrayToHex(digest)
        } catch (e: GeneralSecurityException) {
            throw RuntimeException(e) // Wrap in RTE, we don't expect this to occur in testing
        }
    }

    private fun byteArrayToHex(a: ByteArray): String? {
        val sb = StringBuilder(a.size * 2)
        for (b in a) sb.append(String.format("%02x", b))
        return sb.toString()
    }
}