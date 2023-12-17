package kim.myeongjae.graalvmkotlin.awslambda

import com.amazonaws.services.lambda.runtime.Context
import com.amazonaws.services.lambda.runtime.RequestHandler

class MyFunction : RequestHandler<Any, Any> {
    override fun handleRequest(input: Any?, context: Context?): Any {
        println("Hello, world!")

        return "return value"
    }
}
