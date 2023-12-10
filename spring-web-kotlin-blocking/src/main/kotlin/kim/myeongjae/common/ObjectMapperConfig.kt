package kim.myeongjae.common

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.springframework.context.annotation.Configuration

@Configuration
class ObjectMapperConfig(objectMapper: ObjectMapper) {
    init {
        objectMapper.registerModule(KotlinModule.Builder().build())
    }
}
