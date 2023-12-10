package kim.myeongjae.springwebkotilnblocking.article.domain.model

import com.fasterxml.jackson.databind.exc.InvalidDefinitionException
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.github.projectmapk.jackson.module.kogera.readValue
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.github.projectmapk.jackson.module.kogera.jacksonObjectMapper as jacksonObjectMapperKogera

private class ExampleResponse(val id: ArticleId)
data class ExampleRequest(val id: ArticleId = ArticleId(0L), val foo: String = "")

class ArticleIdTest : StringSpec(
    {
        val objectMapperKogera = jacksonObjectMapperKogera()
        val objectMapper = jacksonObjectMapper()

        "serialize" {
            objectMapper.writeValueAsString(ArticleId(1L)) shouldBe "1"
            objectMapper.writeValueAsString(ExampleResponse(ArticleId(1L))) shouldBe "{\"id\":1}"
        }

        "deserialize_fail" {
            shouldThrow<InvalidDefinitionException> {
                objectMapper.readValue<ExampleRequest>(
                    """
                {
                  "id": 1,
                  "foo": "bar"
                }
                    """.trimIndent(),
                )
            }
        }

        "deserialize_kogera" {
            val obj: ExampleRequest = objectMapperKogera.readValue(
                """
                {
                  "id": 1,
                  "foo": "bar"
                }
                """.trimIndent(),
            )
            obj shouldBe ExampleRequest(ArticleId(1L), "bar")
        }
    },
)
