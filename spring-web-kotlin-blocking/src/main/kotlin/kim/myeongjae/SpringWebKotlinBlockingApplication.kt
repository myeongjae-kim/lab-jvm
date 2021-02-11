package kim.myeongjae

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class SpringWebKotlinBlockingApplication

fun main(args: Array<String>) {
    runApplication<SpringWebKotlinBlockingApplication>(*args)
}
