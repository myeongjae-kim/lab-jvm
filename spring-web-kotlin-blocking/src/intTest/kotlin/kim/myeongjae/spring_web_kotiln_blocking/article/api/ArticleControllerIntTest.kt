package kim.myeongjae.spring_web_kotiln_blocking.article.api

import kim.myeongjae.common.Constants
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.jdbc.Sql
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.transaction.annotation.Transactional

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Sql("/data/test-article.sql")
@Transactional
class ArticleControllerIntTest @Autowired constructor(val mvc: MockMvc) {

    @Nested
    inner class GetPublishedArticle {
        @Test
        fun `should respond ok when a published article has been found`() {
            mvc.perform(MockMvcRequestBuilders.get("/articles/slug1"))
                .andExpect(MockMvcResultMatchers.status().isOk)
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").doesNotExist())
                .andExpect(MockMvcResultMatchers.jsonPath("$.published").doesNotExist())
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").isString)
                .andExpect(MockMvcResultMatchers.jsonPath("$.content").isString)
                .andExpect(MockMvcResultMatchers.jsonPath("$.createdAt").isString)
                .andExpect(MockMvcResultMatchers.jsonPath("$.updatedAt").isString)
                .andDo(MockMvcResultHandlers.print())
        }

        @Test
        fun `should respond not found when a requested article doesn't exist`() {
            mvc.perform(MockMvcRequestBuilders.get("/articles/slug2"))
                .andExpect(MockMvcResultMatchers.status().isNotFound)
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(404))
                .andExpect(MockMvcResultMatchers.jsonPath("$.error").value("Not Found"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").isString)
                .andExpect(MockMvcResultMatchers.jsonPath("$.timestamp").isString)
                .andDo(MockMvcResultHandlers.print())
        }
    }

    @Nested
    inner class GetArticle {
        @ParameterizedTest
        @CsvSource(value = ["slug1,true", "slug2, false"])
        fun `should respond ok either an article has been published or not`(slug: String, published: Boolean) {
            mvc.perform(MockMvcRequestBuilders.get("/articles/$slug").header(Constants.HEADER_INTERNAL, ""))
                .andExpect(MockMvcResultMatchers.status().isOk)
                .andExpect(MockMvcResultMatchers.jsonPath("$.published").value(published))
        }
    }
}
