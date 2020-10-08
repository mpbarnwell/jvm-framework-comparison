package ratpack.grpc.server

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import ratpack.grpc.GreeterClient
import ratpack.grpc.GreeterService
import ratpack.guice.Guice

fun main() {
    val test = GrpcRatpackServerTest()
    test.setup()
}

class GrpcRatpackServerTest {

    var server: GrpcRatpackServer? = null
    var client: GreeterClient? = null

    @BeforeEach
    fun setup() {
        val port = 50051
        client = GreeterClient("localhost", port)
        server = GrpcRatpackServer.start {
            it.serverConfig {
                it.port(port)
            }
            it.registry(Guice.registry {
                it.bind(GreeterService::class.java)
            })
        }
    }

    @AfterEach
    fun cleanup() {
        server?.stop()
        client?.shutdown()
    }

//    @Test
    fun `test grpc ratpack server`() {
        assertTrue(server?.isRunning ?: false, "server is not running")
        val user = "drmaas"
        val response = client?.greet(user)
        assert(response == "Hello drmaas")
    }

}