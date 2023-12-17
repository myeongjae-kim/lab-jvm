package kim.myeongjae.graalvmkotlin.awslambda

import com.amazonaws.services.lambda.runtime.Context
import com.amazonaws.services.lambda.runtime.RequestHandler
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.URI
import java.util.stream.Collectors

class MyFunction : RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    override fun handleRequest(input: APIGatewayProxyRequestEvent, context: Context): APIGatewayProxyResponseEvent {
        val headers: MutableMap<String, String> = HashMap()
        headers["Content-Type"] = "application/json"
        headers["X-Custom-Header"] = "application/json"

        val response = APIGatewayProxyResponseEvent().withHeaders(headers)

        return try {
            val pageContents: String = this.getPageContents("https://checkip.amazonaws.com")
            val output = String.format("{ \"message\": \"hello world\", \"location\": \"%s\" }", pageContents)
            response
                .withStatusCode(200)
                .withBody(output)
        } catch (e: IOException) {
            response
                .withBody("{}")
                .withStatusCode(500)
        }
    }

    private fun getPageContents(address: String): String {
        val url = URI(address).toURL()
        BufferedReader(InputStreamReader(url.openStream())).use { br ->
            return br.lines().collect(Collectors.joining(System.lineSeparator()))
        }
    }
}
