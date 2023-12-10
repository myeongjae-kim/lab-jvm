package kim.myeongjae.springwebkotilnblocking.article.domain.model

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

private class ExampleResponse(val id: ArticleId)

class ArticleIdTest : StringSpec({

    "serialize" {
        val objectMapper = ObjectMapper().apply {
            registerModule(KotlinModule.Builder().build())
        }

        objectMapper.writeValueAsString(ArticleId(1L)) shouldBe "1"
        objectMapper.writeValueAsString(ExampleResponse(ArticleId(1L))) shouldBe "{\"id\":1}"
    }
},)
